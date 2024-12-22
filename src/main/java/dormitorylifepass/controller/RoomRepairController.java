package dormitorylifepass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.common.R;
import dormitorylifepass.dto.RoomRepairDto;
import dormitorylifepass.entity.RoomRepair;
import dormitorylifepass.service.RoomRepairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
