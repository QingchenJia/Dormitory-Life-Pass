package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.dto.RoomChangeDto;
import dormitorylifepass.entity.RoomChange;

import java.util.List;

public interface RoomChangeService extends IService<RoomChange> {
    void insertRoomChange(RoomChange roomChange);

    Page<RoomChangeDto> selectPage(Page<RoomChange> page);

    void updateStatus(List<Long> ids, Integer status);

    List<RoomChangeDto> selectList(Long id, Integer status);
}
