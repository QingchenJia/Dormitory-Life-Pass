package dormitorylifepass.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum BuildingType {
    MEN_DORMITORY(1, "男宿舍楼"),
    WOMEN_DORMITORY(0, "女宿舍楼");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    BuildingType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static BuildingType toEnum(Integer code) {
        for (BuildingType buildingType : BuildingType.values()) {
            if (buildingType.getCode().equals(code)) {
                return buildingType;
            }
        }
        return null;
    }
}
