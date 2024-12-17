package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.enums.EmployeeStatus;
import dormitorylifepass.enums.EmployeeType;

import java.util.List;

public interface EmployeeService extends IService<Employee> {
    Employee login(Employee employee);

    void insert(Employee employee);

    void selectPage(Page<Employee> page, String name);

    List<Employee> selectList(EmployeeType employeeType, EmployeeStatus employeeStatus);
}
