package dormitorylifepass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.common.R;
import dormitorylifepass.dto.RoomRepairDto;
import dormitorylifepass.entity.RoomRepair;
import dormitorylifepass.service.RoomRepairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomRepair")
@Slf4j
public class RoomRepairController {
    @Autowired
    private RoomRepairService roomRepairService;

    /**
     * 处理房间维修申请的接口方法
     * 该方法通过POST请求接收房间维修信息，并调用服务层方法进行维修申请的插入操作
     *
     * @param roomRepair 通过请求体接收的房间维修对象，包含维修申请的相关信息
     * @return 返回一个自定义的响应对象，表示维修申请的结果
     */
    @PostMapping
    public R<String> application(@RequestBody RoomRepair roomRepair) {
        // 调用服务层方法，插入房间维修申请信息
        roomRepairService.insertRoomRepair(roomRepair);
        // 返回成功响应，表示维修申请已成功提交
        return R.success("申请成功");
    }

    /**
     * 处理房间维修信息的分页请求
     *
     * @param page     请求的页码，用于指定从哪一页开始获取数据
     * @param pageSize 每页的大小，即每页包含的记录数
     * @return 返回一个包含分页信息的响应对象，其中包含房间维修的列表和分页细节
     */
    @GetMapping("/page")
    public R<Page<RoomRepairDto>> page(int page, int pageSize) {
        // 创建一个Page对象，用于封装分页查询的参数
        Page<RoomRepair> pageInfo = new Page<>(page, pageSize);
        // 调用服务层的分页方法，执行分页查询
        Page<RoomRepairDto> pageResult = roomRepairService.selectPage(pageInfo);
        // 此处应返回分页查询的结果，但目前方法体尚未完成实现
        return R.success(pageResult);
    }

    /**
     * 获取维修列表接口
     * 根据房间ID和维修状态筛选维修记录
     *
     * @param id     房间ID，用于筛选特定房间的维修记录
     * @param status 维修状态，用于筛选特定状态的维修记录
     * @return 返回一个封装了维修信息列表的结果对象
     */
    @GetMapping("/list")
    public R<List<RoomRepairDto>> list(Long id, Integer status) {
        // 调用服务层方法，根据房间ID和维修状态获取维修记录列表
        List<RoomRepairDto> roomRepairDtos = roomRepairService.selectList(id, status);
        // 返回封装了维修记录列表的成功结果对象
        return R.success(roomRepairDtos);
    }

    /**
     * 处理房间维修安排请求
     * <p>
     * 该方法接收一个RoomRepair对象作为请求体，用于安排房间维修任务
     * 使用@PostMapping注解表明该方法处理POST请求，路径为"/arrange"
     *
     * @param roomRepair 包含房间维修信息的对象，用于安排维修任务
     * @return 返回一个R<String>对象，表示维修安排的结果
     */
    @PostMapping("/arrange")
    public R<String> arrange(@RequestBody RoomRepair roomRepair) {
        // 调用roomRepairService的arrange方法来安排房间维修
        roomRepairService.arrange(roomRepair);
        // 返回成功结果，表示维修安排成功
        return R.success("安排成功");
    }

    /**
     * 处理维修任务完成的请求
     * <p>
     * 该方法通过接收一个RoomRepair对象，标记相应的维修任务为完成状态
     * 主要用于维修管理系统中，维修人员或管理员标记维修任务结束
     *
     * @param roomRepair 包含维修任务信息的RoomRepair对象，用于标识和更新维修任务
     * @return 返回一个表示操作成功的响应对象，包含成功信息
     */
    @PostMapping("/finish")
    public R<String> finish(@RequestBody RoomRepair roomRepair) {
        roomRepairService.finish(roomRepair);
        return R.success("完成");
    }
}
