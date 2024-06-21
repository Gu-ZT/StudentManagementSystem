package dev.dubhe.sms.manager.data.dto;

import dev.dubhe.sms.manager.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ChangeStudentDto {
    private Long id;           // ID
    private String studentId;  // 学号
    private String name;       // 姓名
    private String sex;        // 性别
    private Date birth;        // 出生日期
    private String qq;         // QQ号
    private String phone;      // 手机号
    private String address;    // 住址
    private Long image;        // 照片

    public Date getBirth() {
        return DateUtil.afterDays(this.birth, 1);
    }
}
