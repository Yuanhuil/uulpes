package com.njpes.www.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.util.SchoolYearTerm;
import com.njpes.www.entity.workschedule.enums.SchoolTerm;

public class SchoolYearTermUtil {

    /**
     * 根据当前年获得学年列表,最小值为2010年
     * 
     * @author 赵忠诚
     */
    public static List<String> getSchoolYearsList() {
        List<String> rs = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        int startYear = year;
        if (month >= Constants.SCHOOLYEAR_FIRSTTERM_BEGIN_MONTH) {
            rs.add(year + "-" + (year + 1));
        } else {
            rs.add((year - 1) + "-" + year);
            startYear = year - 1;
        }
        for (int i = startYear; i >= 2010; i--) {
            rs.add((i - 1) + "-" + i);
        }
        return rs;
    }

    /**
     * 根据当前时间判断学年
     */
    public static String getCurrentSchoolYear() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        String schoolyear = "";
        if (month >= Constants.SCHOOLYEAR_FIRSTTERM_BEGIN_MONTH) {
            schoolyear = year + "-" + (year + 1);
        } else {
            schoolyear = (year - 1) + "-" + year;
        }
        return schoolyear;
    }

    /**
     * 根据时间判断学年和学期
     * 
     * @author 赵忠诚
     */
    public static SchoolYearTerm getSchoolYearByDate(Date startDate, Date endDate) throws Exception {
        SchoolYearTerm syt = new SchoolYearTerm();
        Calendar c_start = Calendar.getInstance();
        Calendar c_end = Calendar.getInstance();
        c_start.setTime(startDate);
        c_end.setTime(endDate);
        if (!c_end.after(c_start))
            return null;
        if (c_start.get(Calendar.YEAR) == c_end.get(Calendar.YEAR)) {
            if ((c_end.get(Calendar.MONTH) + 1 > Constants.SCHOOLYEAR_FIRSTTERM_BEGIN_MONTH
                    && c_start.get(Calendar.MONTH) + 1 > Constants.SCHOOLYEAR_FIRSTTERM_BEGIN_MONTH)
                    || (c_end.get(Calendar.MONTH) + 1 < Constants.SCHOOLYEAR_SECONDTERM_BEGIN_MONTH
                            && c_start.get(Calendar.MONTH) + 1 < Constants.SCHOOLYEAR_SECONDTERM_BEGIN_MONTH)) {
                String schoolyear = c_start.get(Calendar.YEAR) + "-" + (c_start.get(Calendar.YEAR) + 1);
                syt.setSchoolyear(schoolyear);
                syt.setTerm("1");
                syt.setTermName("第一学期");
            } else if (c_end.get(Calendar.MONTH) + 1 >= Constants.SCHOOLYEAR_SECONDTERM_BEGIN_MONTH
                    && c_start.get(Calendar.MONTH) + 1 >= Constants.SCHOOLYEAR_SECONDTERM_BEGIN_MONTH) {
                String schoolyear = (c_start.get(Calendar.YEAR) - 1) + "-" + (c_start.get(Calendar.YEAR));
                syt.setSchoolyear(schoolyear);
                syt.setTerm("2");
                syt.setTermName("第二学期");
            } else {
                throw new Exception("开始时间和结束时间不在同一个学期内，无法完成计算" + c_start.toString() + c_end.toString());
            }
        } else if (c_start.get(Calendar.YEAR) + 1 == c_end.get(Calendar.YEAR)
                && c_start.get(Calendar.MONTH) + 1 > Constants.SCHOOLYEAR_FIRSTTERM_BEGIN_MONTH
                && c_end.get(Calendar.MONTH) + 1 < Constants.SCHOOLYEAR_SECONDTERM_BEGIN_MONTH) {
            String schoolyear = c_start.get(Calendar.YEAR) + "-" + (c_start.get(Calendar.YEAR) + 1);
            syt.setSchoolyear(schoolyear);
            syt.setTerm("1");
            syt.setTermName("第一学期");
        } else {
            throw new Exception("开始时间和结束时间不在同一个学年或同一学期，无法完成计算" + c_start.toString() + c_end.toString());
        }
        return syt;
    }

    /**
     * @throws ParseException
     * @author ethanzhao
     * @param schoolyear
     *            ex:2015-2016
     * @param term
     *            1 or 2
     * @return first is startDate, second is endDate
     */
    public static List<Date> getStartEndDateBySchoolYearTerm(String schoolyear, String term) throws ParseException {
        List<Date> rs = new ArrayList<Date>();
        String[] years = schoolyear.split("-");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.equals(term, SchoolTerm.first.getId())) {
            Date startDate = sf.parse(years[0] + "-" + Constants.SCHOOLYEAR_FIRSTTERM_BEGIN_MONTH + "-1");
            Date endDate = sf.parse(years[1] + "-" + Constants.SCHOOLYEAR_SECONDTERM_BEGIN_MONTH + "-1");
            rs.add(startDate);
            rs.add(endDate);
        } else if (StringUtils.equals(term, SchoolTerm.second.getId())) {
            Date startDate = sf.parse(years[1] + "-" + Constants.SCHOOLYEAR_SECONDTERM_BEGIN_MONTH + "-1");
            Date endDate = sf.parse(years[1] + "-" + Constants.SCHOOLYEAR_FIRSTTERM_BEGIN_MONTH + "-1");
            rs.add(startDate);
            rs.add(endDate);
        }
        return rs;
    }

    public static void main(String[] a) {
        List<String> list = SchoolYearTermUtil.getSchoolYearsList();
        for (String s : list) {
            System.out.println(s);
        }
    }
}
