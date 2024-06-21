package dev.dubhe.sms.manager.service;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    String upload(String filename, MultipartFile file);
    void get(Long id, @Nonnull HttpServletResponse response) throws IOException;
}
