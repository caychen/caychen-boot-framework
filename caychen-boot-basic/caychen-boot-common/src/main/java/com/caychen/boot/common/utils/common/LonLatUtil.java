package com.caychen.boot.common.utils.common;

import com.caychen.boot.common.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 校验经纬度
 */
public class LonLatUtil {
    protected static final Logger logger = LoggerFactory.getLogger(LonLatUtil.class);
    /**
     * 地球半径 计算经纬度之间的距离时候使用
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 经纬度间的距离 返回米
     */
    public static double getDistance(double maxLat, double maxLng, double minLat, double minLng) {
        double radLat1 = rad(maxLat);
        double radLat2 = rad(minLat);
        double a = radLat1 - radLat2;
        double b = rad(maxLng) - rad(minLng);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        logger.info("========getDistance ：" + s * 1000);
        return s * 1000;
    }

    /**
     * 经纬度转换（Long->Double）
     *
     * @param longValue 经度或纬度的整数形式，如"39901308"
     * @return 经度或纬度的小数形式，如"39.901308"
     */
    public static String convertLonLat(String longValue) {
        if (StringUtils.isEmpty(longValue)) {
            return "";
        }
        try {
            return new BigDecimal(longValue).divide(new BigDecimal(1_000_000), 6, RoundingMode.HALF_UP).toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 经纬度转换（Long->Double）
     *
     * @param longValue 经度或纬度的整数形式，如"39901308"
     * @return 经度或纬度的小数形式，如"39.901308"
     */
    public static Double convertLonLat(Long longValue) {
        if (longValue == null) {
            return null;
        }
        try {
            return new BigDecimal(longValue).divide(new BigDecimal(1_000_000), 6, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            return null;
        }
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
