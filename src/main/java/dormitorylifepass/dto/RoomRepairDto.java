package dormitorylifepass.dto;

import dormitorylifepass.entity.RoomRepair;
import lombok.Data;

@Data
public class RoomRepairDto extends RoomRepair {
    private String roomName;

    private String studentName;

    private String employeeDmName;

    private String employeeMwName;
}
