package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CheckInStatus {
    ONGOING(1, "进行中"),
    ENDED(2, "已结束");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    CheckInStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static CheckInStatus toEnum(Integer code) {
        for (CheckInStatus checkInStatus : CheckInStatus.values()) {
            if (checkInStatus.getCode().equals(code)) {
                return checkInStatus;
            }
        }
        return null;
    }
}
