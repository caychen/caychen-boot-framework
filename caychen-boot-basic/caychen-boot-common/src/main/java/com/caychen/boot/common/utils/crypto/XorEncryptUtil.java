package com.caychen.boot.common.utils.crypto;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Caychen
 * @Date: 2024/3/13 15:38
 * @Description:
 */
public class XorEncryptUtil {

    public static String xorEncrypt(String text, String key) throws UnsupportedEncodingException {
        byte[] keyarr = key.getBytes(StandardCharsets.UTF_8);
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);

        byte[] encryptedBytes = new byte[textBytes.length];

        for (int i = 0; i < textBytes.length; ++i) {
            encryptedBytes[i] = (byte) (textBytes[i] ^ keyarr[i % keyarr.length]);
        }

        return bytesToHex(encryptedBytes); // 将加密后的字节数组转换为十六进制字符串返回
    }

    // 辅助方法：将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            String hexStr = Integer.toHexString(aByte & 0xff);
            if (hexStr.length() < 2) {
                hexStr = "0" + hexStr;
            }
            result.append(hexStr);
        }
        return result.toString();
    }
}
