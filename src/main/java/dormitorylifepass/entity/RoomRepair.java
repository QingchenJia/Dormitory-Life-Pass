package dormitorylifepass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitorylifepass.common.BaseEntity;
import dormitorylifepass.enums.RoomRepairStatus;
import lombok.Data;

@Data
public class RoomRepair extends BaseEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roomId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long studentId;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long employeeDmId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long employeeMwId;

    private RoomRepairStatus status;
}
