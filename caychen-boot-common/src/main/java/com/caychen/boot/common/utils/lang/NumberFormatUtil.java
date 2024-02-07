package com.caychen.boot.common.utils.lang;


import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 数字转换工具类
 *
 */

public class NumberFormatUtil {

    /**
     * 取小数点后N位
     *
     * @param data
     * @param num  位数（目前采用理想范围内的1-6位判断，临时使用）
     * @return 格式化数据
     */
    public static Double getDoubleValueData(Double data, Integer num) {
        try {
            return Double.valueOf(formatToString(data,num));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0d;
    }

    public static Float getFloatValueData(Double data, Integer num) {
        try {
            return Float.valueOf(formatToString(data,num));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0f;
    }

    public static String formatToString(Double data, Integer num) {
        java.text.DecimalFormat df = null;
        if (data == null) return "0";
        if (null != num) {
            if (num == 1) {
                df = new java.text.DecimalFormat("#0.0");
            } else if (num == 2) {
                df = new java.text.DecimalFormat("#0.00");
            } else if (num == 3) {
                df = new java.text.DecimalFormat("#0.000");
            } else if (num == 4) {
                df = new java.text.DecimalFormat("#0.0000");
            } else if (num == 5) {
                df = new java.text.DecimalFormat("#0.00000");
            } else if (num == 6) {
                df = new java.text.DecimalFormat("#0.000000");
            } else {
                df = new java.text.DecimalFormat("#0.0");
            }
        } else {
            df = new java.text.DecimalFormat("#0.0");
        }
        try {
            String d = data == null ? "0" : df.format(data) + "";
            return d;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 经纬度装换
     *
     * @param number
     * @return
     */
    public static double getLatitudeOrLongitude(int number) {
        return number * 0.000001;
    }

    /**
     * 剩余油量转换
     *
     * @param oilwear 位置云返回的油量百分比
     *                oilCapacity 邮箱容量
     * @return
     */
    public static double getOilwear(double oilwear, double oilCapacity) {
        return (oilwear * oilCapacity) / 10000;
    }

    /**
     * 整车里程and油量转换
     *
     * @param statusValue 待处理值
     * @return
     */
    public static double getStatusValue(long statusValue) {
        return statusValue * 0.01;
    }

    /**
     * 四舍五入保留小数位数
     */
    public static double formatNumber(double src, int validBit) {
        double result = src;
        try {
            result = new BigDecimal(src).setScale(validBit, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            // do nothing
        }
        return result;
    }


    /**
     *两个数相加
     * @param addend
     * @param augend
     * @return
     */
    public static BigDecimal digitalAdd(String addend, String augend) {
        return new BigDecimal(addend).add(new BigDecimal(augend)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     *两个数相减
     * @param minuend
     * @param subtrahend
     * @return
     */
    public static BigDecimal digitalSub(String minuend, String subtrahend) {
        return new BigDecimal(minuend).subtract(new BigDecimal(subtrahend)).setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    /**
     *两个数相乘
     * @param multiplicand
     * @param multiplier
     * @return
     */
    public static BigDecimal digitalMul(String multiplicand,String multiplier) {
        return new BigDecimal(multiplicand).multiply(new BigDecimal(multiplier)).setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    /**
     *两个数相除
     * @param LargeDivisor
     * @param divisor
     * @param scale
     * @return
     */
    public static BigDecimal digitalDiv(String LargeDivisor,String divisor,int scale) {
        return new BigDecimal(LargeDivisor).divide(new BigDecimal(divisor),scale,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * double转String(主要是将科学计数法转字符串)
     * @param value
     * @return
     */
    public static String parseString(Double value){
        if (value == null) {
            return "";
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return (nf.format(value));
    }
}