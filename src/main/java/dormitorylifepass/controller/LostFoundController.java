package dormitorylifepass.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dormitorylifepass.common.R;
import dormitorylifepass.entity.LostFound;
import dormitorylifepass.service.LostFoundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lostFound")
@Slf4j
public class LostFoundController {
    @Autowired
    private LostFoundService lostFoundService;

    /**
     * 发布失物招领信息
     * <p>
     * 该方法通过POST请求接收失物招领信息，并将其添加到系统中
     * 主要功能包括：
     * 1. 接收客户端发送的失物招领信息
     * 2. 记录日志，便于跟踪和调试
     * 3. 调用服务层方法，将失物招领信息插入数据库
     * 4. 返回发布结果，告知客户端操作是否成功
     *
     * @param lostFound 失物招领信息，包含物品名称、描述、联系人等
     * @return 返回一个表示操作结果的响应对象，包含成功消息
     */
    @PostMapping
    public R<String> publish(@RequestBody LostFound lostFound) {
        // 记录发布失物招领信息的日志
        log.info("发布失物招领信息：{}", lostFound);

        // 调用服务层方法，插入失物招领信息到数据库
        lostFoundService.insertLostFound(lostFound);

        // 返回发布成功的响应
        return R.success("发布成功");
    }

    /**
     * 处理寻物启事的分页请求
     *
     * @param page     当前页码，用于指定从哪一页开始获取数据
     * @param pageSize 每页大小，用于指定每页包含的寻物启事数量
     * @return 返回一个响应对象，包含分页的寻物启事信息
     */
    @GetMapping("/page")
    public R<Page<LostFound>> page(int page, int pageSize) {
        // 创建一个分页对象，用于封装分页查询参数
        Page<LostFound> pageInfo = new Page<>(page, pageSize);

        // 调用服务层方法，执行分页查询
        lostFoundService.page(pageInfo);

        // 返回成功响应，携带分页信息
        return R.success(pageInfo);
    }

    /**
     * 处理失物招领列表请求
     *
     * @param studentId 学生ID，用于查询特定学生的失物招领信息
     * @return 返回一个封装了失物招领列表的响应对象
     */
    @GetMapping("/list")
    public R<List<LostFound>> list(Long studentId) {
        // 调用服务层方法，获取数据库中的失物招领信息列表
        List<LostFound> lostFoundsDB = lostFoundService.selectList(studentId);
        // 返回成功响应，包含失物招领列表
        return R.success(lostFoundsDB);
    }
}
