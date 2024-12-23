package dormitorylifepass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.common.R;
import dormitorylifepass.dto.CheckInDto;
import dormitorylifepass.entity.CheckIn;
import dormitorylifepass.service.CheckInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkIn")
@Slf4j
public class CheckInController {
    @Autowired
    private CheckInService checkInService;

    /**
     * 处理签到信息的发布请求
     * 该方法接收一个签到对象，将其信息记录到日志，并通过服务层接口插入到数据库中
     *
     * @param checkIn 包含签到相关信息的CheckIn对象，由请求体中获取
     * @return 返回一个表示操作结果的R对象，包含成功消息
     */
    @PostMapping
    public R<String> publish(@RequestBody CheckIn checkIn) {
        // 记录签到信息到日志，便于调试和追踪
        log.info("发布签到信息：{}", checkIn);

        // 调用服务层接口方法，插入签到信息到数据库
        checkInService.insertCheckIn(checkIn);

        // 返回成功响应，表示签到信息发布成功
        return R.success("发布成功");
    }

    /**
     * 处理检查记录的分页请求
     *
     * @param page     当前页码，用于指定从哪一页开始分页
     * @param pageSize 每页大小，用于指定每页包含的记录数
     * @return 返回一个分页响应对象，包含检查记录的列表和分页信息
     */
    @GetMapping("page")
    public R<Page<CheckInDto>> page(int page, int pageSize) {
        // 创建一个分页对象，用于后续的分页查询
        Page<CheckIn> pageInfo = new Page<>(page, pageSize);
        // 此处应进行分页查询操作，但代码未给出具体实现
        return null;
    }

    /**
     * 根据用户ID获取签到列表
     * <p>
     * 此接口用于获取特定用户的签到信息列表通过用户的唯一标识ID来查询该用户的所有签到记录
     * 主要用于前端展示用户的签到历史记录
     *
     * @param id 用户ID，用于查询签到信息的唯一标识
     * @return 返回一个封装了签到信息列表的响应对象
     */
    @GetMapping("list")
    public R<List<CheckInDto>> list(Long id) {
        // 调用服务层方法，根据用户ID查询签到信息列表
        List<CheckInDto> checkInDtos = checkInService.selectList(id);
        // 返回成功响应，包含签到信息列表
        return R.success(checkInDtos);
    }
}
