package dev.dubhe.sms.manager.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import dev.dubhe.sms.manager.data.dao.IUserDao;
import dev.dubhe.sms.manager.data.pojo.User;
import dev.dubhe.sms.manager.data.vo.UserLoginVo;
import dev.dubhe.sms.manager.exception.CustomException;
import dev.dubhe.sms.manager.service.ITokenService;
import dev.dubhe.sms.manager.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final IUserDao userDao;
    private final PasswordEncoder encoder;
    private final ITokenService tokenService;

    @Override
    public UserLoginVo register(String username, String password) {
        User user = userDao.getByUserName(username);
        if (user != null) throw new CustomException("用户已存在");
        password = encoder.encode(password);
        user = new User(username, password);
        userDao.save(user);
        String token = tokenService.createToken(user);
        return new UserLoginVo(user.getId(), user.getNickname(), token);
    }

    @Override
    public UserLoginVo login(String username, String password) {
        User user = userDao.getByUserName(username);
        if (user == null) throw new CustomException("用户不存在");
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw CustomException.loginFail();
        }
        String token = tokenService.createToken(user);
        return new UserLoginVo(user.getId(), user.getNickname(), token);
    }

    @Override
    public boolean changePwd(HttpServletRequest request, String old, String password) {
        String token = tokenService.getToken(request);
        User user = tokenService.parserToken(token);
        if (user == null) throw CustomException.unauthorized();
        if (!BCrypt.checkpw(old, user.getPassword())) {
            throw CustomException.error("密码错误！");
        }
        password = encoder.encode(password);
        user.setPassword(password);
        return userDao.updateById(user);
    }
}
