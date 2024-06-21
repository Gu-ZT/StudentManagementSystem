package dev.dubhe.sms.manager.service.impl;

import dev.dubhe.sms.manager.data.dao.IStudentDao;
import dev.dubhe.sms.manager.data.pojo.Student;
import dev.dubhe.sms.manager.data.vo.StudentVo;
import dev.dubhe.sms.manager.exception.CustomException;
import dev.dubhe.sms.manager.service.IStudentService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService implements IStudentService {
    private final IStudentDao studentDao;

    @Override
    public boolean createStudent(@Nonnull String studentId, @Nonnull String name, @Nullable String sex, @Nullable Date birth, @Nullable String qq, @Nullable String phone, @Nullable String address, @Nullable Long image) {
        Student student = new Student(studentId, name, sex, birth, qq, phone, address, image);
        return studentDao.save(student);
    }

    @Override
    public boolean changeStudent(@Nonnull Long id, @Nullable String studentId, @Nullable String name, @Nullable String sex, @Nullable Date birth, @Nullable String qq, @Nullable String phone, @Nullable String address, @Nullable Long image) {
        Student student = studentDao.getById(id);
        if (student == null) throw CustomException.operationFailed();
        student.setStudentId(studentId != null ? studentId : student.getStudentId());
        student.setName(name != null ? name : student.getName());
        student.setSex(sex != null ? sex : student.getSex());
        student.setBirth(birth != null ? birth : student.getBirth());
        student.setQq(qq != null ? qq : student.getQq());
        student.setPhone(phone != null ? phone : student.getPhone());
        student.setAddress(address != null ? address : student.getAddress());
        student.setImage(image != null ? image : student.getImage());
        return studentDao.updateById(student);
    }

    @Override
    public boolean removeStudent(@Nonnull Long id) {
        Student student = studentDao.getById(id);
        if (student == null) throw CustomException.operationFailed();
        return studentDao.removeById(student);
    }

    @Override
    public @Nonnull List<StudentVo> getStudents() {
        return studentDao.list()
            .stream()
            .map(StudentVo::of)
            .toList();
    }
}
