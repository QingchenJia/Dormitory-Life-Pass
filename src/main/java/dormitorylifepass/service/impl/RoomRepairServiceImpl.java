package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.dto.RoomRepairDto;
import dormitorylifepass.entity.*;
import dormitorylifepass.enums.EmployeeStatus;
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
        List<RoomRepairDto> roomRepairDtos = getDtos(roomRepairsDB);

        // 将转换后的记录列表设置到分页对象中
        roomRepairDtoPage.setRecords(roomRepairDtos);

        // 返回转换后的分页对象
        return roomRepairDtoPage;
    }

    /**
     * 将RoomRepair对象列表转换为RoomRepairDto对象列表
     * 此方法主要用于数据传输，将从数据库中获取的RoomRepair对象转换为DTO对象，以便在不同层之间传输
     * 它还负责从相关实体（如房间、学生、员工）中获取和设置名称信息
     *
     * @param roomRepairsDB 从数据库中获取的RoomRepair对象列表
     * @return 转换后的RoomRepairDto对象列表
     */
    private List<RoomRepairDto> getDtos(List<RoomRepair> roomRepairsDB) {
        return roomRepairsDB.stream().map(roomRepair -> {
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
    }

    /**
     * 根据学生ID或员工ID以及状态选择房间维修记录列表
     *
     * @param id     学生或员工的ID，用于查询维修记录
     * @param status 维修记录的状态，可选参数，用于过滤查询结果
     * @return 返回一个包含查询到的房间维修记录DTO的列表
     */
    @Override
    public List<RoomRepairDto> selectList(Long id, Integer status) {
        // 创建Lambda查询包装器，用于构建查询条件
        LambdaQueryWrapper<RoomRepair> queryWrapper = new LambdaQueryWrapper<>();

        // 设置查询条件：如果状态不为空，则根据提供的状态查询维修记录；
        // 同时根据学生ID查询维修记录，或者根据员工ID（包括DM和MW角色）查询维修记录
        queryWrapper.eq(status != null, RoomRepair::getStatus, RoomRepairStatus.toEnum(status))
                .eq(RoomRepair::getStudentId, id)
                .or()
                .eq(RoomRepair::getEmployeeDmId, id)
                .or()
                .eq(RoomRepair::getEmployeeMwId, id);

        // 执行查询，获取符合条件的房间维修记录列表
        List<RoomRepair> roomRepairsDB = list(queryWrapper);

        // 将查询到的房间维修记录转换为DTO格式并返回
        return getDtos(roomRepairsDB);
    }

    /**
     * 安排房间维修任务
     * <p>
     * 此方法用于将房间维修任务标记为已安排状态，并更新数据库中的相应记录
     * 它首先设置房间维修任务的状态为ARRANGED，然后通过updateById方法更新数据库
     *
     * @param roomRepair 房间维修任务对象，包含需要更新的信息
     */
    @Override
    public void arrange(RoomRepair roomRepair) {
        // 根据房间维修任务关联的员工ID获取员工对象
        Employee employeeDB = employeeService.getById(roomRepair.getEmployeeMwId());
        // 设置员工的状态为已安排，表示该员工已经被安排了维修任务
        employeeDB.setStatus(EmployeeStatus.ARRANGED);
        // 更新数据库中的员工信息
        employeeService.updateById(employeeDB);

        // 设置房间维修任务的状态为已安排
        roomRepair.setStatus(RoomRepairStatus.ARRANGED);
        // 更新数据库中的房间维修任务信息
        updateById(roomRepair);
    }

    /**
     * 完结房间维修工单
     * 当房间维修工作完成后，调用此方法来更新工单状态为完结
     *
     * @param roomRepair 房间维修对象，包含维修工单的详细信息
     */
    @Override
    public void finish(RoomRepair roomRepair) {
        // 设置维修工单的状态为完结
        roomRepair.setStatus(RoomRepairStatus.FINISHED);
        // 根据ID更新维修工单的信息
        updateById(roomRepair);

        // 从数据库中获取更新后的房间维修对象
        RoomRepair roomRepairDB = getById(roomRepair.getId());
        // 创建员工对象，用于更新维修工的工单状态
        Employee employee = new Employee();
        // 设置员工ID为房间维修对象中的维修员工ID
        employee.setId(roomRepairDB.getEmployeeMwId());
        // 设置员工状态为未安排，表示该员工已完成当前维修任务，可接受新的任务
        employee.setStatus(EmployeeStatus.NOT_ARRANGED);
        // 调用员工服务的更新方法，更新员工的状态
        employeeService.updateById(employee);
    }
}
