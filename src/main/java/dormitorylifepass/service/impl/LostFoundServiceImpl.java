package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.LostFound;
import dormitorylifepass.entity.Student;
import dormitorylifepass.enums.LostFoundStatus;
import dormitorylifepass.mapper.LostFoundMapper;
import dormitorylifepass.service.LostFoundService;
import dormitorylifepass.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LostFoundServiceImpl extends ServiceImpl<LostFoundMapper, LostFound> implements LostFoundService {
    @Autowired
    private StudentService studentService;

    /**
     * 插入失物招领信息
     * <p>
     * 此方法用于将失物招领对象的状态设置为已上传，并保存到数据库中
     * 选择@Override注解是为了表明该方法重写了父类或接口的方法
     *
     * @param lostFound 失物招领对象，包含失物招领的相关信息
     */
    @Override
    public void insertLostFound(LostFound lostFound) {
        // 检查失物招领信息中是否包含了电话号码
        if (lostFound.getPhone() == null) {
            // 如果没有电话号码，通过学生ID获取学生信息，并设置失物招领信息中的电话号码为学生的电话号码
            Student studentDB = studentService.getById(lostFound.getStudentId());
            lostFound.setPhone(studentDB.getPhone());
        }

        // 设置失物招领信息的状态为已上传
        lostFound.setStatus(LostFoundStatus.UPLOADED);
        // 保存失物招领信息到数据库
        save(lostFound);
    }

    /**
     * 根据学生ID选择丢失和发现物品的列表
     *
     * @param studentId 学生的唯一标识符
     * @return 包含LostFound对象的列表，这些对象与给定的学生ID相关联
     */
    @Override
    public List<LostFound> selectList(Long studentId) {
        // 创建一个Lambda查询包装器，用于构建查询条件
        LambdaQueryWrapper<LostFound> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件，筛选出学生ID等于给定值的记录
        queryWrapper.eq(studentId != null, LostFound::getStudentId, studentId);

        // 执行查询并返回结果列表
        return list(queryWrapper);
    }
}
