package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    Employee login(Employee employee);

    void insert(Employee employee);
}
