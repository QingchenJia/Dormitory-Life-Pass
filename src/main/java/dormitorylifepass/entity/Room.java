package dormitorylifepass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitorylifepass.common.BaseEntity;
import dormitorylifepass.enums.RoomStatus;
import lombok.Data;

@Data
public class Room extends BaseEntity {
    private Integer floor;

    private Integer number;

    private String name;

    private Integer maxNum;

    private RoomStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long buildingId;
}
