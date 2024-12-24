package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.entity.LostFound;

import java.util.List;

public interface LostFoundService extends IService<LostFound> {
    void insertLostFound(LostFound lostFound);

    List<LostFound> selectList(Long studentId);

    void solve(List<Long> ids);

    void deleteById(Long id);
}
