package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum LostFoundType {
    FOUND(1, "找到"),
    LOST(2, "丢失");

    @EnumValue
    private final Integer code;
    private final String name;

    LostFoundType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
