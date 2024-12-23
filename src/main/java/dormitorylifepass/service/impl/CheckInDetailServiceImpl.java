package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.CheckIn;
import dormitorylifepass.entity.CheckInDetail;
import dormitorylifepass.mapper.CheckInDetailMapper;
import dormitorylifepass.service.CheckInDetailService;
import dormitorylifepass.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckInDetailServiceImpl extends ServiceImpl<CheckInDetailMapper, CheckInDetail> implements CheckInDetailService {
    @Autowired
    private CheckInService checkInService;

    /**
     * 签名方法，用于处理用户的签到操作
     *
     * @param checkInDetail 包含签到详细信息的对象，包括签到ID和其他相关数据
     */
    @Override
    public void signIn(CheckInDetail checkInDetail) {
        // 保存签到详细信息到数据库
        save(checkInDetail);

        // 创建查询条件，用于查询具有相同签到ID的所有签到详细记录
        LambdaQueryWrapper<CheckInDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CheckInDetail::getCheckInId, checkInDetail.getCheckInId());

        // 计算签到人数，即具有相同签到ID的记录数
        int signInNum = (int) count(queryWrapper);

        // 创建一个CheckIn对象，用于更新签到记录的签到人数
        CheckIn checkIn = new CheckIn();
        checkIn.setId(checkInDetail.getCheckInId());
        checkIn.setSignInNum(signInNum);

        // 调用CheckInService的updateById方法来更新签到记录的签到人数
        checkInService.updateById(checkIn);
    }
}
