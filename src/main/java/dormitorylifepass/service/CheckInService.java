package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.dto.CheckInDto;
import dormitorylifepass.entity.CheckIn;

import java.util.List;

public interface CheckInService extends IService<CheckIn> {
    void insertCheckIn(CheckIn checkIn);

    Page<CheckInDto> selectPage(Page<CheckIn> page);

    List<CheckInDto> selectList(Long id);
}
