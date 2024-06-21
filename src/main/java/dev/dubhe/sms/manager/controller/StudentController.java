package dev.dubhe.sms.manager.controller;

import dev.dubhe.sms.manager.data.ResponseResult;
import dev.dubhe.sms.manager.data.dto.ChangeStudentDto;
import dev.dubhe.sms.manager.data.dto.CreateStudentDto;
import dev.dubhe.sms.manager.data.pojo.User;
import dev.dubhe.sms.manager.exception.CustomException;
import dev.dubhe.sms.manager.service.IStudentService;
import dev.dubhe.sms.manager.service.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*")
public class StudentController {
    private final IStudentService studentService;
    private final ITokenService tokenService;

    @PostMapping("/add")
    public ResponseResult create(@RequestBody CreateStudentDto dto, HttpServletRequest request) {
        User user = tokenService.parserToken(tokenService.getToken(request));
        if (user == null) throw CustomException.unauthorized();
        if (!studentService.createStudent(
            dto.getStudentId(),
            dto.getName(),
            dto.getSex(),
            dto.getBirth(),
            dto.getQq(),
            dto.getPhone(),
            dto.getAddress(),
            dto.getImage()
        )) throw CustomException.operationFailed();
        return ResponseResult.SUCCESS;
    }

    @PutMapping("/change")
    public ResponseResult change(@RequestBody ChangeStudentDto dto, HttpServletRequest request) {
        User user = tokenService.parserToken(tokenService.getToken(request));
        if (user == null) throw CustomException.unauthorized();
        if (!studentService.changeStudent(
            dto.getId(),
            dto.getStudentId(),
            dto.getName(),
            dto.getSex(),
            dto.getBirth(),
            dto.getQq(),
            dto.getPhone(),
            dto.getAddress(),
            dto.getImage()
        )) throw CustomException.operationFailed();
        return ResponseResult.SUCCESS;
    }

    @DeleteMapping("/del/{id}")
    public ResponseResult remove(@PathVariable Long id, HttpServletRequest request) {
        User user = tokenService.parserToken(tokenService.getToken(request));
        if (user == null) throw CustomException.unauthorized();
        if (!studentService.removeStudent(id)) throw CustomException.operationFailed();
        return ResponseResult.SUCCESS;
    }

    @GetMapping("/get")
    public ResponseResult get(HttpServletRequest request) {
        User user = tokenService.parserToken(tokenService.getToken(request));
        if (user == null) throw CustomException.unauthorized();
        return ResponseResult.success(studentService.getStudents());
    }
}
