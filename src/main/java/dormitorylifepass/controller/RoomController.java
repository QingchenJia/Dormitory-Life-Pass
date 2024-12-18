package dormitorylifepass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.common.R;
import dormitorylifepass.entity.Room;
import dormitorylifepass.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@Slf4j
public class RoomController {
    @Autowired
    private RoomService roomService;

    /**
     * 处理新增房间的请求
     * <p>
     * 该方法接收一个Room对象作为参数，通过roomService进行保存，并返回保存结果的信息
     * 使用@PostMapping注解表明该方法处理POST请求
     *
     * @param room 房间对象，包含房间的详细信息
     * @return 返回一个R<String>对象，包含操作结果的字符串信息
     */
    @PostMapping
    public R<String> save(@RequestBody Room room) {
        // 记录新增房间的日志，打印房间信息
        log.info("新增房间，房间信息：{}", room);

        // 调用roomService的save方法，保存房间信息
        roomService.save(room);

        // 返回成功保存后的响应信息
        return R.success("新增成功");
    }

    /**
     * 处理房间分页查询请求
     *
     * @param page     页码，表示请求的页面编号
     * @param pageSize 每页大小，表示每页显示的房间数量
     * @param name     房间名称关键字，用于模糊查询
     * @return 返回一个响应对象，包含分页查询的结果
     * <p>
     * 此方法通过接收页码、每页大小和房间名称关键字参数，
     * 使用RoomService进行分页查询，然后将查询结果封装到响应对象中返回。
     * 使用分页查询可以有效提高查询效率，避免一次性加载大量数据导致的性能问题。
     */
    @GetMapping("/page")
    public R<Page<Room>> page(int page, int pageSize, String name) {
        // 创建分页对象，传入当前页码和每页大小
        Page<Room> pageInfo = new Page<>(page, pageSize);
        // 调用服务层方法执行分页查询，根据房间名称关键字进行模糊查询
        roomService.selectPage(pageInfo, name);
        // 返回包含分页查询结果的响应对象
        return R.success(pageInfo);
    }

    /**
     * 根据房间ID查询房间信息
     *
     * @param id 房间ID，用于标识特定的房间
     * @return 返回一个包含房间信息的响应对象
     */
    @GetMapping("/{id}")
    public R<Room> queryOne(@PathVariable Long id) {
        // 调用roomService的getById方法根据房间ID查询房间信息
        Room room = roomService.getById(id);
        // 返回包含查询结果的响应对象
        return R.success(room);
    }

    /**
     * 更新房间信息的接口方法
     * 使用HTTP PUT请求来更新数据库中特定房间的信息
     *
     * @param room 包含更新信息的房间对象，通过请求体传递
     * @return 返回一个表示操作结果的响应对象，包含成功消息
     */
    @PutMapping
    public R<String> update(@RequestBody Room room) {
        // 调用服务层方法，根据房间对象的ID更新房间信息
        roomService.updateById(room);
        // 返回成功响应，包含成功消息
        return R.success("修改成功");
    }
}