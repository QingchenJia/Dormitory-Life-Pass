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
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 解决失物招领问题
     * 根据提供的失物招领ID列表，将这些失物招领的状态更新为已解决
     * 此方法主要用于批量更新失物招领的状态，确保它们被标记为已解决
     *
     * @param ids 失物招领的ID列表，这些ID标识了需要被更新状态的失物招领项
     */
    @Override
    @Transactional
    public void solve(List<Long> ids) {
        // 根据ID列表创建一个新的失物招领列表，每个失物招领的状态都被设置为已解决
        List<LostFound> lostFounds = ids.stream().map(id -> {
            LostFound lostFound = new LostFound();
            lostFound.setId(id);
            lostFound.setStatus(LostFoundStatus.SOLVED);
            return lostFound;
        }).toList();

        // 批量更新失物招领列表，以反映它们的状态变化
        updateBatchById(lostFounds);
    }
}
