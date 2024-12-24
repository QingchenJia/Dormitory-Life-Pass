package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.dto.RoomDto;
import dormitorylifepass.entity.Room;

import java.util.List;

public interface RoomService extends IService<Room> {
    void selectPage(Page<Room> page, String name);

    List<Room> selectList(Integer gender);

    void insertRoom(Room room);

    List<RoomDto> searchInfo(Long buildingId);

    void updateStatus();

    Room queryOne(Long id);

    void updateByRoomId(Room room);
}
