package dormitorylifepass;

import dormitorylifepass.entity.Employee;
import dormitorylifepass.enums.EmployeeStatus;
import dormitorylifepass.enums.EmployeeType;
import dormitorylifepass.service.EmployeeService;
import dormitorylifepass.service.RoomService;
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
}
