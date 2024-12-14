package dormitorylifepass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitorylifepass.common.BaseEntity;
import dormitorylifepass.enums.CheckInStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CheckIn extends BaseEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long employeeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long buildingId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private CheckInStatus status;

    private Integer signInNum;

    private Integer totalNum;
}
