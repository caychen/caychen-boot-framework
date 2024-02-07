package com.caychen.boot.common;

import com.caychen.boot.common.utils.crypto.MD5;
import org.junit.jupiter.api.Test;

/**
 * @Author: Caychen
 * @Date: 2021/8/25 12:56
 * @Description:
 */
public class Md5Test {

    @Test
    public void testEncrypt(){
        String encrypt = MD5.encrypt("caychen");
        System.out.println(encrypt);
    }
}
