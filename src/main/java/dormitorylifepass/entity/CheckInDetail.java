package dormitorylifepass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitorylifepass.common.BaseEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class CheckInDetail extends BaseEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long checkInId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long studentId;

    private String description;
}
