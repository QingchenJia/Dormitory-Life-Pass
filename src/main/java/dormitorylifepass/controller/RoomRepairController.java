package dormitorylifepass.controller;

import dormitorylifepass.common.R;
import dormitorylifepass.entity.RoomRepair;
import dormitorylifepass.service.RoomRepairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
