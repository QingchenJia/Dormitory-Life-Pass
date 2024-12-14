package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoomRepairStatus {
    REQUESTED(1, "已申请"),
    ARRANGED(2, "已安排"),
    FINISHED(3, "已完成");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    RoomRepairStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static RoomRepairStatus toEnum(Integer code) {
        for (RoomRepairStatus roomRepairStatus : RoomRepairStatus.values()) {
            if (roomRepairStatus.getCode().equals(code)) {
                return roomRepairStatus;
            }
        }
        return null;
    }
}
