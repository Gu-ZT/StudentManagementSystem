package dev.dubhe.sms.manager.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.dubhe.sms.manager.data.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
