package dormitorylifepass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitorylifepass.common.BaseEntity;
import dormitorylifepass.enums.RoomChangeStatus;
import lombok.Data;

@Data
public class RoomChange extends BaseEntity {
    private Long oldRoomId;

    private Long newRoomId;

    private Long studentId;

    private String reason;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long oldEmployeeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long newEmployeeId;

    private RoomChangeStatus status;
}
