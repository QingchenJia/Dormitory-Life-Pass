package dormitorylifepass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.RoomChange;
import dormitorylifepass.enums.RoomChangeStatus;
import dormitorylifepass.mapper.RoomChangeMapper;
import dormitorylifepass.service.RoomChangeService;
import org.springframework.stereotype.Service;

@Service
public class RoomChangeServiceImpl extends ServiceImpl<RoomChangeMapper, RoomChange> implements RoomChangeService {
    /**
     * 插入房间变更记录
     * <p>
     * 该方法用于处理房间变更的请求它将房间变更的状态设置为"已请求"，并保存该变更记录
     *
     * @param roomChange 房间变更对象，包含房间变更的详细信息
     */
    @Override
    public void insertRoomChange(RoomChange roomChange) {
        // 设置房间变更的状态为已请求
        roomChange.setStatus(RoomChangeStatus.REQUESTED);
        // 保存房间变更记录
        save(roomChange);
    }
}
