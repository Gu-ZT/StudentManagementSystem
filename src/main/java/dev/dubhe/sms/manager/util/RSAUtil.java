package dev.dubhe.sms.manager.util;

import dev.dubhe.sms.manager.exception.CustomException;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.File;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@Slf4j
public class RSAUtil {
    private static final String RSA_KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private static String PUBLIC_KEY = null;
    private static String PRIVATE_KEY = null;

    public static String getPublicKey() {
        if (RSAUtil.PUBLIC_KEY != null) return RSAUtil.PUBLIC_KEY;
        RSAUtil.readOrCreateKey();
        return RSAUtil.PUBLIC_KEY;
    }

    public static String getPrivateKey() {
        if (RSAUtil.PRIVATE_KEY != null) return RSAUtil.PRIVATE_KEY;
        RSAUtil.readOrCreateKey();
        return RSAUtil.PRIVATE_KEY;
    }

    private static void readOrCreateKey() {
        File publicKey = new File("public.pem");
        File privateKey = new File("private.pem");
        RSAUtil.PUBLIC_KEY = FileUtil.readFile(publicKey);
        RSAUtil.PRIVATE_KEY = FileUtil.readFile(privateKey);
        if (RSAUtil.PUBLIC_KEY == null || RSAUtil.PRIVATE_KEY == null) {
            Map.Entry<String, String> key = generateKey();
            RSAUtil.PUBLIC_KEY = RSAUtil.encoderPublicKey(key.getKey());
            RSAUtil.PRIVATE_KEY = RSAUtil.encoderPrivateKey(key.getValue());
            FileUtil.writeFile(privateKey, RSAUtil.PRIVATE_KEY);
            FileUtil.writeFile(publicKey, RSAUtil.PUBLIC_KEY);
        }
        RSAUtil.PUBLIC_KEY = RSAUtil.decoderPublicKey(RSAUtil.PUBLIC_KEY);
        RSAUtil.PRIVATE_KEY = RSAUtil.decoderPrivateKey(RSAUtil.PRIVATE_KEY);
    }

    private static final String PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_KEY_END = "-----END PUBLIC KEY-----";
    private static final String PRIVATE_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----";
    private static final String PRIVATE_KEY_END = "-----END PRIVATE KEY-----";

    public static @Nonnull String encoderPublicKey(@Nonnull String key) {
        StringBuilder sb = new StringBuilder(PUBLIC_KEY_BEGIN);
        for (int i = 0; i < key.length(); i++) {
            if (i % 64 == 0) sb.append('\n');
            sb.append(key.charAt(i));
        }
        if (sb.charAt(sb.length() - 1) != '\n') sb.append('\n');
        sb.append(PUBLIC_KEY_END);
        return sb.toString();
    }

    public static @Nonnull String decoderPublicKey(@Nonnull String key) {
        return key.replace("\n", "").replace(PUBLIC_KEY_BEGIN, "").replace(PUBLIC_KEY_END, "");
    }

    public static @Nonnull String encoderPrivateKey(@Nonnull String key) {
        StringBuilder sb = new StringBuilder(PRIVATE_KEY_BEGIN);
        for (int i = 0; i < key.length(); i++) {
            if (i % 64 == 0) sb.append('\n');
            sb.append(key.charAt(i));
        }
        if (sb.charAt(sb.length() - 1) != '\n') sb.append('\n');
        sb.append(PRIVATE_KEY_END);
        return sb.toString();
    }

    public static @Nonnull String decoderPrivateKey(@Nonnull String key) {
        return key.replace("\n", "").replace(PRIVATE_KEY_BEGIN, "").replace(PRIVATE_KEY_END, "");
    }

