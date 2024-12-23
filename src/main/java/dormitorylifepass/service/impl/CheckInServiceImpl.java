package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.common.BaseEntity;
import dormitorylifepass.dto.CheckInDto;
import dormitorylifepass.entity.*;
import dormitorylifepass.enums.CheckInStatus;
import dormitorylifepass.mapper.CheckInMapper;
import dormitorylifepass.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements CheckInService {
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 插入新的签到记录并设置其状态为进行中
     * <p>
     * 此方法接收一个签到对象作为参数，并将其状态设置为正在进行中的签到，
     * 然后调用save方法来保存这个签到记录到数据库或持久化存储中
     *
     * @param checkIn 签到对象，包含签到的相关信息
     */
    @Override
    public void insertCheckIn(CheckIn checkIn) {
        // 查询员工对应的楼宇信息
        LambdaQueryWrapper<Building> queryWrapperBuilding = new LambdaQueryWrapper<>();
        queryWrapperBuilding.eq(Building::getEmployeeId, checkIn.getEmployeeId());
        Building buildingDB = buildingService.getOne(queryWrapperBuilding);
        // 设置签到记录关联的楼宇ID
        checkIn.setBuildingId(buildingDB.getId());

        // 查询楼宇对应的房间信息
        LambdaQueryWrapper<Room> queryWrapperRoom = new LambdaQueryWrapper<>();
        queryWrapperRoom.eq(Room::getBuildingId, buildingDB.getId());
        List<Room> roomsDB = roomService.list(queryWrapperRoom);
        List<Long> roomIdsDB = roomsDB.stream().map(BaseEntity::getId).toList();
        // 统计房间对应的学生数量
        LambdaQueryWrapper<Student> queryWrapperStudent = new LambdaQueryWrapper<>();
        queryWrapperStudent.in(Student::getRoomId, roomIdsDB);
        int count = (int) studentService.count(queryWrapperStudent);
        checkIn.setTotalNum(count);

        // 设置签到记录的状态为正在进行中
        checkIn.setStatus(CheckInStatus.ONGOING);
        // 保存签到记录
        save(checkIn);
    }

    /**
     * 重写selectPage方法，用于查询并转换检查记录到DTO格式的分页数据
     * 此方法不仅处理分页逻辑，还负责将检查记录与员工、楼宇信息关联，以DTO形式返回
     *
     * @param page 检查记录的分页对象，包含分页参数和检查记录列表
     * @return 返回一个转换后的CheckInDto分页对象，包含员工和楼宇名称
     */
    @Override
    public Page<CheckInDto> selectPage(Page<CheckIn> page) {
        // 执行分页查询
        page(page);

        // 创建一个新的CheckInDto分页对象，并从原分页对象中复制除records外的其他属性
        Page<CheckInDto> checkInDtoPage = new Page<>();
        BeanUtils.copyProperties(page, checkInDtoPage, "records");

        // 从分页对象中获取检查记录列表，并转换为CheckInDto列表
        List<CheckIn> checkInsDB = page.getRecords();
        List<CheckInDto> checkInDtos = getDtos(checkInsDB);

        // 将转换后的CheckInDto列表设置到分页对象中
        checkInDtoPage.setRecords(checkInDtos);

        // 返回转换后的分页对象
        return checkInDtoPage;
    }

    /**
     * 将检查记录列表转换为DTO列表
     * 此方法通过映射每个检查记录到一个DTO对象，并 enriched 与员工和楼宇信息，从而准备数据以供进一步使用或传输
     *
     * @param checkInsDB 从数据库获取的检查记录列表
     * @return 转换后的检查记录DTO列表，包含员工和楼宇名称信息
     */
    private List<CheckInDto> getDtos(List<CheckIn> checkInsDB) {
        return checkInsDB.stream().map(checkIn -> {
            // 创建一个新的CheckInDto对象，并从检查记录中复制属性
            CheckInDto checkInDto = new CheckInDto();
            BeanUtils.copyProperties(checkIn, checkInDto);

            // 根据员工ID获取员工信息，并设置员工名称到DTO
            Employee employeeDB = employeeService.getById(checkIn.getEmployeeId());
            checkInDto.setEmployeeName(employeeDB.getName());

            // 根据楼宇ID获取楼宇信息，并设置楼宇名称到DTO
            Building buildingDB = buildingService.getById(checkIn.getBuildingId());
            checkInDto.setBuildingName(buildingDB.getName());

            // 返回转换后的CheckInDto对象
            return checkInDto;
        }).toList();
    }

    /**
     * 根据学生ID选择入住信息列表
     * <p>
     * 此方法首先根据提供的学生ID获取学生信息，然后获取该学生所在房间的信息，
     * 进而得到宿舍楼ID接下来，它构建一个查询条件，以获取与该学生相关
     * 的入住信息这可能涉及到该学生特定的入住记录，或者根据宿舍楼ID获取该楼的所有入住记录
     *
     * @param id 学生ID，用于查询入住信息
     * @return 返回一个CheckInDto对象列表，包含与学生相关的入住信息
     */
    @Override
    public List<CheckInDto> selectList(Long id) {
        // 构建查询条件，用于查询入住信息
        LambdaQueryWrapper<CheckIn> queryWrapper = new LambdaQueryWrapper<>();

        // 根据学生ID获取学生信息
        Student studentDB = studentService.getById(id);
        if (studentDB != null) {
            // 获取学生所在房间的信息
            Room roomDB = roomService.getById(studentDB.getRoomId());
            // 获取宿舍楼ID
            Long buildingId = roomDB.getBuildingId();
            queryWrapper.eq(CheckIn::getBuildingId, buildingId);
        } else {
            queryWrapper.eq(CheckIn::getEmployeeId, id);
        }
        // 执行查询，获取入住信息列表
        List<CheckIn> checkInsDB = list(queryWrapper);
        // 将查询结果转换为DTO列表，并返回
        return getDtos(checkInsDB);
    }
}
