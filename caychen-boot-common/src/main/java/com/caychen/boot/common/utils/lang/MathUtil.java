package com.caychen.boot.common.utils.lang;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MathUtil {

    public static String floor(String str,int num){
        double s = Math.floor(Double.parseDouble(str) * Math.pow(10,num)) * (1 / Math.pow(10,num)) * 1d;
        return String.valueOf(s);
    }

    /**
     * 四舍五入保留小数位
     */
    public static BigDecimal scale(double org, int newScale) {
        return BigDecimal.valueOf(org).setScale(newScale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入保留小数位
     */
    public static BigDecimal scale(Float org, int newScale) {
        if(org == null){
            org = 0F;
        }
        return BigDecimal.valueOf(org).setScale(newScale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入保留小数位
     */
    public static BigDecimal scale(BigDecimal org, int newScale) {
        if(org == null){
            org = BigDecimal.ZERO;
        }
        return org.setScale(newScale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入保留小数位
     */
    public static BigDecimal scale(String org, int newScale) {
        if(StringUtils.isEmpty(org)){
            org = "0";
        }
        return BigDecimal.valueOf(Double.valueOf(org)).setScale(newScale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入保留小数位
     */
    public static List<BigDecimal> scaleBigDecimalList(List<BigDecimal> b, int newScale) {
        return b.stream().map(e -> e == null? BigDecimal.ZERO : e.setScale(newScale, BigDecimal.ROUND_HALF_UP))
                .collect(Collectors.toList());
    }

    /**
     * 四舍五入保留小数位
     */
    public static List<Float> scaleFloatList(List<Float> f, int newScale) {
        return f.stream().map(e -> e == null ? 0F : MathUtil.scale(e, newScale).floatValue())
                .collect(Collectors.toList());
    }

    /**
     * 四舍五入保留小数位
     */
    public static List<String> scaleStringList(List<String> s, int newScale) {
        return s.stream().map(e -> StringUtils.isBlank(e) ? "0" : MathUtil.scale(e, newScale).toString())
                .collect(Collectors.toList());
    }

    /**
     * 四舍五入保留小数位
     */
    public static List<Double> scaleDoubleList(List<Double> d, int newScale) {
        return d.stream().map(e -> e == null ? 0D : MathUtil.scale(e, newScale).doubleValue())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(scale(15.3333333D,2));
    }
}
