package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.entity.RoomChange;

public interface RoomChangeService extends IService<RoomChange> {
    void insertRoomChange(RoomChange roomChange);
}
