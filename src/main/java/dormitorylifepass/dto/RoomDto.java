package dormitorylifepass.dto;

import dormitorylifepass.entity.Room;
import dormitorylifepass.entity.Student;
import lombok.Data;

import java.util.List;

@Data
public class RoomDto extends Room {
    private String buildingName;

    private Integer peopleNum;

    private List<Student> students;
}
