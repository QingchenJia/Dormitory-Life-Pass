package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.entity.Student;

public interface StudentService extends IService<Student> {
    Student Login(Student student);

    void insert(Student student);

    void selectPage(Page<Student> page, String name);
}
