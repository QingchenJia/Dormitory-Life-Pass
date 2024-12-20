package dormitorylifepass.dto;

import dormitorylifepass.entity.RoomChange;
import lombok.Data;

@Data
public class RoomChangeDto extends RoomChange {
    private String studentName;

    private String oldRoomName;

    private String newRoomName;

    private String oldEmployeeName;

    private String newEmployeeName;
}
