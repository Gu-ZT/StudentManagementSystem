package dev.dubhe.sms.manager.service;

import dev.dubhe.sms.manager.data.vo.StudentVo;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.List;

public interface IStudentService {
    boolean createStudent(@Nonnull String studentId, @Nonnull String name, @Nullable String sex, @Nullable Date birth, @Nullable String qq, @Nullable String phone, @Nullable String address, @Nullable Long image);

    boolean changeStudent(@Nonnull Long id, @Nullable String studentId, @Nullable String name, @Nullable String sex, @Nullable Date birth, @Nullable String qq, @Nullable String phone, @Nullable String address, @Nullable Long image);

    boolean removeStudent(@Nonnull Long id);

    @Nonnull List<StudentVo> getStudents();
}
