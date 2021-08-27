package com.caychen.common.utils;

import com.caychen.common.constant.CommonConstant;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: Caychen
 * @Date: 2021/8/26 19:52
 * @Description:
 */
public class AesEncryptUtil {

    //秘钥
    private static final byte[] raw = Base64.decodeBase64(CommonConstant.ORIGIN_ENCRYPT_STR);

    public static String Encrypt(String sSrc) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return new Base64().encodeToString(encrypted);
    }

    public static String Decrypt(String sSrc) throws Exception {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] encrypted1 = new Base64().decode(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original, "utf-8");
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}
