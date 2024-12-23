package dormitorylifepass.dto;

import dormitorylifepass.entity.CheckIn;
import lombok.Data;

@Data
public class CheckInDto extends CheckIn {
    private String employeeName;

    private String buildingName;
}
