package dormitorylifepass;

import dormitorylifepass.entity.Employee;
import dormitorylifepass.service.EmployeeService;
import dormitorylifepass.utils.SHA256Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DormitoryLifePassApplicationTests {
    @Autowired
    private EmployeeService employeeService;

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
}
