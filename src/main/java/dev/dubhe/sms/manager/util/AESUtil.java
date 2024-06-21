package dev.dubhe.sms.manager.util;

import dev.dubhe.sms.manager.exception.CustomException;
import jakarta.annotation.Nonnull;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {
    private static final String ALGORITHM = "AES";

    public static @Nonnull String encrypt(@Nonnull String data, @Nonnull String key) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] keys = md.digest(key.getBytes(StandardCharsets.UTF_8));
            keyGenerator.init(new SecureRandom(keys));
            SecretKey secretKey = new SecretKeySpec(keys, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public static @Nonnull String decrypt(@Nonnull String encryptedData, @Nonnull String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] keys = md.digest(key.getBytes(StandardCharsets.UTF_8));
            SecretKey secretKey = new SecretKeySpec(keys, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] originalData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(originalData);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
}
