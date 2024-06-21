package dev.dubhe.sms.manager.service.impl;

import cn.hutool.core.util.IdUtil;
import dev.dubhe.sms.manager.data.dao.IFileMapDao;
import dev.dubhe.sms.manager.data.pojo.FileMap;
import dev.dubhe.sms.manager.exception.CustomException;
import dev.dubhe.sms.manager.service.IFileService;
import dev.dubhe.sms.manager.util.DataUtil;
import dev.dubhe.sms.manager.util.FileUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService {
    private final IFileMapDao fileMapDao;

    @Override
    public String upload(@Nonnull String filename, @Nonnull MultipartFile file) {
        String path = "./files";
        String uuid = IdUtil.fastUUID();
        int indexOf = filename.lastIndexOf(".");
        String storageName = uuid + filename.substring(indexOf);
        File desc = new File(path, storageName);
        try {
            if (!desc.getParentFile().exists() && !desc.getParentFile().mkdirs()) {
                log.error("无法创建目录");
                throw CustomException.uploadError();
            }
            file.transferTo(desc.toPath());
            FileMap fileMap = new FileMap(storageName, desc.getAbsolutePath());
            this.fileMapDao.save(fileMap);
            return fileMap.getId().toString();
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw CustomException.uploadError();
        }
    }

    public void get(Long id, @Nonnull HttpServletResponse response) throws IOException {
        FileMap file = DataUtil.validate(this.fileMapDao.getById(id), CustomException::fileNotExist);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        FileUtil.setAttachmentResponseHeader(response, file.getName());
        FileUtil.writeBytes(file.getAddr(), response.getOutputStream());
    }
}
