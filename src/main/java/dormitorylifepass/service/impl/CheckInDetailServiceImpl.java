package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.CheckInDetail;
import dormitorylifepass.mapper.CheckInDetailMapper;
import dormitorylifepass.service.CheckInDetailService;
import org.springframework.stereotype.Service;

@Service
public class CheckInDetailServiceImpl extends ServiceImpl<CheckInDetailMapper, CheckInDetail> implements CheckInDetailService {
}
