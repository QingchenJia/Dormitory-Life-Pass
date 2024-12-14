package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum RoomRepairStatus {
    REQUESTED(1, "已申请"),
    ARRANGED(2, "已安排"),
    FINISHED(3, "已完成");

    @EnumValue
    private final Integer code;
    private final String name;

    RoomRepairStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
