package dev.dubhe.sms.manager.exception;

import cn.hutool.http.HttpStatus;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomException extends RuntimeException {
    private int code = HttpStatus.HTTP_INTERNAL_ERROR;

    public CustomException(String msg) {
        this(HttpStatus.HTTP_INTERNAL_ERROR, msg);
    }

    public CustomException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public static @Nonnull CustomException error() {
        return CustomException.error("系统错误");
    }

    public static @Nonnull CustomException error(String msg) {
        return new CustomException(msg);
    }

    public static @Nonnull CustomException error(Throwable throwable) {
        return new CustomException(throwable.getMessage());
    }

    public static @Nonnull CustomException unauthorized() {
        return new CustomException(HttpStatus.HTTP_UNAUTHORIZED, "用户不存在或登录过期");
    }

    public static @Nonnull CustomException loginFail() {
        return new CustomException(HttpStatus.HTTP_UNAUTHORIZED, "用户名或密码错误");
    }

    public static @Nonnull CustomException operationFailed() {
        return new CustomException(HttpStatus.HTTP_NOT_MODIFIED, "操作失败");
    }

    public static @Nonnull CustomException forbidden() {
        return new CustomException(HttpStatus.HTTP_FORBIDDEN, "你无权进行此操作！");
    }

    public static @Nonnull CustomException illegalFile() {
        return new CustomException(HttpStatus.HTTP_NOT_MODIFIED, "非法的文件！");
    }

    public static @Nonnull CustomException uploadError() {
        return new CustomException(HttpStatus.HTTP_NOT_MODIFIED, "文件上传失败！");
    }

    public static @Nonnull CustomException fileNotExist() {
        return new CustomException(HttpStatus.HTTP_NOT_FOUND, "文件不存在！");
    }
}
