package dormitorylifepass.common;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected Long createUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected Long updateUser;

    @TableLogic(value = "0", delval = "1")
    protected Integer isDeleted;
}