    public static @Nonnull Map.Entry<String, String> generateKey() {
        KeyPairGenerator keygen;
        try {
            keygen = KeyPairGenerator.getInstance(RSA_KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw CustomException.error("RSA初始化密钥出现错误,算法异常");
        }
        SecureRandom secrand = new SecureRandom();
        //初始化随机产生器
        secrand.setSeed("Alian".getBytes());
        //初始化密钥生成器
        keygen.initialize(KEY_SIZE, secrand);
        KeyPair keyPair = keygen.genKeyPair();
        //获取公钥并转成base64编码
        byte[] pub_key = keyPair.getPublic().getEncoded();
        String publicKeyStr = Base64.getEncoder().encodeToString(pub_key);
        //获取私钥并转成base64编码
        byte[] pri_key = keyPair.getPrivate().getEncoded();
        String privateKeyStr = Base64.getEncoder().encodeToString(pri_key);
        return Map.entry(publicKeyStr, privateKeyStr);
    }

    /**
     * 公钥加密
     *
     * @param data 加密前的字符串
     * @return base64编码后的字符串
     */
    public static @Nonnull String encryptByPublicKey(@Nonnull String data, String key) {
        try {
            //Java原生base64解码
            byte[] pubKey = Base64.getDecoder().decode(key);
            //创建X509编码密钥规范
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
            //返回转换指定算法的KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            //根据X509编码密钥规范产生公钥对象
            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
            //根据转换的名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            //用公钥初始化此Cipher对象（加密模式）
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //对数据加密
            byte[] encrypt = cipher.doFinal(data.getBytes());
            //返回base64编码后的字符串
            return Base64.getEncoder().encodeToString(encrypt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw CustomException.error(e);
        }
    }

    /**
     * 公钥加密
     *
     * @param data 加密前的字符串
     * @return base64编码后的字符串
     */
    public static @Nonnull String encryptByPublicKey(@Nonnull String data) {
        return RSAUtil.encryptByPublicKey(data, RSAUtil.getPublicKey());
    }

    /**
     * 私钥加密
     *
     * @param data 加密前的字符串
     * @return base64编码后后的字符串
     */
    public static @Nonnull String encryptByPrivateKey(@Nonnull String data) {
        try {
            //Java原生base64解码
            byte[] priKey = Base64.getDecoder().decode(RSAUtil.getPrivateKey());
            //创建PKCS8编码密钥规范
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
            //返回转换指定算法的KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            //根据PKCS8编码密钥规范产生私钥对象
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            //根据转换的名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            //用私钥初始化此Cipher对象（加密模式）
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            //对数据加密
            byte[] encrypt = cipher.doFinal(data.getBytes());
            //返回base64编码后的字符串
            return Base64.getEncoder().encodeToString(encrypt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw CustomException.error(e);
        }
    }

    /**
     * 公钥解密
     *
     * @param data 解密前的字符串
     * @return 解密后的字符串
     */
    public static @Nonnull String decryptByPublicKey(@Nonnull String data) {
        try {
            //Java原生base64解码
            byte[] pubKey = Base64.getDecoder().decode(RSAUtil.getPublicKey());
            //创建X509编码密钥规范
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
            //返回转换指定算法的KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            //根据X509编码密钥规范产生公钥对象
            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
            //根据转换的名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            //用公钥初始化此Cipher对象（解密模式）
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            //对数据解密
            byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(data));
            //返回字符串
            return new String(decrypt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw CustomException.error(e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data 解密前的字符串
     * @return 解密后的字符串
     */
    public static @Nonnull String decryptByPrivateKey(@Nonnull String data) {
        try {
            //Java原生base64解码
            byte[] priKey = Base64.getDecoder().decode(RSAUtil.getPrivateKey());
            //创建PKCS8编码密钥规范
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
            //返回转换指定算法的KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
            //根据PKCS8编码密钥规范产生私钥对象
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            //根据转换的名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            //用私钥初始化此Cipher对象（解密模式）
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //对数据解密
            byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(data));
            //返回字符串
            return new String(decrypt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw CustomException.error(e);
        }
    }
}
