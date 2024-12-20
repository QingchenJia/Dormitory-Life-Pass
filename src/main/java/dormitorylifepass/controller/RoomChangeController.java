package dormitorylifepass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.common.R;
import dormitorylifepass.dto.RoomChangeDto;
import dormitorylifepass.entity.RoomChange;
import dormitorylifepass.service.RoomChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roomChange")
@Slf4j
public class RoomChangeController {
    @Autowired
    private RoomChangeService roomChangeService;

    @GetMapping("/page")
    public R<Page<RoomChangeDto>> page(int page, int pageSize) {
        // 创建分页对象，传入当前页码和每页大小
        Page<RoomChange> pageInfo = new Page<>(page, pageSize);
        // 调用服务层方法执行分页查询
        Page<RoomChangeDto> pageResult = roomChangeService.selectPage(pageInfo);
        // 返回包含分页查询结果的响应对象
        return R.success(pageResult);
    }

    /**
     * 处理宿舍调换申请的接口
     * 该接口接收一个RoomChange对象，其中包含宿舍调换的相关信息
     * 主要作用是将调换申请的信息记录到系统中
     *
     * @param roomChange 宿舍调换申请对象，包含调换的详细信息
     * @return 返回一个R<String>对象，表示请求处理的结果
     */
    @PostMapping("/application")
    public R<String> application(@RequestBody RoomChange roomChange) {
        // 记录宿舍调换申请的日志信息
        log.info("宿舍调换申请:{}", roomChange);
        // 调用服务层方法，将宿舍调换申请信息插入到数据库中
        roomChangeService.insertRoomChange(roomChange);
        // 返回成功响应，表示请求已成功申请
        return R.success("请求已申请");
    }
}
