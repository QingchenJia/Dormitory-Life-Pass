package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum EmployeeType {
    DORMITORY_MANAGER(1, "宿舍管理员"),
    MAINTENANCE_WORKER(2, "维修工"),
    ROOT(0, "超级管理员");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    EmployeeType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static EmployeeType toEnum(Integer code) {
        for (EmployeeType employeeType : EmployeeType.values()) {
            if (employeeType.getCode().equals(code)) {
                return employeeType;
            }
        }
        return null;
    }
}
