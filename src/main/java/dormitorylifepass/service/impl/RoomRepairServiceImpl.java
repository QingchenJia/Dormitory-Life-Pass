package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.dto.RoomRepairDto;
import dormitorylifepass.entity.*;
import dormitorylifepass.enums.RoomRepairStatus;
import dormitorylifepass.mapper.RoomRepairMapper;
import dormitorylifepass.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomRepairServiceImpl extends ServiceImpl<RoomRepairMapper, RoomRepair> implements RoomRepairService {
    @Autowired
    private StudentService studentService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private EmployeeService employeeService;

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

    /**
     * 选择分页方法，用于查询房间维修信息
     * 该方法扩展了分页查询，将查询结果转换为包含额外信息的DTO列表
     *
     * @param page 分页对象，包含分页参数和查询结果
     * @return 返回一个转换后的分页对象，包含房间维修的DTO列表
     */
    @Override
    public Page<RoomRepairDto> selectPage(Page<RoomRepair> page) {
        // 执行分页查询
        page(page);

        // 创建一个新的分页对象，用于存储转换后的结果
        Page<RoomRepairDto> roomRepairDtoPage = new Page<>();
        // 复制分页属性，但不包括记录本身
        BeanUtils.copyProperties(page, roomRepairDtoPage, "records");

        // 获取原始的房间维修记录列表
        List<RoomRepair> roomRepairsDB = page.getRecords();
        // 将每个房间维修记录转换为DTO，并设置额外的属性
        List<RoomRepairDto> roomRepairDtos = roomRepairsDB.stream().map(roomRepair -> {
            // 创建一个新的DTO对象
            RoomRepairDto roomRepairDto = new RoomRepairDto();
            // 复制基本属性
            BeanUtils.copyProperties(roomRepair, roomRepairDto);

            // 根据房间ID查询房间信息，并设置房间名称
            Room roomDB = roomService.getById(roomRepair.getRoomId());
            roomRepairDto.setRoomName(roomDB.getName());

            // 根据学生ID查询学生信息，并设置学生姓名
            Student studentDB = studentService.getById(roomRepair.getStudentId());
            roomRepairDto.setStudentName(studentDB.getName());

            // 根据维修人员ID查询员工信息，并设置维修人员姓名
            Employee employeeDmDB = employeeService.getById(roomRepair.getEmployeeDmId());
            roomRepairDto.setEmployeeDmName(employeeDmDB.getName());

            // 如果有指定的维护人员ID，则查询并设置维护人员姓名
            if (roomRepair.getEmployeeMwId() != null) {
                Employee employeeMwDB = employeeService.getById(roomRepair.getEmployeeMwId());
                roomRepairDto.setEmployeeMwName(employeeMwDB.getName());
            }

            // 返回转换后的DTO对象
            return roomRepairDto;
        }).toList();

        // 将转换后的记录列表设置到分页对象中
        roomRepairDtoPage.setRecords(roomRepairDtos);

        // 返回转换后的分页对象
        return roomRepairDtoPage;
    }
}
