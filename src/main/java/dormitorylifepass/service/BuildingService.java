package dormitorylifepass.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import dormitorylifepass.entity.Building;

public interface BuildingService extends IService<Building> {
    void selectPage(Page<Building> page, String name);

    void updateBuilding(Building building);
}
