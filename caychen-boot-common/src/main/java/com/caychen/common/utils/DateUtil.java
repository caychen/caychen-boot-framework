package com.caychen.common.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.caychen.common.constant.DateConstant;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 15:49
 * @Description:
 */
public class DateUtil {

    public static Date minDateTime(Date date) {
        return new DateTime(date)
                .setField(DateField.HOUR_OF_DAY, 0)
                .setField(DateField.MINUTE, 0)
                .setField(DateField.SECOND, 0)
                .setField(DateField.MILLISECOND, 0)
                .toJdkDate();
    }

    public static Date maxDateTime(Date date) {
        return new DateTime(date)
                .setField(DateField.HOUR_OF_DAY, 23)
                .setField(DateField.MINUTE, 59)
                .setField(DateField.SECOND, 59)
                .setField(DateField.MILLISECOND, 0)
                .toJdkDate();
    }

    /**
     * 获取当天最小时间，返回值格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static LocalDateTime minTime(Date date) {
        if (date == null) {
            return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        } else {
            return asLocalDateTime(minDateTime(date));
        }
    }

    /**
     * 获取当天最大时间，返回值格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static LocalDateTime maxTime(Date date) {
        if (date == null) {
            return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        } else {
            return asLocalDateTime(maxDateTime(date));
        }
    }

    /**
     * LocalDate类型转Date类型
     *
     * @param localDate
     * @return
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime类型转Date类型
     *
     * @param localDateTime
     * @return
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date类型转LocalDate类型
     *
     * @param date
     * @return
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date类型转LocalDateTime类型
     *
     * @param date
     * @return
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 计算两个LocalDateTime的时间差
     *
     * @param start
     * @param end
     * @return Duration类型的时间差，可以通过toDays、toHours、toMinutes、etc...
     */
    public static Duration between(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end);
    }

    /**
     * 计算两个LocalDate的时间差
     *
     * @param start
     * @param end
     * @return Duration类型的时间差，可以通过toDays、toHours、toMinutes、etc...
     */
    public static Duration between(LocalDate start, LocalDate end) {
        return Duration.between(start, end);
    }

    /**
     * 计算两个Date的时间差
     *
     * @param start
     * @param end
     * @return Duration类型的时间差，可以通过toDays、toHours、toMinutes、etc...
     */
    public static Duration between(Date start, Date end) {
        return Duration.between(asLocalDateTime(start), asLocalDateTime(end));
    }

    /**
     * format Date
     *
     * @param timestamp 毫秒
     * @param pattern   格式
     * @return
     */
    public static String formatDate(long timestamp, String pattern) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String formatDate(Long date, String pattern) {
        return formatDate(new Date(date), pattern);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date parseDateTime(String dateStr) {
        return parseDateTime(dateStr, DateConstant.COMMON_DATE_FORMATS);
    }

    public static Date parseDateTime(String dateStr, String... patterns) {
        try {
            return DateUtils.parseDate(dateStr, patterns);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
