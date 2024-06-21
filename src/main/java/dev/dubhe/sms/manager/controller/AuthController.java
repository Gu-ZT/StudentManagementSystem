package dev.dubhe.sms.manager.controller;

import dev.dubhe.sms.manager.data.ResponseResult;
import dev.dubhe.sms.manager.data.dto.KeyDto;
import dev.dubhe.sms.manager.util.LoremIpsumUtil;
import dev.dubhe.sms.manager.util.RSAUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @GetMapping("/public")
    public ResponseResult getPublicKey() {
        return ResponseResult.success(RSAUtil.encoderPublicKey(RSAUtil.getPublicKey()));
    }

    @PostMapping("/test/get")
    public ResponseResult test(@RequestBody KeyDto dto) {
        return ResponseResult.success(RSAUtil.encryptByPublicKey(LoremIpsumUtil.generateLoremIpsum(14), RSAUtil.decoderPublicKey(dto.getKey().replace("\r", ""))));
    }
}
