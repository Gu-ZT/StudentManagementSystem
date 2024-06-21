package dev.dubhe.sms.manager.data.dto;

import dev.dubhe.sms.manager.util.RSAUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePwdDto {
    private String old;
    private String password;

    public String getOld() {
        return RSAUtil.decryptByPrivateKey(this.old);
    }
    public String getPassword() {
        return RSAUtil.decryptByPrivateKey(this.password);
    }
}
