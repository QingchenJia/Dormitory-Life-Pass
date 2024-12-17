package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum EmployeeStatus {
    ARRANGED(1, "已安排"),
    NOT_ARRANGED(0, "未安排");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    EmployeeStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static EmployeeStatus toEnum(Integer code) {
        for (EmployeeStatus employeeStatus : EmployeeStatus.values()) {
            if (employeeStatus.getCode().equals(code)) {
                return employeeStatus;
            }
        }
        return null;
    }
}
