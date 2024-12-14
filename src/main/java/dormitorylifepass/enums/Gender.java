package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum Gender {
    MALE(1, "男"),
    FEMALE(0, "女");

    @EnumValue
    private final Integer code;
    private final String name;

    Gender(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
