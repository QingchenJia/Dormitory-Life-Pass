package dormitorylifepass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.common.R;
import dormitorylifepass.dto.RoomChangeDto;
import dormitorylifepass.entity.RoomChange;
import dormitorylifepass.service.RoomChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping
    public R<String> application(@RequestBody RoomChange roomChange) {
        // 记录宿舍调换申请的日志信息
        log.info("宿舍调换申请:{}", roomChange);
        // 调用服务层方法，将宿舍调换申请信息插入到数据库中
        roomChangeService.insertRoomChange(roomChange);
        // 返回成功响应，表示请求已成功申请
        return R.success("请求已申请");
    }

    /**
     * 审批通过房间状态变更请求
     * 该方法用于处理审批通过的操作，通过更新房间状态来响应审批请求
     *
     * @param ids 房间ID列表，表示需要审批通过的房间变更请求
     * @return 返回一个响应对象，表示操作结果和消息
     */
    @PutMapping("/status/2")
    public R<String> approve(@RequestParam("ids") List<Long> ids) {
        // 调用服务层方法更新房间状态
        roomChangeService.updateStatus(ids, 2);
        // 返回成功响应
        return R.success("已通过");
    }

    /**
     * 使用PUT请求更新房间状态为驳回
     * 该接口接收一个房间ID列表，并将这些房间的状态更新为驳回（状态码3）
     *
     * @param ids 房间ID列表，用于指定需要更新状态的房间
     * @return 返回一个自定义的响应对象，表示操作成功，并附带简短的成功消息
     */
    @PutMapping("/status/3")
    public R<String> overrule(@RequestParam("ids") List<Long> ids) {
        // 调用服务层方法，更新房间状态
        roomChangeService.updateStatus(ids, 3);
        // 返回成功响应，附带成功消息
        return R.success("已驳回");
    }

    /**
     * 接受房间状态变更请求
     * 该方法通过PUT请求更新房间的状态为已接受
     *
     * @param ids 房间ID列表，表示需要更新状态的房间
     * @return 返回一个响应对象，表示操作结果
     */
    @PutMapping("/status/4")
    public R<String> accept(@RequestParam("ids") List<Long> ids) {
        // 调用服务层方法更新房间状态
        roomChangeService.updateStatus(ids, 4);
        // 返回成功响应
        return R.success("已接受");
    }

    /**
     * 处理拒绝请求的操作
     * 通过PUT请求更新房间变更申请的状态为拒绝
     *
     * @param ids 房间变更申请的ID列表，这些申请将被拒绝
     * @return 返回一个响应对象，表示操作结果
     */
    @PutMapping("/status/5")
    public R<String> reject(@RequestParam("ids") List<Long> ids) {
        // 调用服务层方法更新状态
        roomChangeService.updateStatus(ids, 5);
        // 返回成功响应
        return R.success("已拒绝");
    }
}
