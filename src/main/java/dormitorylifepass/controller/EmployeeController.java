package dormitorylifepass.controller;

import dormitorylifepass.common.R;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 处理员工登录请求
     * <p>
     * 该方法接收登录请求，验证员工身份，并在登录成功后创建会话
     *
     * @param request  HTTP请求对象，用于创建会话
     * @param employee 包含员工登录信息的对象
     * @return 返回登录结果，包括登录失败的错误信息或登录成功的员工信息
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 记录员工登录信息
        log.info("员工登录：{}", employee);

        // 调用服务层方法进行登录验证
        Employee employeeDB = employeeService.login(employee);
        // 如果登录失败，返回错误信息
        if (employeeDB == null)
            return R.error("登录失败");

        // 登录成功，创建会话并存储员工信息
        request.getSession().setAttribute("currentUser", employeeDB.getId());
        // 返回登录成功的员工信息
        return R.success(employeeDB);
    }

    /**
     * 处理用户退出登录请求
     * <p>
     * 该方法通过移除用户会话中的身份信息来实现退出登录的功能
     * 使用HTTP POST方法发送到/logout端点来调用此方法
     *
     * @param request 包含用户请求的所有数据，用于访问会话信息
     * @return 返回一个表示退出成功消息的响应对象
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 移除用户会话中的身份信息，以实现退出登录
        request.getSession().removeAttribute("currentUser");
        // 返回表示退出成功的消息
        return R.success("退出成功");
    }

    /**
     * 处理员工信息的保存请求
     * 该方法接收一个Employee对象作为参数，通过employeeService进行数据库插入操作
     * 主要用于新增员工信息
     *
     * @param employee 包含员工信息的Employee对象，通过请求体传递
     * @return 返回一个R<String>对象，表示新增员工操作的结果
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee) {
        // 记录新增员工的日志信息
        log.info("新增员工，员工信息：{}", employee);

        // 调用服务层方法插入员工信息
        employeeService.insert(employee);
        // 返回新增员工成功的响应
        return R.success("新增员工成功");
    }
}
