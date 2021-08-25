package com.caychen.common;

import com.caychen.common.config.id.IdKeyGenerator;
import com.caychen.common.utils.MD5;
import org.junit.jupiter.api.Test;

/**
 * @Author: Caychen
 * @Date: 2021/8/25 12:51
 * @Description:
 */
public class IdKeyGeneratorTest {

    @Test
    public void testNextId(){
        IdKeyGenerator idKeyGenerator = new IdKeyGenerator(1, 1);
        Long nextId = idKeyGenerator.nextId();
        System.out.println(nextId);
    }

    @Test
    public void testEncrypt(){
        String encrypt = MD5.encrypt("caychen");
        System.out.println(encrypt);
    }
}