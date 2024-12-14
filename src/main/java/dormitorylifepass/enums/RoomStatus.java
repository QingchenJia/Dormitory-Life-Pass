package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum RoomStatus {
    AVAILABLE(1, "可入住"),
    UNAVAILABLE(0, "不可入住");

    @EnumValue
    private final Integer code;
    private final String name;

    RoomStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
