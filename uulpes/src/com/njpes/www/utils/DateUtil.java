package com.njpes.www.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    /**
     * 日期操作函数
     * 
     * @param date
     *            日期
     * @param field
     *            字段calendar.DATE
     * @param dates
     *            值
     * @return
     */
    public static Date dateAdd(Date date, int field, int dates) {
        if (date == null) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(field, dates);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();
        return date;
    }

    public static void main(String[] args) {
        Date d = new Date();
        for (int i = 0; i < 20; i++) {
            d = DateUtil.dateAdd(d, Calendar.DATE, 1);
            System.out.println(d.toLocaleString());
        }

    }

    public static String SimpleDateFormat(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null || pattern.equals("")) {
            pattern = "yyyy-MM-dd";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
