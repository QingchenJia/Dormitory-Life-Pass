package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.common.CustomException;
import dormitorylifepass.dto.RoomChangeDto;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.entity.Room;
import dormitorylifepass.entity.RoomChange;
import dormitorylifepass.entity.Student;
import dormitorylifepass.enums.RoomChangeStatus;
import dormitorylifepass.enums.RoomStatus;
import dormitorylifepass.mapper.RoomChangeMapper;
import dormitorylifepass.service.EmployeeService;
import dormitorylifepass.service.RoomChangeService;
import dormitorylifepass.service.RoomService;
import dormitorylifepass.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        List<RoomChangeDto> roomChangeDtos = toDtos(roomChangesDB);

        // 将转换后的DTO列表设置到分页对象中
        roomChangeDtoPage.setRecords(roomChangeDtos);

        // 返回包含转换后数据的分页对象
        return roomChangeDtoPage;
    }

    /**
     * 将RoomChange对象列表转换为RoomChangeDto对象列表
     * 此方法主要用于将从数据库查询到的房间变更记录转换为包含学生和房间名称信息的DTO对象列表
     *
     * @param roomChangesDB 从数据库查询到的RoomChange对象列表
     * @return 转换后的RoomChangeDto对象列表
     */
    private List<RoomChangeDto> toDtos(List<RoomChange> roomChangesDB) {
        // 将查询到的房间变更记录转换为DTO对象，包含学生和房间的名称信息
        // 返回转换后的DTO对象列表
        return roomChangesDB.stream().map(roomChange -> {
            // 创建一个新的RoomChangeDto对象
            RoomChangeDto roomChangeDto = new RoomChangeDto();

            // 使用Spring框架的工具类复制RoomChange对象的属性到RoomChangeDto对象
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
    }

    /**
     * 更新房间变更请求的状态
     * 此方法使用事务处理，以确保数据一致性
     * 当状态更新为接受（ACCEPT）时，还会更新相关学生的房间分配
     *
     * @param ids    房间变更请求的ID列表
     * @param status 新的状态值
     */
    @Override
    @Transactional
    public void updateStatus(List<Long> ids, Integer status) {
        ids.forEach(id -> {
            // 创建一个RoomChange对象来更新状态
            RoomChange roomChange = new RoomChange();
            roomChange.setId(id);
            roomChange.setStatus(RoomChangeStatus.toEnum(status));

            // 更新房间变更请求的状态
            updateById(roomChange);

            // 如果状态更新为接受（ACCEPT），则更新相关学生的房间分配
            if (RoomChangeStatus.ACCEPT.getCode().equals(status)) {
                // 根据ID获取房间变更请求的详细信息
                RoomChange roomChangeDB = getById(id);
                Long newRoomId = roomChangeDB.getNewRoomId();

                // 检查新房间是否可用
                Room newRoomDB = roomService.getById(newRoomId);
                if (RoomStatus.UNAVAILABLE.equals(newRoomDB.getStatus())) {
                    throw new CustomException("该房间不可入住");
                }

                // 创建更新条件对象，用于更新学生的房间ID
                LambdaUpdateWrapper<Student> updateWrapperStudent = new LambdaUpdateWrapper<>();
                updateWrapperStudent.set(Student::getRoomId, roomChangeDB.getNewRoomId())
                        .eq(Student::getId, roomChangeDB.getStudentId());

                // 执行学生房间ID的更新
                studentService.update(updateWrapperStudent);

                // 更新房间状态
                roomService.updateStatus();
            }
        });
    }

    /**
     * 根据学生ID和申请状态查询宿舍变更记录
     *
     * @param id     学生ID，用于查询该学生的宿舍变更记录
     * @param status 申请状态，用于过滤特定状态的变更记录，如果为null，则不考虑状态
     * @return 返回一个包含宿舍变更信息的列表
     */
    @Override
    public List<RoomChangeDto> selectList(Long id, Integer status) {
        // 创建查询条件构建器
        LambdaQueryWrapper<RoomChange> queryWrapper = new LambdaQueryWrapper<>();

        // 根据提供的状态和学生ID构建查询条件
        queryWrapper.eq(status != null, RoomChange::getStatus, RoomChangeStatus.toEnum(status))
                .eq(RoomChange::getStudentId, id)
                .or()
                .eq(RoomChange::getOldEmployeeId, id)
                .or()
                .eq(RoomChange::getNewEmployeeId, id);

        // 执行查询，获取符合条件的宿舍变更记录列表
        List<RoomChange> roomChangesDB = list(queryWrapper);

        // 将查询到的实体对象列表转换为DTO列表，并返回
        return toDtos(roomChangesDB);
    }
}
