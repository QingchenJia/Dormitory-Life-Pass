package dormitorylifepass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.common.R;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.enums.EmployeeStatus;
import dormitorylifepass.enums.EmployeeType;
import dormitorylifepass.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 根据页面号和页面大小以及员工姓名查询员工信息
     * 此方法使用GET请求处理分页查询，允许根据员工姓名进行模糊搜索
     *
     * @param page     页面号，表示请求的页面编号
     * @param pageSize 页面大小，表示每页显示的记录数
     * @param name     员工姓名，用于模糊匹配查询
     * @return 返回一个封装了员工信息页面对象的响应结果
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        // 创建Page对象，用于封装分页查询的参数
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        // 调用服务层方法执行分页查询
        employeeService.selectPage(pageInfo, name);

        // 返回封装了查询结果的响应对象
        return R.success(pageInfo);
    }

    /**
     * 根据员工ID查询员工信息
     *
     * @param id 员工ID，通过URL路径变量传递
     * @return 返回一个封装了员工信息的响应对象
     */
    @GetMapping("/{id}")
    public R<Employee> queryOne(@PathVariable Long id) {
        // 根据ID从数据库中获取员工信息
        Employee employeeDB = employeeService.getById(id);
        // 返回成功响应，包含查询到的员工信息
        return R.success(employeeDB);
    }

    /**
     * 更新员工信息的接口方法
     * 该方法通过HTTP PUT请求接收员工数据，并调用服务层方法更新数据库中的员工信息
     *
     * @param employee 员工对象，包含要更新的员工信息，通过请求体传递
     * @return 返回一个表示操作结果的响应对象，包含操作是否成功和提示信息
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
        // 记录更新员工的日志信息
        log.info("员工信息修改：{}", employee);
        // 调用服务层方法，根据员工对象中的信息更新数据库
        employeeService.updateById(employee);
        // 返回成功响应，表示员工信息修改成功
        return R.success("员工信息修改成功");
    }

    /**
     * 根据员工类型和状态获取员工列表
     * 此方法使用GET请求处理，从请求参数中获取员工类型和状态，
     * 并调用服务层方法查询符合条件的员工列表，最后将查询结果封装到响应对象中返回
     *
     * @param employeeType   员工类型，用于筛选查询结果
     * @param employeeStatus 员工状态，用于筛选查询结果
     * @return 返回一个封装了查询结果的响应对象
     */
    @GetMapping("/list")
    public R<List<Employee>> list(@RequestParam("type") Integer employeeType, @RequestParam("status") Integer employeeStatus) {
        // 调用服务层方法，根据员工类型和状态查询员工列表
        List<Employee> employeesDB = employeeService.selectList(EmployeeType.toEnum(employeeType), EmployeeStatus.toEnum(employeeStatus));
        // 将查询结果封装到响应对象中并返回
        return R.success(employeesDB);
    }
}
