package dormitorylifepass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dormitorylifepass.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
