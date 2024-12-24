package dormitorylifepass.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.dto.RoomDto;
import dormitorylifepass.entity.Building;
import dormitorylifepass.entity.Room;
import dormitorylifepass.entity.Student;
import dormitorylifepass.enums.BuildingType;
import dormitorylifepass.enums.RoomStatus;
import dormitorylifepass.mapper.RoomMapper;
import dormitorylifepass.service.BuildingService;
import dormitorylifepass.service.RoomService;
import dormitorylifepass.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private StudentService studentService;

    /**
     * 根据楼层查询房间信息
     *
     * @param page 分页对象，用于封装分页查询所需的信息
     * @param name 房间名称的关键词，用于模糊查询
     */
    @Override
    public void selectPage(Page<Room> page, String name) {
        // 创建Lambda查询条件包装器，用于构建查询条件和排序
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();

        // 如果名称不为空且不只包含空格，则按名称进行模糊查询，并按楼层升序排序
        queryWrapper.like(StringUtil.notNullNorEmpty(name), Room::getName, name)
                .orderByAsc(Room::getFloor);

        // 执行分页查询
        page(page, queryWrapper);
    }

    /**
     * 根据性别选择房间列表
     * 此方法首先根据性别类型获取相应的楼宇列表，然后获取这些楼宇下的所有房间
     *
     * @param gender 性别类型，用于筛选楼宇类型
     * @return 符合条件的房间列表
     */
    @Override
    public List<Room> selectList(Integer gender) {
        // 更新房间状态，确保房间状态是最新的
        updateStatus();

        // 创建楼宇查询条件，筛选出符合性别类型的楼宇
        LambdaQueryWrapper<Building> queryWrapperBuilding = new LambdaQueryWrapper<>();
        queryWrapperBuilding.eq(Building::getType, BuildingType.toEnum(gender));

        // 查询符合条件的楼宇列表，并提取其ID
        List<Building> buildingsDB = buildingService.list(queryWrapperBuilding);
        List<Long> buildingIdsDB = buildingsDB.stream().map(Building::getId).toList();

        // 创建房间查询条件，筛选出属于查询楼宇列表中的房间
        LambdaQueryWrapper<Room> queryWrapperRoom = new LambdaQueryWrapper<>();
        queryWrapperRoom.in(Room::getBuildingId, buildingIdsDB)
                // 确保房间状态为可用
                .eq(Room::getStatus, RoomStatus.AVAILABLE)
                // 按房间名称升序排序
                .orderByAsc(Room::getName);

        // 返回符合条件的房间列表
        return list(queryWrapperRoom);
    }

    /**
     * 插入一个新的房间记录到数据库中
     * 此方法用于初始化房间的状态，并将其保存到数据库中
     *
     * @param room 要插入的房间对象，包含房间的相关信息
     */
    @Override
    public void insertRoom(Room room) {
        // 设置房间状态为可用，表示房间可以被预订或使用
        room.setStatus(RoomStatus.AVAILABLE);
        // 保存房间对象到数据库中
        save(room);
    }

    /**
     * 根据楼宇ID搜索房间信息
     * <p>
     * 此方法首先从数据库中获取指定ID的楼宇信息，然后查询该楼宇下的所有房间信息
     * 对于每个房间，进一步查询居住在该房间的学生信息，并将这些信息整合到一个DTO列表中返回
     *
     * @param buildingId 楼宇ID，用于查询特定楼宇及其房间信息
     * @return 包含房间及其相关信息的DTO列表
     */
    @Override
    public List<RoomDto> searchInfo(Long buildingId) {
        // 更新房间状态，确保房间状态是最新的
        updateStatus();

        // 根据楼宇ID获取楼宇实体
        Building buildingDB = buildingService.getById(buildingId);

        // 查询该楼宇下的所有房间
        LambdaQueryWrapper<Room> queryWrapperRoom = new LambdaQueryWrapper<>();
        queryWrapperRoom.eq(Room::getBuildingId, buildingId);
        List<Room> roomsDB = list(queryWrapperRoom);

        // 将房间实体转换为DTO，并为每个DTO设置楼宇名称和学生信息
        // 返回包含所有房间DTO的列表
        return roomsDB.stream().map(room -> {
            RoomDto roomDto = new RoomDto();
            BeanUtils.copyProperties(room, roomDto);
            roomDto.setBuildingName(buildingDB.getName());

            // 查询居住在该房间的所有学生
            LambdaQueryWrapper<Student> queryWrapperStudent = new LambdaQueryWrapper<>();
            queryWrapperStudent.eq(Student::getRoomId, room.getId());
            List<Student> studentsDB = studentService.list(queryWrapperStudent);
            roomDto.setStudents(studentsDB);

            // 设置房间的居住人数
            roomDto.setPeopleNum(studentsDB.size());
            return roomDto;
        }).toList();
    }

    /**
     * 更新房间状态
     * 根据每个房间的入住学生数量与房间最大容纳人数来更新房间的状态
     * 如果房间的入住学生数量少于最大容纳人数，则房间状态为可用，否则为不可用
     */
    @Override
    @Transactional
    public void updateStatus() {
        // 获取所有房间列表
        List<Room> roomsDB = list();

        // 遍历每个房间
        roomsDB.forEach(room -> {
            // 创建查询条件，查询与房间关联的学生数量
            LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Student::getRoomId, room.getId());
            // 计算入住该房间的学生数量
            int count = (int) studentService.count(queryWrapper);

            // 根据房间的入住学生数量与最大容纳人数，更新房间状态
            room.setStatus(count < room.getMaxNum() ? RoomStatus.AVAILABLE : RoomStatus.UNAVAILABLE);
        });

        // 批量更新房间状态
        updateBatchById(roomsDB);

        // 清除Redis中与房间相关的缓存
        Set<String> keys = redisTemplate.keys("room:*");
        redisTemplate.delete(Objects.requireNonNull(keys));
    }

    /**
     * 根据ID查询房间信息
     * 首先尝试从Redis缓存中获取数据，如果缓存不存在，则从数据库中查询
     * 并将查询结果缓存到Redis中，以提高下次查询的效率
     *
     * @param id 房间的唯一标识符
     * @return 返回房间对象，如果未找到则返回null
     */
    @Override
    public Room queryOne(Long id) {
        // 构造Redis缓存的键
        String key = "room:roomId:" + id;
        // 检查Redis缓存中是否存在该房间的信息
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            // 如果缓存存在，则直接从缓存中获取并返回房间对象
            return (Room) redisTemplate.opsForValue().get(key);
        }

        // 如果缓存不存在，则从数据库中查询房间信息
        Room roomDB = getById(id);
        // 将查询到的房间信息存入Redis缓存，并设置缓存过期时间为15分钟
        redisTemplate.opsForValue().set(key, roomDB, 15, TimeUnit.MINUTES);

        // 返回从数据库中查询到的房间信息
        return roomDB;
    }

    /**
     * 根据房间ID更新房间信息，并同步更新缓存
     * 当房间信息被更新时，需要确保缓存中的数据与数据库中的数据保持一致
     * 此方法首先调用updateById方法来更新数据库中的房间信息，然后删除缓存中的相应房间信息
     * 这样可以确保下一次查询该房间信息时，能够从数据库中获取到最新的数据并重新缓存
     *
     * @param room 要更新的房间对象，包含要更新的房间信息
     */
    @Override
    public void updateByRoomId(Room room) {
        updateById(room);
        redisTemplate.delete("room:roomId:" + room.getId());
    }

    /**
     * 根据ID删除实体，并从Redis中删除对应的缓存
     *
     * @param id 实体的ID，用于数据库和Redis缓存中的定位
     */
    @Override
    public void deleteById(Long id) {
        // 删除数据库中的实体
        removeById(id);
        // 删除Redis中与该实体ID对应的缓存
        redisTemplate.delete("room:roomId:" + id);
    }
}
