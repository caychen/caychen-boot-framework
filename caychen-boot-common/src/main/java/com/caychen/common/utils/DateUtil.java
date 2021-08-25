package com.caychen.common.utils;

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
import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 15:49
 * @Description:
 */
public class DateUtil {

    /**
     * 获取当天最小时间，返回值格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static LocalDateTime minTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    /**
     * 获取当天最大时间，返回值格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static LocalDateTime maxTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
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
