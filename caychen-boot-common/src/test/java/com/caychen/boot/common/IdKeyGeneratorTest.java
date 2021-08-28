package com.caychen.boot.common;

import com.caychen.boot.common.config.id.IdKeyGenerator;
import org.junit.jupiter.api.Test;

/**
 * @Author: Caychen
 * @Date: 2021/8/25 12:51
 * @Description:
 */
public class IdKeyGeneratorTest {

    @Test
    public void testNextId() {
        IdKeyGenerator idKeyGenerator = new IdKeyGenerator(1, 1);
        Long nextId = idKeyGenerator.nextId();
        System.out.println(nextId);
    }


}