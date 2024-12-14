package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum LostFoundStatus {
    UPLOADED(1, "已上传"),
    SOLVED(2, "已解决");

    @EnumValue
    private final Integer code;
    private final String name;

    LostFoundStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
