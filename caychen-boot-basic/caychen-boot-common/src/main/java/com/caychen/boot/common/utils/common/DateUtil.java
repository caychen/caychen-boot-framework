package com.caychen.boot.common.utils.common;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.caychen.boot.common.constant.DateConstant;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 15:49
 * @Description:
 */
public class DateUtil {

    public static final int CURRENT = 0;
    public static final int NEXT = 1;
    public static final int PREVIOUS = -1;
    public static final int QUARTER = 3;

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
     * @return
     */
    public static String formatDate(Long timestamp) {
        return formatDate(timestamp, DateConstant.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * format Date
     *
     * @param timestamp 毫秒
     * @param pattern   格式
     * @return
     */
    public static String formatDate(Long timestamp, String pattern) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String formatDate(Date date) {
        return formatDate(date, DateConstant.YYYY_MM_DD_HH_MM_SS);
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

    public static String formatLocalDate(LocalDate localDate, String pattern) {
        return formatDate(asDate(localDate), pattern);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return formatDate(asDate(localDateTime), pattern);
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

    /**
     * 获取当前日期的开始时间
     *
     * @param zoneId 时间偏移量
     * @return
     */
    public static LocalDateTime todayStart(ZoneId zoneId) {
        return startOfDay(0, zoneId);
    }

    /**
     * 获取当前的ZoneDateTime
     *
     * @param zoneId 时区偏移量
     * @return
     */
    public static ZonedDateTime now(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId);
    }

    /**
     * 获取当前日期的开始时间ZonedDateTime
     *
     * @param date   日期
     * @param zoneId 时区偏移量
     * @return
     */
    public static ZonedDateTime localDateToZoneDateTime(LocalDate date, ZoneId zoneId) {
        return date.atStartOfDay(zoneId);
    }

    /**
     * 获取当前日期的开始时间
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime startOfDay(ZonedDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.DAYS).toLocalDateTime();
    }

    /**
     * 获取今天后的指定天数的开始时间
     *
     * @param plusDays 当前多少天后
     * @param zoneId   时区偏移量
     * @return
     */
    public static LocalDateTime startOfDay(int plusDays, ZoneId zoneId) {
        return startOfDay(now(zoneId).plusDays(plusDays));
    }

    /**
     * 获取指定日期的后几个工作日的时间LocalDate
     *
     * @param date 指定日期
     * @param days 工作日数
     * @return
     */
    public static LocalDate plusWeekdays(LocalDate date, int days) {
        if (days == 0) {
            return date;
        }
        if (Math.abs(days) > 50) {
            throw new IllegalArgumentException("days must be less than 50");
        }
        int i = 0;
        int delta = days > 0 ? 1 : -1;
        while (i < Math.abs(days)) {
            date = date.plusDays(delta);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                i += 1;
            }
        }
        return date;
    }

    /**
     * 获取指定日期的后几个工作日的时间ZoneDateTime
     *
     * @param date
     * @param days
     * @return
     */
    public static ZonedDateTime plusWeekdays(ZonedDateTime date, int days) {
        return plusWeekdays(date.toLocalDate(), days).atStartOfDay(date.getZone());
    }

    /**
     * 获取当前月份的第一天的时间ZoneDateTime
     *
     * @param zoneId
     * @return
     */
    public static ZonedDateTime firstDayOfMonth(ZoneId zoneId) {
        return now(zoneId).withDayOfMonth(1);
    }

    /**
     * 将Date转成指定时区的Date
     *
     * @param date
     * @return
     */
    public static Date dateToDate(Date date, ZoneId zoneId) {
        LocalDateTime dt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return toDate(ZonedDateTime.of(dt, zoneId));
    }

    /**
     * 将LocalDate转成Date
     *
     * @param date
     * @return
     */
    public static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * ZonedDateTime 转换成Date
     *
     * @param dateTime
     * @return
     */
    public static Date toDate(ZonedDateTime dateTime) {
        return Date.from(dateTime.toInstant());
    }

    /**
     * String 转换成 Date
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String date, String format, ZoneId zoneId) throws ParseException {
        DateTimeFormatter formatter = getDateTimeFormatter(format, zoneId);
        Instant instant = Instant.from(formatter.parse(date));
        return Date.from(instant);
    }

    private static DateTimeFormatter getDateTimeFormatter(String format, ZoneId zoneId) {
        return DateTimeFormatter.ofPattern(format).withZone(zoneId);
    }

    /**
     * 将Date转成相应的时区的localDate
     *
     * @param date
     * @param zoneId
     * @return
     */
    public static LocalDate toLocalDate(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId).toLocalDate();
    }

    /**
     * 将Instant转成指定时区偏移量的localDate
     *
     * @param instant
     * @param zoneId
     * @return
     */
    public static LocalDate toLocalDate(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 将Instant转换成指定时区偏移量的localDateTime
     *
     * @param instant
     * @param zoneId
     * @return
     */
    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 将Instant转成系统默认时区偏移量的LocalDateTime
     *
     * @param instant
     * @return
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return toLocalDateTime(instant, ZoneId.systemDefault());
    }

    /**
     * 将ZoneDateTime 转成 指定时区偏移量的LocalDateTime
     *
     * @param zonedDateTime 时间
     * @param zoneId        指定时区偏移量
     * @return
     */
    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime, ZoneId zoneId) {
        return zonedDateTime.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * 将ZoneDateTime 转成 LocalDateTime
     *
     * @param zonedDateTime
     * @return
     */
    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * String 转成 ZoneDateTime
     * 需要类似 yyyy-MM-dd HH:mm:ss 需要日期和时间信息完整信息
     *
     * @param date
     * @param format
     * @param zoneId
     * @return
     */
    public static ZonedDateTime stringToZoneDateTime(String date, String format, ZoneId zoneId) {
        DateTimeFormatter formatter = getDateTimeFormatter(format, zoneId);
        return ZonedDateTime.parse(date, formatter);
    }

    /**
     * 将时间戳long转成ZonedDateTime
     *
     * @param timeStamp
     * @param zoneId
     * @return
     */
    public static ZonedDateTime longToZoneDateTime(long timeStamp, ZoneId zoneId) {
        return ZonedDateTime.from(Instant.ofEpochMilli(timeStamp).atZone(zoneId));
    }

    /**
     * 两个时区的zoneDateTime相互转换
     *
     * @param zonedDateTime 需要转换的如期
     * @param zoneId        转换成的ZoneDateTime的时区偏移量
     * @return
     */
    public static ZonedDateTime zonedDateTimeToZoneDateTime(ZonedDateTime zonedDateTime, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(zonedDateTime.toInstant(), zoneId);
    }

    /**
     * Date 转成 指定时区偏移量的ZoneDateTime
     *
     * @param date
     * @param zoneId
     * @return
     */
    public static ZonedDateTime toZonedDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId);
    }

    /**
     * LocaldateTime 转成 指定时区偏移量的ZonedDateTime
     *
     * @param localDateTime 本地时间
     * @param zoneId        转成ZonedDateTime的时区偏移量
     * @return
     */
    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.atZone(zoneId);
    }

    /**
     * Date装换成String
     *
     * @param date   时间
     * @param format 转化格式
     * @return
     */
    public static String dateToString(Date date, String format, ZoneId zoneId) {
        DateTimeFormatter formatter = getDateTimeFormatter(format, zoneId);
        return formatter.format(date.toInstant());
    }

    /**
     * ZoneDateTime 转换成 String
     *
     * @param dateTime
     * @param zoneId   localDateTime所属时区
     * @return
     */
    public static String zoneDateTimeToString(ZonedDateTime dateTime, String format, ZoneId zoneId) {
        DateTimeFormatter formatter = getDateTimeFormatter(format, zoneId);
        return dateTime.format(formatter);
    }

    /**
     * LocalDateTime 转成 String
     *
     * @param localDateTime
     * @param format
     * @return
     */
    public static String localDateTimeToString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    /**
     * 将ZonedDateTime转成时间戳long
     *
     * @return
     * @parm zonedDateTime
     */
    public static long zoneDateTimeToLong(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 将LocalDateTime转成时间戳long
     *
     * @param localDateTime
     * @param zoneId
     * @return
     */
    public static long toLong(LocalDateTime localDateTime, ZoneId zoneId) {
        return zoneDateTimeToLong(localDateTime.atZone(zoneId));
    }

    /**
     * 获取某天的开始日期
     *
     * @param offset 0今天，1明天，-1昨天，依次类推
     * @return
     */
    public static LocalDateTime dayStart(int offset) {

        return LocalDateTime.of(LocalDate.now().plusDays(offset), LocalTime.MIN);
    }

    /**
     * 获取某周的开始日期
     *
     * @param offset 0本周，1下周，-1上周，依次类推
     * @return
     */
    public static LocalDateTime weekStart(int offset) {
        return LocalDateTime.now().plusWeeks(offset).with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取某月的开始日期
     *
     * @param offset 0本月，1下个月，-1上个月，依次类推
     * @return
     */
    public static LocalDateTime monthStart(int offset) {
        return LocalDateTime.now().plusMonths(offset).with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }


    /**
     * 获取某季度的开始日期
     * 季度一年四季， 第一季度：2月-4月， 第二季度：5月-7月， 第三季度：8月-10月， 第四季度：11月-1月
     *
     * @param offset 0本季度，1下个季度，-1上个季度，依次类推
     * @return
     */
    public static LocalDateTime quarterStart(int offset) {
        final LocalDateTime date = LocalDateTime.now().plusMonths(offset * 3);
        int month = date.getMonth().getValue();//当月
        int start = 0;
        if (month >= 2 && month <= 4) {//第一季度
            start = 2;
        } else if (month >= 5 && month <= 7) {//第二季度
            start = 5;
        } else if (month >= 8 && month <= 10) {//第三季度
            start = 8;
        } else if ((month >= 11 && month <= 12)) {//第四季度
            start = 11;
        } else if (month == 1) {//第四季度
            start = 11;
            month = 13;
        }
        return date.plusMonths(start - month).with(TemporalAdjusters.firstDayOfMonth()).minusDays(NEXT);
    }

    /**
     * 获取某年的开始日期
     *
     * @param offset 0今年，1明年，-1去年，依次类推
     * @return
     */
    public static LocalDateTime yearStart(int offset) {
        return LocalDateTime.now().plusYears(offset).with(TemporalAdjusters.firstDayOfYear());
    }


    /**
     * 获取本周的第一天或最后一天
     *
     * @param : [today, isFirst: true 表示开始时间，false表示结束时间]
     * @return
     */
    public static String getStartOrEndDayOfWeek(LocalDate today, Boolean isFirst) {
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        DayOfWeek week = today.getDayOfWeek();
        int value = week.getValue();
        if (isFirst) {
            resDate = today.minusDays(value - 1);
        } else {
            resDate = today.plusDays(7 - value);
        }
        return resDate.toString();
    }

    /**
     * 获取本季度的第一天或最后一天
     *
     * @param : [today, isFirst: true 表示开始时间，false表示结束时间]
     * @return
     */
    public static String getStartOrEndDayOfQuarter(LocalDate today, Boolean isFirst) {
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        if (isFirst) {
            resDate = LocalDate.of(today.getYear(), firstMonthOfQuarter, 1);
        } else {
            resDate = LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear()));
        }
        return resDate.toString();
    }

    /**
     * 获取本年的第一天或最后一天
     *
     * @param : [today, isFirst: true 表示开始时间，false表示结束时间]
     * @return
     */
    public static String getStartOrEndDayOfYear(LocalDate today, Boolean isFirst) {
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        if (isFirst) {
            resDate = LocalDate.of(today.getYear(), Month.JANUARY, 1);
        } else {
            resDate = LocalDate.of(today.getYear(), Month.DECEMBER, Month.DECEMBER.length(today.isLeapYear()));
        }
        return resDate.toString();
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


}
