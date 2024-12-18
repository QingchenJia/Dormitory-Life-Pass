package dormitorylifepass.controller;

import dormitorylifepass.common.R;
import dormitorylifepass.entity.Student;
import dormitorylifepass.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 处理学生登录请求
     * <p>
     * 该方法接收一个HttpServletRequest对象和一个Student对象作为参数，其中Student对象通过请求体接收
     * 它首先记录学生登录信息，然后调用学生服务的Login方法进行登录验证
     * 如果登录成功，它将在当前会话中设置用户ID，并返回一个成功的响应，包含学生信息
     * 如果登录失败，它将返回一个错误的响应，包含失败消息
     *
     * @param request HttpServletRequest对象，用于获取当前请求的会话
     * @param student Student对象，包含学生登录所需的用户名和密码
     * @return R<Student> 返回一个封装了学生信息的响应对象，根据登录结果可能是成功或错误的响应
     */
    @PostMapping("/login")
    public R<Student> login(HttpServletRequest request, @RequestBody Student student) {
        // 记录学生登录信息
        log.info("学生登录：{}", student);

        // 调用学生服务的Login方法进行登录验证
        Student studentDB = studentService.Login(student);

        // 如果登录失败，返回错误的响应
        if (studentDB == null)
            return R.error("登陆失败");

        // 在当前会话中设置用户ID
        request.getSession().setAttribute("currentUser", studentDB.getId());

        // 返回成功的响应，包含学生信息
        return R.success(studentDB);
    }

    /**
     * 处理用户退出登录的请求
     *
     * @param request HttpServletRequest对象，用于访问当前请求的会话
     * @return 返回一个表示退出结果的消息响应对象
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 移除用户会话中的身份信息，以实现退出登录
        request.getSession().removeAttribute("currentUser");
        // 返回表示退出成功的消息
        return R.success("退出成功");
    }

    /**
     * 处理POST请求以保存学生信息
     * 该方法接收一个Student对象作为请求体，并将其保存到数据库中
     *
     * @param student 包含学生信息的Student对象，由请求体自动转换而来
     * @return 返回一个R<String>对象，表示保存操作的结果
     */
    @PostMapping
    public R<String> save(@RequestBody Student student) {
        studentService.insert(student);
        return R.success("添加成功");
    }
}
