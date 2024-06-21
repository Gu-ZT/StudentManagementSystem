package dev.dubhe.sms.manager.controller;

import dev.dubhe.sms.manager.data.ResponseResult;
import dev.dubhe.sms.manager.exception.CustomException;
import dev.dubhe.sms.manager.service.IFileService;
import dev.dubhe.sms.manager.util.DataUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "*")
public class FileController {
    private final IFileService fileService;

    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("avatar") MultipartFile file) {
        String filename = DataUtil.validate(file.getOriginalFilename(), CustomException::illegalFile);
        String id = this.fileService.upload(filename, file);
        return ResponseResult.success(id);
    }

    @GetMapping("/get/{id}")
    public void get(@PathVariable Long id, HttpServletResponse response) throws IOException {
        this.fileService.get(id,response);
    }
}
