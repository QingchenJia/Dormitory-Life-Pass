package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.dto.RoomRepairDto;
import dormitorylifepass.entity.RoomRepair;

import java.util.List;

public interface RoomRepairService extends IService<RoomRepair> {
    void insertRoomRepair(RoomRepair roomRepair);

    Page<RoomRepairDto> selectPage(Page<RoomRepair> page);

    List<RoomRepairDto> selectList(Long id, Integer status);

    void arrange(RoomRepair roomRepair);

    void finish(RoomRepair roomRepair);
}
