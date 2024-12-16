package dormitorylifepass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.common.R;
import dormitorylifepass.entity.Building;
import dormitorylifepass.service.BuildingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/building")
@Slf4j
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    /**
     * 处理新建建筑物的请求
     * 该方法通过POST请求接收Building对象的JSON数据，然后调用服务层方法保存数据
     *
     * @param building 包含建筑物信息的Building对象，由请求体中获取
     * @return 返回一个R类型的对象，表示操作结果
     * 如果保存成功，返回成功信息
     */
    @PostMapping
    public R<String> save(@RequestBody Building building) {
        buildingService.save(building);
        return R.success("新增成功");
    }

    /**
     * 根据页码和页面大小以及名称查询建筑信息
     *
     * @param page     页码，用于指定从哪一页开始查询
     * @param pageSize 页面大小，用于指定每页显示的记录数
     * @param name     建筑名称，用于模糊查询
     * @return 返回一个包含建筑信息页面对象的响应
     * <p>
     * 此方法使用@GetMapping注解，映射到/page请求路径，支持HTTP GET请求
     * 它创建一个分页对象并调用服务方法进行查询，然后返回查询结果
     */
    @GetMapping("/page")
    public R<Page<Building>> page(int page, int pageSize, String name) {
        // 创建一个分页对象，传入当前页码和页面大小
        Page<Building> pageInfo = new Page<>(page, pageSize);
        // 调用建筑服务的分页查询方法，传入分页对象和查询名称
        buildingService.selectPage(pageInfo, name);
        // 返回一个包含分页对象的成功响应
        return R.success(pageInfo);
    }
}
