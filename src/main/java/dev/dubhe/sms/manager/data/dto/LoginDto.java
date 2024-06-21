package dev.dubhe.sms.manager.data.dto;

import dev.dubhe.sms.manager.util.RSAUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {
    @NotBlank(message = "用户名或密码不能为空")
    private String username;
    @NotBlank(message = "用户名或密码不能为空")
    private String password;

    public String getPassword() {
        return RSAUtil.decryptByPrivateKey(this.password);
    }
}
