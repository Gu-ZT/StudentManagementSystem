package dev.dubhe.sms.manager.util;

import cn.hutool.core.codec.Base64Encoder;
import dev.dubhe.sms.manager.exception.CustomException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileUtil {
    public static @Nullable String readFile(@Nonnull File file) {
        if (!file.isFile() || !file.canRead()) return null;
        List<Character> buf = new ArrayList<>();
        char[] buffer = new char[1024];
        try (FileReader reader = new FileReader(file)) {
            int length;
            while ((length = reader.read(buffer)) > 0) {
                for (int i = 0; i < length; i++) {
                    buf.add(buffer[i]);
                }
            }
            byte[] result = new byte[buf.size()];
            for (int i = 0; i < buf.size(); i++) {
                result[i] = (byte) buf.get(i).charValue();
            }
            return new String(result, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static void writeFile(@Nonnull File file, @Nonnull String string) {
        try {
            if (!file.isFile() && !file.createNewFile()) throw CustomException.error();
            if (!file.canWrite()) throw CustomException.error();
            try (FileWriter writer = new FileWriter(file)) {
                byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
                char[] chars = new char[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    chars[i] = (char) bytes[i];
                }
                writer.write(chars);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw CustomException.error(e);
        }
    }

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    log.error("failed to close output stream", e1);
                }
            }
        }
    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(@Nonnull HttpServletResponse response, @Nonnull String realFileName) {
        String percentEncodedFileName = percentEncode(realFileName);

        String contentDispositionValue = "attachment; filename=" +
            percentEncodedFileName +
            ";" +
            "filename*=" +
            "utf-8''" +
            percentEncodedFileName;

        response.setHeader("Content-disposition", contentDispositionValue);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static @Nonnull String percentEncode(@Nonnull String s) {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8);
        return encode.replaceAll("\\+", "%20");
    }

    public static String fileToBase64(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] temp = new byte[1024 * 1024];
            int l;
            while ((l = is.read(temp)) != -1) {
                bos.write(temp, 0, l);
            }
            return Base64Encoder.encode(bos.toByteArray());
        } catch (IOException e) {
            log.error("文件转换base64失败!", e);
            throw e;
        }
    }
}
