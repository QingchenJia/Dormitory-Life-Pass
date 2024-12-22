package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.Building;
import dormitorylifepass.entity.Room;
import dormitorylifepass.entity.RoomRepair;
import dormitorylifepass.entity.Student;
import dormitorylifepass.enums.RoomRepairStatus;
import dormitorylifepass.mapper.RoomRepairMapper;
import dormitorylifepass.service.BuildingService;
import dormitorylifepass.service.RoomRepairService;
import dormitorylifepass.service.RoomService;
import dormitorylifepass.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomRepairServiceImpl extends ServiceImpl<RoomRepairMapper, RoomRepair> implements RoomRepairService {
    @Autowired
    private StudentService studentService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private BuildingService buildingService;

    /**
     * 插入房间维修记录
     * 如果房间维修对象中未提供房间ID，则通过学生ID获取关联的房间ID
     * 同时，根据房间ID获取对应的楼宇信息，以便分配给指定的维修人员
     * 最后，设置维修记录的状态并保存
     *
     * @param roomRepair 房间维修对象，包含维修的相关信息
     */
    @Override
    public void insertRoomRepair(RoomRepair roomRepair) {
        // 检查房间维修对象中是否提供了房间ID，如果没有，则通过学生ID获取房间ID
        if (roomRepair.getRoomId() == null) {
            Student studentDB = studentService.getById(roomRepair.getStudentId());
            roomRepair.setRoomId(studentDB.getRoomId());
        }

        // 根据房间ID获取房间信息，以便进一步获取楼宇信息
        Room roomDB = roomService.getById(roomRepair.getRoomId());
        // 根据房间所属的楼宇ID获取楼宇信息，用于获取维修人员ID
        Building buildingDB = buildingService.getById(roomDB.getBuildingId());
        // 设置房间维修记录中的维修人员ID
        roomRepair.setEmployeeDmId(buildingDB.getEmployeeId());

        // 设置房间维修记录的初始状态为已请求
        roomRepair.setStatus(RoomRepairStatus.REQUESTED);

        // 保存房间维修记录
        save(roomRepair);
    }
}
