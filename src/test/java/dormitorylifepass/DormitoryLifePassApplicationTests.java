package dormitorylifepass;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.dto.CheckInDto;
import dormitorylifepass.dto.RoomChangeDto;
import dormitorylifepass.dto.RoomDto;
import dormitorylifepass.dto.RoomRepairDto;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.entity.RoomChange;
import dormitorylifepass.entity.RoomRepair;
import dormitorylifepass.enums.EmployeeStatus;
import dormitorylifepass.enums.EmployeeType;
import dormitorylifepass.service.*;
import dormitorylifepass.utils.SHA256Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DormitoryLifePassApplicationTests {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomChangeService roomChangeService;
    @Autowired
    private RoomRepairService roomRepairService;
    @Autowired
    private CheckInService checkInService;

    @Test
    void contextLoads() {
    }

    @Test
    void testSelectInitEmployee() {
        List<Employee> list = employeeService.list();
        list.forEach(System.out::println);
    }

    @Test
    void testSHA256Encrypt() {
        String encrypt = SHA256Util.encrypt("123456");
        System.out.println(encrypt.length());
        System.out.println(encrypt);
    }

    @Test
    void testSelectEmployeeByType() {
        List<Employee> employees = employeeService.selectList(EmployeeType.DORMITORY_MANAGER, EmployeeStatus.ARRANGED);
        employees.forEach(System.out::println);
    }

    @Test
    void testSelectRoomByGender() {
        roomService.selectList(1);
    }

    @Test
    void testRoomUpdateStatus() {
        roomService.updateStatus();
    }

    @Test
    void testSearchInfo() {
        List<RoomDto> roomDtos = roomService.searchInfo(1868569239229210626L);
        roomDtos.forEach(System.out::println);
    }

    @Test
    void testSelectRoomChangeDtoPage() {
        Page<RoomChangeDto> roomChangeDtoPage = roomChangeService.selectPage(new Page<RoomChange>(1, 10));
        roomChangeDtoPage.getRecords().forEach(System.out::println);
    }

    @Test
    void testSelectRoomChangeListByUserId() {
        List<RoomChangeDto> roomChangeDtos = roomChangeService.selectList(1868988253831430144L, 4);
        System.out.println(roomChangeDtos);
    }

    @Test
    void testSelectRoomRepairDtoPage() {
        Page<RoomRepairDto> roomRepairDtoPage = roomRepairService.selectPage(new Page<RoomRepair>(1, 10));
        roomRepairDtoPage.getRecords().forEach(System.out::println);
    }

    @Test
    void testSelectRoomRepairList() {
        List<RoomRepairDto> roomRepairDtos = roomRepairService.selectList(1869243215647526914L, null);
        System.out.println(roomRepairDtos);
    }

    @Test
    void testSelectCheckInPage() {
        Page<CheckInDto> checkInDtoPage = checkInService.selectPage(new Page<>(1, 10));
        checkInDtoPage.getRecords().forEach(System.out::println);
    }

    @Test
    void testSelectCheckInList() {
        List<CheckInDto> checkInDtos = checkInService.selectList(1868988253831430144L);
        checkInDtos.forEach(System.out::println);
    }
}
