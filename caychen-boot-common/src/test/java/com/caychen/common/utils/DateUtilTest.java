package com.caychen.common.utils;

import com.caychen.common.constant.DateConstant;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 17:00
 * @Description:
 */
class DateUtilTest {

    @Test
    void formatDate() {
        String s = DateUtil.formatDate(new Date(), DateConstant.YYYY_MM_DD_SLASH);
        System.out.println(s);
    }
}