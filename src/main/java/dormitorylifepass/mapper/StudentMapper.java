package dormitorylifepass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dormitorylifepass.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
