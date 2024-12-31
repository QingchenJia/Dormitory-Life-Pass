package dormitorylifepass.service.impl;

import ch.qos.logback.core.util.StringUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.Student;
import dormitorylifepass.mapper.StudentMapper;
import dormitorylifepass.service.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    /**
     * 重写Login方法，用于学生登录验证
     * <p>
     * 该方法根据学生号、用户名和密码进行登录验证只有当学生号或用户名及密码匹配时，才返回对应的学生对象
     * 为了安全考虑，密码在进行比对前会使用SHA-256加密
     *
     * @param student 包含登录信息的学生对象，包括学生号、用户名和密码
     * @return 如果登录成功，返回对应的学生对象；否则返回null
     */
    @Override
    public Student Login(Student student) {
        // 创建Lambda查询包装器，用于构建查询条件
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();

        // 当学生号不为空也不为空字符串时，按学生号进行查询
        // 这里首先判断学生对象中的学生号是否非空且不为空字符串，以防止无效查询
        // 通过Lambda表达式指定查询条件为学生号等于给定的学生号
        queryWrapper.eq(StringUtil.notNullNorEmpty(student.getStudentNum()), Student::getStudentNum, student.getStudentNum())
                // 当用户名不为空也不为空字符串时，按用户名进行查询
                // 同样的，这里也是先进行非空和空字符串的判断，以提高查询效率
                // 通过Lambda表达式指定查询条件为用户名等于给定的用户名
                .eq(StringUtil.notNullNorEmpty(student.getUsername()), Student::getUsername, student.getUsername())
                // 按密码进行查询，这里直接使用SHA-256加密给定的密码，然后进行比较
                // 由于密码在存储时应该是加密的，因此在登录时也需要对输入的密码进行同样的加密处理
                .eq(Student::getPassword, DigestUtil.sha256Hex(student.getPassword()));

        // 使用上述查询条件获取匹配的学生对象
        // 如果有唯一匹配的学生对象，则返回该对象；否则返回null
        return getOne(queryWrapper);
    }

    /**
     * 插入学生信息
     * <p>
     * 此方法首先检查数据库中是否已存在相同的学号或用户名如果存在，抛出运行时异常，提示学号或账号已存在
     * 如果不存在，将学生的密码加密后保存到数据库中
     *
     * @param student 要插入的学生对象，包含学生的基本信息
     * @throws RuntimeException 如果学号或用户名已存在，则抛出此异常
     */
    @Override
    public void insert(Student student) {
        // 创建Lambda查询包装器，用于后续查询操作
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        // 检查学号或用户名是否已存在
        queryWrapper.eq(Student::getStudentNum, student.getStudentNum())
                .or()
                .eq(Student::getUsername, student.getUsername());

        // 如果已存在相同的学号或用户名，抛出异常
        if (count(queryWrapper) > 0)
            throw new RuntimeException("学号或账号已存在");

        // 为学生设置默认密码并加密
        student.setPassword(DigestUtil.sha256Hex("123456"));
        // 保存学生信息到数据库
        save(student);
    }

    /**
     * 根据学生姓名模糊查询并按学生编号升序排序分页
     *
     * @param page 分页对象，用于封装分页查询所需的页码、页大小等信息
     * @param name 学生姓名的关键词，用于模糊查询
     */
    @Override
    public void selectPage(Page<Student> page, String name) {
        // 创建Lambda查询条件包装器，用于构建查询条件和排序规则
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();

        // 判断姓名关键词是否非空，如果非空则添加模糊查询条件，并按学生编号升序排序
        queryWrapper.like(StringUtil.notNullNorEmpty(name), Student::getName, name)
                .orderByAsc(Student::getStudentNum);

        // 执行分页查询
        page(page, queryWrapper);
    }
}
