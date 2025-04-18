package dormitorylifepass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitorylifepass.common.BaseEntity;
import dormitorylifepass.enums.BuildingType;
import lombok.Data;

@Data
public class Building extends BaseEntity {
    private String location;

    private String name;

    private BuildingType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long employeeId;
}
