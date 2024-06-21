package dev.dubhe.sms.manager.data.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("student")
public class Student extends AbstractSqlBase {
    private String studentId;  // 学号
    private String name;       // 姓名
    private String sex;        // 性别
    private Date birth;        // 出生日期
    private String qq;         // QQ号
    private String phone;      // 手机号
    private String address;    // 住址
    private Long   image;      // 照片

    public Student(String studentId, String name, String sex, Date birth, String qq, String phone, String address, Long image) {
        this.studentId = studentId;
        this.name = name;
        this.sex = sex;
        this.birth = birth;
        this.qq = qq;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }
}
