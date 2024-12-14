package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum CheckInStatus {
    ONGOING(1, "进行中"),
    ENDED(2, "已结束");

    @EnumValue
    private final Integer code;
    private final String name;

    CheckInStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
