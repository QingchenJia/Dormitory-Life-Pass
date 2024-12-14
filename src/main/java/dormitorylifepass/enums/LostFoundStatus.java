package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum LostFoundStatus {
    UPLOADED(1, "已上传"),
    SOLVED(2, "已解决");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    LostFoundStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static LostFoundStatus toEnum(Integer code) {
        for (LostFoundStatus lostFoundStatus : LostFoundStatus.values()) {
            if (lostFoundStatus.getCode().equals(code)) {
                return lostFoundStatus;
            }
        }
        return null;
    }
}
