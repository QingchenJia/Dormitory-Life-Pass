package dormitorylifepass;

import dormitorylifepass.entity.Employee;
import dormitorylifepass.service.EmployeeService;
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
}
