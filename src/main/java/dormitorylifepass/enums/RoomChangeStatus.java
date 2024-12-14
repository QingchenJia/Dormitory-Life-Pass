package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
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
    private final String name;

    RoomChangeStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
