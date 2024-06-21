package dev.dubhe.sms.manager.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeNicknameDto {
    @NotBlank(message = "昵称不能为空")
    private String nickname;
}
