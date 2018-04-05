package com.cxd.rtcroom.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    public static String format(Date date, String pattern) {
        if (null == date) {
            return "null";
        }
        if (null == pattern || "".equals(pattern) || "null".equals(pattern)) {
            pattern = DEFAULT_FORMAT;
        }
        return new java.text.SimpleDateFormat(pattern).format(date);
    }

    public static Date format(String date) {
        return format(date, null);
    }

    public static Date format(String date, String pattern) {
        if (null == pattern || "".equals(pattern) || "null".equals(pattern)) {
            pattern = DEFAULT_FORMAT;
        }
        if (null == date || "".equals(date) || "null".equals(date)) {
            return new Date();
        }
        Date d = null;
        try {
            d = new java.text.SimpleDateFormat(pattern).parse(date);
        } catch (ParseException pe) {
        }
        return d;
    }

    public static String format(Long time, String pattern) {
        if (time == null) {
            return "null";
        }
        if (null == pattern || "".equals(pattern) || "null".equals(pattern)) {
            pattern = DEFAULT_FORMAT;
        }
        return new java.text.SimpleDateFormat(pattern).format(time);
    }

    public static String format(Long time) {

        return format(time, null);
    }

    public static Date addDay(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }

    public static Date addMonth(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }

    public static Date addYear(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, num);
        return calendar.getTime();
    }
}
