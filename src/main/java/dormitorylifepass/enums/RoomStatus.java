package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoomStatus {
    AVAILABLE(1, "可入住"),
    UNAVAILABLE(0, "不可入住");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    RoomStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static RoomStatus toEnum(Integer code) {
        for (RoomStatus roomStatus : RoomStatus.values()) {
            if (roomStatus.getCode().equals(code)) {
                return roomStatus;
            }
        }
        return null;
    }
}
