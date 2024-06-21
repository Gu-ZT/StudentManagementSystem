package dev.dubhe.sms.manager.data.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends AbstractSqlBase {
    private String username;  // 用户名
    private String password;  // 密码
    private String nickname;  // 昵称

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.nickname = username;
    }
}
