package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum EmployeeType {
    DORMITORY_MANAGER(1, "宿舍管理员"),
    MAINTENANCE_WORKER(2, "维修工"),
    ROOT(0, "超级管理员");

    @EnumValue
    private final Integer code;
    private final String name;

    EmployeeType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
