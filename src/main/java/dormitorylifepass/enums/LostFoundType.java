package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum LostFoundType {
    FOUND(1, "找到"),
    LOST(2, "丢失");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    LostFoundType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static LostFoundType toEnum(Integer code) {
        for (LostFoundType lostFoundType : LostFoundType.values()) {
            if (lostFoundType.getCode().equals(code)) {
                return lostFoundType;
            }
        }
        return null;
    }
}
