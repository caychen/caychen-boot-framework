package com.caychen.common;

import com.caychen.common.utils.AesEncryptUtil;
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
        String cSrc = "test_data";
        System.out.println(cSrc);
        // 加密
        String enString = AesEncryptUtil.Encrypt(cSrc);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = AesEncryptUtil.Decrypt(enString);
        System.out.println("解密后的字串是：" + DeString);
    }
}