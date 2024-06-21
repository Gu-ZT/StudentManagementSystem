package dev.dubhe.sms.manager.data.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.dubhe.sms.manager.data.dao.IFileMapDao;
import dev.dubhe.sms.manager.data.dao.IStudentDao;
import dev.dubhe.sms.manager.data.mapper.FileMapMapper;
import dev.dubhe.sms.manager.data.mapper.StudentMapper;
import dev.dubhe.sms.manager.data.pojo.FileMap;
import dev.dubhe.sms.manager.data.pojo.Student;
import org.springframework.stereotype.Service;

@Service
public class FileMapDao extends ServiceImpl<FileMapMapper, FileMap> implements IFileMapDao {
}
