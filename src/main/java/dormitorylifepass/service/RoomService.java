package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.entity.Room;

public interface RoomService extends IService<Room> {
    void selectPage(Page<Room> page, String name);
}
