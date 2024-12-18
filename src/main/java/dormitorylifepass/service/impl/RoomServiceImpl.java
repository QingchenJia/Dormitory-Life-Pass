package dormitorylifepass.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dormitorylifepass.entity.Room;
import dormitorylifepass.mapper.RoomMapper;
import dormitorylifepass.service.RoomService;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
    /**
     * 根据楼层查询房间信息
     *
     * @param page 分页对象，用于封装分页查询所需的信息
     * @param name 房间名称的关键词，用于模糊查询
     */
    @Override
    public void selectPage(Page<Room> page, String name) {
        // 创建Lambda查询条件包装器，用于构建查询条件和排序
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();

        // 如果名称不为空且不只包含空格，则按名称进行模糊查询，并按楼层升序排序
        queryWrapper.like(StringUtil.notNullNorEmpty(name), Room::getName, name)
                .orderByAsc(Room::getFloor);

        // 执行分页查询
        page(page, queryWrapper);
    }
}
