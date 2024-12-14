package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.mapper.EmployeeMapper;
import dormitorylifepass.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
