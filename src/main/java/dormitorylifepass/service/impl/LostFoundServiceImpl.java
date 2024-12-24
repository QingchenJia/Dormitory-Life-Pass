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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class LostFoundServiceImpl extends ServiceImpl<LostFoundMapper, LostFound> implements LostFoundService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
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
        // 构造Redis中的键值
        String key = "lostFound:studentId:" + studentId;
        // 检查Redis中是否存在该键
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            // 如果存在，则直接从Redis中获取并返回列表
            return (List<LostFound>) redisTemplate.opsForValue().get(key);
        }

        // 创建一个Lambda查询包装器，用于构建查询条件
        LambdaQueryWrapper<LostFound> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件，筛选出学生ID等于给定值的记录
        queryWrapper.eq(studentId != null, LostFound::getStudentId, studentId);

        // 执行查询并返回结果列表
        List<LostFound> lostFoundsDB = list(queryWrapper);

        // 将查询结果缓存到Redis中，设置过期时间为15分钟
        redisTemplate.opsForValue().set(key, lostFoundsDB, 15, TimeUnit.MINUTES);

        // 返回查询结果
        return lostFoundsDB;
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

        Set<String> keys = redisTemplate.keys("lostFound:*");
        redisTemplate.delete(Objects.requireNonNull(keys));
    }

    /**
     * 根据ID删除失物招领信息
     * 此方法首先从数据库中获取失物招领信息，然后从数据库中删除该信息，
     * 并接着删除Redis缓存中与该失物招领信息相关的条目
     *
     * @param id 失物招领信息的ID，用于标识数据库和缓存中的具体条目
     */
    @Override
    public void deleteById(Long id) {
        // 从数据库中获取失物招领信息
        LostFound lostFoundDB = getById(id);
        // 获取失物招领信息关联的学生ID
        Long studentId = lostFoundDB.getStudentId();
        // 从数据库中删除失物招领信息
        removeById(id);
        // 从Redis缓存中删除与该失物招领信息相关的学生ID条目
        redisTemplate.delete("lostFound:studentId:" + studentId);
        // 从Redis缓存中删除与该失物招领信息相关的学生ID为空的条目
        redisTemplate.delete("lostFound:studentId:null");
    }
}
