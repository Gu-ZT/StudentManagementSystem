package dev.dubhe.sms.manager.controller;

import dev.dubhe.sms.manager.data.ResponseResult;
import dev.dubhe.sms.manager.data.dto.ChangePwdDto;
import dev.dubhe.sms.manager.data.dto.LoginDto;
import dev.dubhe.sms.manager.data.pojo.User;
import dev.dubhe.sms.manager.exception.CustomException;
import dev.dubhe.sms.manager.service.ITokenService;
import dev.dubhe.sms.manager.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {
    private final IUserService userService;
    private final ITokenService tokenService;

    @PostMapping("/register")
    public ResponseResult register(@RequestBody LoginDto login) {
        return ResponseResult.success(userService.register(login.getUsername(), login.getPassword()));
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginDto login) {
        return ResponseResult.success(userService.login(login.getUsername(), login.getPassword()));
    }

    @PutMapping("/pwd")
    public ResponseResult changePwd(@RequestBody ChangePwdDto dto, HttpServletRequest request) {
        if (!userService.changePwd(request, dto.getOld(), dto.getPassword())) throw CustomException.operationFailed();
        return ResponseResult.SUCCESS;
    }

    @GetMapping("/refresh")
    public ResponseResult refreshToken(HttpServletRequest request) {
        User user = tokenService.parserToken(tokenService.getToken(request));
        if (user == null) throw CustomException.unauthorized();
        return ResponseResult.success(tokenService.createToken(user));
    }
}
