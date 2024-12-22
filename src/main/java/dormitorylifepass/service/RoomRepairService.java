package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.entity.RoomRepair;

public interface RoomRepairService extends IService<RoomRepair> {
    void insertRoomRepair(RoomRepair roomRepair);
}
