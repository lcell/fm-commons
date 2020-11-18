package com.github.saleson.fm.commons.security;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @author saleson
 * @date 2020-11-15 16:33
 */
@Slf4j
public class DESUtils {
    private static final Base64 BASE64 = new Base64();

    private static final String CHARSET_UTF8 = "UTF-8";

    public static String getKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(56);
            // 生成一个Key
            SecretKey generateKey = keyGenerator.generateKey();
            // 转变为字节数组
            byte[] encoded = generateKey.getEncoded();
            // 生成密钥字符串
            String encodeHexString = Hex.encodeHexString(encoded);
            return encodeHexString;
        } catch (Exception e) {
            e.printStackTrace();
            return "密钥生成错误.";
        }
    }


    /**
     * 加密
     *
     * @param data
     * @return
     */
    public static String encryptBeforeURLEncoder(String data, String desKey) {
        try {
            return encrypt(URLEncoder.encode(data, CHARSET_UTF8), desKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密，再解码
     *
     * @param data
     * @return
     */
    public static String decryptAftorURLDecoder(String data, String desKey) {
        try {
            return URLDecoder.decode(decrypt(data, desKey), CHARSET_UTF8);
        } catch (Exception ex) {
            log.error("解密异常{}", data);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String message, String key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(CHARSET_UTF8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(CHARSET_UTF8));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return encodeBase64(cipher.doFinal(message.getBytes(CHARSET_UTF8)));
    }

    /**
     * 加密
     *
     * @param desKey
     * @param encryptText
     * @return
     * @throws Exception
     */
    public static String encryptSecureRandom(String desKey, String encryptText) throws GeneralSecurityException, UnsupportedEncodingException {
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = desKey.getBytes();
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        javax.crypto.SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(1, key, sr);
        byte data[] = encryptText.getBytes("UTF-8");
        byte encryptedData[] = cipher.doFinal(data);
        return encodeBase64(encryptedData);
    }

    /**
     * 解密
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String message, String key) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] bytesrc = decodeBase64(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(CHARSET_UTF8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(CHARSET_UTF8));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    /**
     * 编码
     *
     * @param binaryData
     * @return
     */
    private static String encodeBase64(byte[] binaryData) {
        return new String(BASE64.encode(binaryData));
    }

    /**
     * 解码
     *
     * @param base64String
     * @return
     */
    private static byte[] decodeBase64(String base64String) {
        return BASE64.decode(base64String);
    }
}
