package dormitorylifepass.controller;

import dormitorylifepass.common.R;
import dormitorylifepass.entity.CheckInDetail;
import dormitorylifepass.service.CheckInDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkInDetail")
@Slf4j
public class CheckInDetailController {
    @Autowired
    private CheckInDetailService checkInDetailService;

    /**
     * 处理用户签到请求
     * <p>
     * 该方法通过接收用户签到的详细信息来记录用户的签到行为
     * 它首先记录签到信息，然后通过调用服务层方法来保存这些信息
     * 最后，返回一个表示签到成功的响应
     *
     * @param checkInDetail 包含用户签到详细信息的对象，包括用户ID、签到时间等
     * @return 返回一个自定义的响应对象，表示签到操作的结果
     */
    @PostMapping
    public R<String> signIn(@RequestBody CheckInDetail checkInDetail) {
        // 打印签到信息以便于调试和日志记录
        log.info("签到信息：{}", checkInDetail);

        // 调用服务层方法保存签到信息
        checkInDetailService.signIn(checkInDetail);

        // 返回签到成功的响应
        return R.success("签到成功");
    }
}
