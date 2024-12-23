package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.entity.CheckInDetail;

public interface CheckInDetailService extends IService<CheckInDetail> {
    void signIn(CheckInDetail checkInDetail);
}
