package dormitorylifepass.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import dormitorylifepass.common.BaseEntity;
import dormitorylifepass.enums.LostFoundStatus;
import dormitorylifepass.enums.LostFoundType;
import lombok.Data;

@Data
public class LostFound extends BaseEntity {
    private String description;

    @EnumValue
    private LostFoundType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long studentId;

    private String phone;

    private LostFoundStatus status;
}
