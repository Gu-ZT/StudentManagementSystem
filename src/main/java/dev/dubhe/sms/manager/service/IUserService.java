package dev.dubhe.sms.manager.service;

import dev.dubhe.sms.manager.data.vo.UserLoginVo;
import jakarta.servlet.http.HttpServletRequest;

public interface IUserService {
    UserLoginVo register(String username, String password);

    UserLoginVo login(String username, String password);

    boolean changePwd(HttpServletRequest request, String old, String password);
}
