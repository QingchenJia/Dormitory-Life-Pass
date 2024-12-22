package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.dto.RoomRepairDto;
import dormitorylifepass.entity.RoomRepair;

public interface RoomRepairService extends IService<RoomRepair> {
    void insertRoomRepair(RoomRepair roomRepair);

    Page<RoomRepairDto> selectPage(Page<RoomRepair> page);
}
