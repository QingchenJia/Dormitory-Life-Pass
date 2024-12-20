package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.dto.RoomChangeDto;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.entity.Room;
import dormitorylifepass.entity.RoomChange;
import dormitorylifepass.entity.Student;
import dormitorylifepass.enums.RoomChangeStatus;
import dormitorylifepass.mapper.RoomChangeMapper;
import dormitorylifepass.service.EmployeeService;
import dormitorylifepass.service.RoomChangeService;
import dormitorylifepass.service.RoomService;
import dormitorylifepass.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomChangeServiceImpl extends ServiceImpl<RoomChangeMapper, RoomChange> implements RoomChangeService {
    @Autowired
    private StudentService studentService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private EmployeeService employeeService;


    /**
     * 插入房间变更记录
     * <p>
     * 该方法用于处理房间变更的请求它将房间变更的状态设置为"已请求"，并保存该变更记录
     *
     * @param roomChange 房间变更对象，包含房间变更的详细信息
     */
    @Override
    public void insertRoomChange(RoomChange roomChange) {
        // 设置房间变更的状态为已请求
        roomChange.setStatus(RoomChangeStatus.REQUESTED);
        // 保存房间变更记录
        save(roomChange);
    }

    /**
     * 重写selectPage方法，用于查询并转换房间变更记录的分页数据
     * 此方法首先根据分页参数查询数据库中的房间变更记录，然后将每条记录转换成包含学生和房间名称信息的DTO，
     * 并将这些DTO设置到一个新的分页对象中返回
     *
     * @param page 分页参数，包含当前页码、每页大小等信息
     * @return 返回一个分页对象，其中包含转换后的房间变更记录DTO
     */
    @Override
    public Page<RoomChangeDto> selectPage(Page<RoomChange> page) {
        // 执行分页查询，获取分页后的房间变更记录
        page(page);

        // 从分页对象中获取房间变更记录列表
        List<RoomChange> roomChangesDB = page.getRecords();

        // 创建一个新的分页对象用于存储转换后的DTO
        Page<RoomChangeDto> roomChangeDtoPage = new Page<>();
        // 复制分页信息到新的分页对象，但不包括实际的记录数据
        BeanUtils.copyProperties(page, roomChangeDtoPage, "records");

        // 将查询到的房间变更记录转换为DTO对象，包含学生和房间的名称信息
        List<RoomChangeDto> roomChangeDtos = roomChangesDB.stream().map(roomChange -> {
            RoomChangeDto roomChangeDto = new RoomChangeDto();

            BeanUtils.copyProperties(roomChange, roomChangeDto);

            // 根据学生ID查询学生信息
            Student student = studentService.getById(roomChange.getStudentId());
            // 根据房间ID查询旧房间和新房间信息
            Room oldRoom = roomService.getById(roomChange.getOldRoomId());
            Room newRoom = roomService.getById(roomChange.getNewRoomId());
            // 根据员工ID查询旧员工和新员工信息
            Employee oldEmployee = employeeService.getById(roomChange.getOldEmployeeId());
            Employee newEmployee = employeeService.getById(roomChange.getNewEmployeeId());

            // 设置DTO对象的属性值
            roomChangeDto.setStudentName(student.getName());
            roomChangeDto.setOldRoomName(oldRoom.getName());
            roomChangeDto.setNewRoomName(newRoom.getName());
            roomChangeDto.setOldEmployeeName(oldEmployee.getName());
            roomChangeDto.setNewEmployeeName(newEmployee.getName());

            // 返回转换后的DTO对象
            return roomChangeDto;
        }).toList();

        // 将转换后的DTO列表设置到分页对象中
        roomChangeDtoPage.setRecords(roomChangeDtos);

        // 返回包含转换后数据的分页对象
        return roomChangeDtoPage;
    }
}
