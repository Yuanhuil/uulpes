package heracles.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class SimpleDateFormat {
    public static String DATE_PATTEN = "yyyy-MM-dd";
    public static String DATE_PATTEN_TM = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_PATTEN_TM_1 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static String DATE_PATTEN_ZH = "yyyy年MM月dd日";
    public static String DATETM_PATTEN_ZH = "yyyy年MM月dd日 HH点mm分ss秒";
    public static String DATETM_PATTEN_ZH1 = "yyyy年MM月dd日 HH点mm分";
    public static String TM_PATTEN_ZH = "HH点mm分";

    public static String formatDateTime(Date date) {
        String s = DateFormatUtils.format(date, DATE_PATTEN_TM);
        return s;
    }

    public static String format(Date date) {
        String s = DateFormatUtils.format(date, DATE_PATTEN);
        return s;
    }

    public static String formatZH(Date date) {
        String s = DateFormatUtils.format(date, DATE_PATTEN_ZH);
        return s;
    }

    public static String formatZH1(Date date) {
        String s = DateFormatUtils.format(date, DATETM_PATTEN_ZH1);
        return s;
    }

    public static String formatZH2(Date date) {
        String s = DateFormatUtils.format(date, TM_PATTEN_ZH);
        return s;
    }

    public static String format(Date date, String patten) {
        String s = DateFormatUtils.format(date, patten);
        return s;
    }

    public static String format(long time, String patten) {
        String s = DateFormatUtils.format(new Date(time), patten);
        return s;
    }

    public static Date strToDate(String str) {
        try {
            Date date = DateUtils.parseDate(str,
                    new String[] { DATE_PATTEN, DATE_PATTEN_ZH, DATE_PATTEN_TM, DATE_PATTEN_TM_1 });
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public static Date getDayStart(Date date) {
        return getDayStart(date, 0);
    }

    public static Date getDayStart(Date date, int daysLater) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(date);
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), tempCal.get(Calendar.DAY_OF_MONTH), 0, 0,
                0);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return tempCal.getTime();
    }

    public static Date getDayEnd(Date date) {
        return getDayEnd(date, 0);
    }

    public static Date getDayEnd(Date date, int daysLater) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(date);
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), tempCal.get(Calendar.DAY_OF_MONTH), 23, 59,
                59);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return tempCal.getTime();
    }

}
