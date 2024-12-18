package dormitorylifepass.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.Building;
import dormitorylifepass.entity.Room;
import dormitorylifepass.enums.BuildingType;
import dormitorylifepass.enums.RoomStatus;
import dormitorylifepass.mapper.RoomMapper;
import dormitorylifepass.service.BuildingService;
import dormitorylifepass.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
    @Autowired
    private BuildingService buildingService;

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
        // 创建楼宇查询条件，筛选出符合性别类型的楼宇
        LambdaQueryWrapper<Building> queryWrapperBuilding = new LambdaQueryWrapper<>();
        queryWrapperBuilding.eq(Building::getType, BuildingType.toEnum(gender));

        // 查询符合条件的楼宇列表
        List<Building> buildingsDB = buildingService.list(queryWrapperBuilding);
        List<Long> buildingIdsDB = buildingsDB.stream().map(Building::getId).toList();

        // 创建房间查询条件，筛选出属于查询楼宇列表中的房间
        LambdaQueryWrapper<Room> queryWrapperRoom = new LambdaQueryWrapper<>();
        queryWrapperRoom.in(Room::getBuildingId, buildingIdsDB)
                .eq(Room::getStatus, RoomStatus.AVAILABLE);

        // 返回符合条件的房间列表
        return list(queryWrapperRoom);
    }
}
