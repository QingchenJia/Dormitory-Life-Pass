package dormitorylifepass.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitorylifepass.common.BaseEntity;
import dormitorylifepass.enums.Gender;
import lombok.Data;

@Data
public class Student extends BaseEntity {
    private String studentNum;

    private String name;

    private String username;

    private String password;

    private Gender gender;

    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roomId;
}
