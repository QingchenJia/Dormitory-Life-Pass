package dormitorylifepass;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.dto.RoomChangeDto;
import dormitorylifepass.dto.RoomDto;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.entity.RoomChange;
import dormitorylifepass.enums.EmployeeStatus;
import dormitorylifepass.enums.EmployeeType;
import dormitorylifepass.service.EmployeeService;
import dormitorylifepass.service.RoomChangeService;
import dormitorylifepass.service.RoomService;
import dormitorylifepass.service.impl.RoomServiceImpl;
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
    private RoomServiceImpl roomServiceImpl;
    @Autowired
    private RoomChangeService roomChangeService;

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
        roomServiceImpl.updateStatus();
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
}
