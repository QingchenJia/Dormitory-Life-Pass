package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.CheckIn;
import dormitorylifepass.mapper.CheckInMapper;
import dormitorylifepass.service.CheckInService;
import org.springframework.stereotype.Service;

@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements CheckInService {
}
