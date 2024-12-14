package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoomChangeStatus {
    REQUESTED(1, "已申请"),
    APPROVE(2, "通过"),
    OVERRULE(3, "驳回"),
    ACCEPT(4, "接受"),
    REJECT(5, "拒绝");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    RoomChangeStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static RoomChangeStatus toEnum(Integer code) {
        for (RoomChangeStatus roomChangeStatus : RoomChangeStatus.values()) {
            if (roomChangeStatus.getCode().equals(code)) {
                return roomChangeStatus;
            }
        }
        return null;
    }
}
