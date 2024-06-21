package dev.dubhe.sms.manager.data.vo;

import dev.dubhe.sms.manager.data.pojo.Student;
import jakarta.annotation.Nonnull;

import java.util.Date;

public record StudentVo(String id, String studentId, String name, String sex, Date birth, String qq, String phone, String address, String image) {
    public static @Nonnull StudentVo of(@Nonnull Student student){
        return new StudentVo(
            student.getId().toString(),
            student.getStudentId(),
            student.getName(),
            student.getSex(),
            student.getBirth(),
            student.getQq(),
            student.getPhone(),
            student.getAddress(),
            student.getImage().toString()
        );
    }
}
