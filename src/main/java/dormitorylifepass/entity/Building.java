package dormitorylifepass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitorylifepass.common.BaseEntity;
import lombok.Data;

@Data
public class Building extends BaseEntity {
    private String location;

    private Integer number;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long employeeId;
}
