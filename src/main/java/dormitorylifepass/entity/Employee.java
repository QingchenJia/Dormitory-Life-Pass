package dormitorylifepass.entity;

import dormitorylifepass.common.BaseEntity;
import dormitorylifepass.enums.EmployeeStatus;
import dormitorylifepass.enums.EmployeeType;
import dormitorylifepass.enums.Gender;
import lombok.Data;

@Data
public class Employee extends BaseEntity {
    private String jobNum;

    private String name;

    private String username;

    private String password;

    private Gender gender;

    private String phone;

    private EmployeeType type;

    private EmployeeStatus status;
}
