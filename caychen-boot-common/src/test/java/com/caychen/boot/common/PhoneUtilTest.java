package com.caychen.boot.common;

import com.caychen.boot.common.utils.common.PhoneUtil;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 11:13
 * @Description:
 */
public class PhoneUtilTest {

    @Test
    @Order(1)
    public void testPhoneCity() {
        String cn = PhoneUtil.getCity("15221795280", "CN", Locale.CHINA);
        System.out.println(cn);
    }

    @Test
    @Order(2)
    public void testPhoneCarrier() {
        String cn = PhoneUtil.getCarrier("15221795280", "CN", Locale.CHINA);
        System.out.println(cn);
    }

    @Test
    @Order(3)
    public void testTimeZone() {
        List<String> timeZone = PhoneUtil.getTimeZone("15221795280", "CN");
        System.out.println(timeZone);
    }
}
