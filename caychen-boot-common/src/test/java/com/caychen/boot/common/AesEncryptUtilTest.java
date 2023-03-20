package com.caychen.boot.common;

import com.caychen.boot.common.utils.AesEncryptUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 11:45
 * @Description:
 */
public class AesEncryptUtilTest {

    @Test
    public void test() throws Exception {
        // 需要加密的字串
        String cSrc = "今天是周二，我好开心";
        System.out.println(cSrc);
        // 加密
        String enString = AesEncryptUtil.aesEncrypt(cSrc);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = AesEncryptUtil.aesDecrypt(enString);
        System.out.println("解密后的字串是：" + DeString);

        String key = "zhaoyanjunzhaoy1";

        String iv = "1234567890123456";

        String s = AesEncryptUtil.aesEncrypt(cSrc, key, iv);
        System.out.println(s);

        String s1 = AesEncryptUtil.aesDecrypt(s, key, iv);
        System.out.println(s1);
    }
}