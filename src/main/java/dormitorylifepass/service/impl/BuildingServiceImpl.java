package dormitorylifepass.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.Building;
import dormitorylifepass.entity.Employee;
import dormitorylifepass.enums.EmployeeStatus;
import dormitorylifepass.mapper.BuildingMapper;
import dormitorylifepass.service.BuildingService;
import dormitorylifepass.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {
    @Autowired
    private EmployeeService employeeService;

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
                .orderByAsc(Building::getName);

        // 执行分页查询
        page(page, queryWrapper);
    }

    /**
     * 更新建筑信息，并根据情况更新关联的员工状态
     * 当建筑信息更新时，如果指定了员工ID，则将该员工的状态更新为已安排
     *
     * @param building 要更新的建筑对象，包含新的建筑信息和可能关联的员工ID
     */
    @Override
    @Transactional
    public void updateBuilding(Building building) {
        // 更新建筑信息
        updateById(building);

        // 如果建筑关联了员工ID，则更新该员工的状态为已安排
        if (building.getEmployeeId() != null) {
            // 创建更新条件构造器
            LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<>();
            // 设置更新内容：员工状态为已安排，并且员工ID等于建筑关联的员工ID
            updateWrapper.set(Employee::getStatus, EmployeeStatus.ARRANGED)
                    .eq(Employee::getId, building.getEmployeeId());

            // 执行员工信息更新操作
            employeeService.update(updateWrapper);
        }
    }
}
