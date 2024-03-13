package com.caychen.boot.common.utils.crypto;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Caychen
 * @Date: 2024/3/13 15:41
 * @Description:
 */
public class XorDecryptUtil {

    public static String xorDecrypt(String textOld, String key) throws UnsupportedEncodingException {
        byte[] keyarr = key.getBytes(StandardCharsets.UTF_8);
        int length = textOld.length() / 2;
        byte[] resultBytes = new byte[length];

        String textNew;
        for (int index = 0; index < length; ++index) {
            textNew = textOld.substring(index * 2, index * 2 + 2);
            resultBytes[index] = Integer.valueOf(Integer.parseInt(textNew, 16)).byteValue();
        }

        byte[] text = new byte[length];

        for (int i = 0; i < resultBytes.length; ++i) {
            text[i] = (byte) (resultBytes[i] ^ keyarr[i % keyarr.length]);
        }

        textNew = new String(text, StandardCharsets.UTF_8);
        return textNew;
    }

}
