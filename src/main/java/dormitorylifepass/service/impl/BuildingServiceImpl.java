package dormitorylifepass.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.Building;
import dormitorylifepass.mapper.BuildingMapper;
import dormitorylifepass.service.BuildingService;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {
    /**
     * 根据名称模糊查询建筑物，并按建筑物编号升序排序
     *
     * @param page 分页对象，用于封装分页查询所需的信息
     * @param name 建筑物名称的关键词，用于模糊查询
     */
    @Override
    public void selectPage(Page<Building> page, String name) {
        // 创建Lambda查询条件包装器
        LambdaQueryWrapper<Building> queryWrapper = new LambdaQueryWrapper<>();

        // 如果名称关键词不为空也不为空字符串，则添加模糊查询条件
        queryWrapper.like(StringUtil.notNullNorEmpty(name), Building::getName, name)
                // 添加按建筑物编号升序排序的条件
                .orderByAsc(Building::getNumber);

        // 执行分页查询
        page(page, queryWrapper);
    }
}
