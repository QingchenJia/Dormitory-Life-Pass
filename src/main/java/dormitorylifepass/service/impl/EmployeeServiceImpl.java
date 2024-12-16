package dormitorylifepass.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.common.CustomException;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.mapper.EmployeeMapper;
import dormitorylifepass.service.EmployeeService;
import dormitorylifepass.utils.SHA256Util;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    /**
     * 重写登录方法，用于验证员工的登录信息
     *
     * @param employee 包含用户名和密码的员工对象，用于登录验证
     * @return 如果登录成功，返回对应的员工对象；如果登录失败，返回null
     */
    @Override
    public Employee login(Employee employee) {
        // 创建一个Lambda查询包装器，用于构建查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件：用户名和密码必须匹配
        // 注意：密码需要使用SHA256加密后进行比较，以确保安全性
        queryWrapper.eq(StringUtil.notNullNorEmpty(employee.getUsername()), Employee::getUsername, employee.getUsername())
                .eq(StringUtil.notNullNorEmpty(employee.getJobNum()), Employee::getJobNum, employee.getJobNum())
                .eq(Employee::getPassword, SHA256Util.encrypt(employee.getPassword()));

        // 执行查询，返回满足条件的员工对象
        // 如果没有满足条件的记录，getOne方法将返回null
        return getOne(queryWrapper);
    }

    /**
     * 插入新员工记录到数据库
     * <p>
     * 在员工实体被插入数据库之前，此方法会设置其密码为加密后的默认值
     * 默认密码为 "123456"，使用 SHA-256 算法进行加密以保证安全性
     *
     * @param employee 待插入的员工实体，不包含密码
     */
    @Override
    public void insert(Employee employee) {
        // 检查新员工的工号或用户名是否已存在于数据库中
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getJobNum, employee.getJobNum())
                .or()
                .eq(Employee::getUsername, employee.getUsername());

        // 如果存在相同的工号或用户名，则抛出异常
        if (count(queryWrapper) > 0)
            throw new CustomException("员工工号或账号已存在");

        // 加密默认密码并设置到员工实体
        employee.setPassword(SHA256Util.encrypt("123456"));
        // 保存员工实体到数据库
        save(employee);
    }
}
