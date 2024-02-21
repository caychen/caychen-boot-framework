package com.caychen.boot.common.utils.crypto;

import com.caychen.boot.common.constant.CommonConstant;
import com.caychen.boot.common.utils.lang.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: Caychen
 * @Date: 2021/8/26 19:52
 * @Description:
 */
@Slf4j
public class AesEncryptUtil {

    //秘钥
    private static final byte[] DEFAULT_RAW_KEY = Base64.decodeBase64(CommonConstant.ORIGIN_ENCRYPT_STR);

    public static String aesEncrypt(String sSrc) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(DEFAULT_RAW_KEY, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return new Base64().encodeToString(encrypted);
    }

    public static String aesDecrypt(String sSrc) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(DEFAULT_RAW_KEY, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        //先用base64解密
        byte[] encrypted = new Base64().decode(sSrc);
        try {
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, "utf-8");
        } catch (Exception e) {
            log.error("AES解密失败：", e);
            return null;
        }

    }

    public static String aesEncrypt(String data, String key, String iv) throws Exception {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
            return null;
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), new IvParameterSpec(iv.getBytes()));
        byte[] bytes = cipher.doFinal(data.getBytes());

        // Base64加密
        return new Base64().encodeToString(bytes);
    }

    /**
     * AES/CBC/PKCS5Padding 解密
     *
     * @param data 待解密内容(Base64加密串)
     * @param key  密钥
     * @param iv   向量
     * @return 解密后的值
     */
    public static String aesDecrypt(String data, String key, String iv) throws Exception {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
            return null;
        }

        byte[] encrypted = new Base64().decode(data);

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));


        byte[] decData = cipher.doFinal(encrypted);

        return new String(decData, "utf-8");
    }

}
