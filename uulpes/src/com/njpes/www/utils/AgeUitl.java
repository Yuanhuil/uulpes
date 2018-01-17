package com.njpes.www.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hsqldb.lib.StringUtil;

public class AgeUitl {
    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    public static int getAgeByBirthdayStr(String birthdayStr) {
        try {
            if (StringUtil.isEmpty(birthdayStr))
                return -1;
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
            Date birthday = formatDate.parse(birthdayStr);
            Calendar cal = Calendar.getInstance();

            if (cal.before(birthday)) {
                throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
            }

            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(birthday);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            int age = yearNow - yearBirth;

            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    // monthNow==monthBirth
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;
                    }
                } else {
                    // monthNow>monthBirth
                    age--;
                }
            }
            return age;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeAtTime(String birthdayStr, String endTime) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
            Date birthday = formatDate.parse(birthdayStr);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd");
            Date ddd = fd.parse(endTime);
            cal.setTime(ddd);

            if (cal.before(birthday)) {
                throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
            }

            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(birthday);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            int age = yearNow - yearBirth;

            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    // monthNow==monthBirth
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;
                    }
                } else {
                    // monthNow>monthBirth
                    age--;
                }
            }
            return age;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    static public int getRealGradeid(int gradecodeid, String xxxz) {
        if (gradecodeid <= 6) {
            return gradecodeid;
        }
        if (gradecodeid >= 11) {
            return gradecodeid - 1;
        }

        if (xxxz != null) {
            if (Integer.parseInt(xxxz) == 5) {
                return gradecodeid <= 5 ? gradecodeid : gradecodeid - 1;
            }
            return gradecodeid;
        }
        return gradecodeid;
    }

    static public int getNj(int gradeorderid) {
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        if (gradeorderid <= 6) {// 小学
            if (monthNow >= 9)
                return yearNow - gradeorderid + 1;
            else
                return yearNow - gradeorderid;
        }
        // 初中
        else if (gradeorderid > 6 && gradeorderid <= 10) {
            if (monthNow >= 9)
                return yearNow - gradeorderid + 7;
            else
                return yearNow - gradeorderid + 6;
        }
        // 高中
        else if (gradeorderid > 10 && gradeorderid <= 13) {
            if (monthNow >= 9)
                return yearNow - gradeorderid + 11;
            else
                return yearNow - gradeorderid + 10;
        }
        return 0;
    }

    static public int getXdByGrade(int gradeid) {
        if (gradeid < 7)
            return 1;
        else if (gradeid < 11)
            return 2;
        else if (gradeid < 14)
            return 3;
        else
            return 4;
    }

    static public String getGradeName(int gradeorderid) {
        switch (gradeorderid) {
        case 1:
            return "小学一年级";
        case 2:
            return "小学二年级";
        case 3:
            return "小学三年级";
        case 4:
            return "小学四年级";
        case 5:
            return "小学五年级";
        case 6:
            return "小学六年级";

        case 7:
            return "初中一年级";
        case 8:
            return "初中二年级";
        case 9:
            return "初中三年级";
        case 10:
            return "初中四年级";
        case 11:
            return "高中一年级";
        case 12:
            return "高中二年级";
        case 13:
            return "高中三年级";
        default:
            return "成人";
        }
    }

    static public String getNjName(int gradeorderid) {
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        if (gradeorderid <= 6) {// 小学
            if (monthNow >= 9) {
                return String.format("小学%s级", yearNow - gradeorderid + 1);
            } else
                return String.format("小学%s级", yearNow - gradeorderid);
        }
        // 初中
        else if (gradeorderid > 6 && gradeorderid <= 10) {
            if (monthNow >= 9)
                return String.format("初中%s级", yearNow - gradeorderid + 7);

            else
                return String.format("初中%s级", yearNow - gradeorderid + 6);
        }
        // 高中
        else if (gradeorderid > 10 && gradeorderid <= 13) {
            if (monthNow >= 9)
                return String.format("高中%s级", yearNow - gradeorderid + 11);
            else
                return String.format("高中%s级", yearNow - gradeorderid + 10);
        }
        return null;
    }

    /**
     * @author 赵忠诚
     */
    static public Date getBirthDate(String idcard) {
        if (StringUtils.isEmpty(idcard))
            return null;
        String birthday = idcard.substring(6, 14);
        Date birthdate = null;
        try {
            birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return birthdate;
    }

    // 把yyyymmdd转成yyyy-MM-dd格式
    public static String formatDate(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sfstr;
    }

    // 根据学年、学期得到开始时间
    public static String getStartTimeFromSchoolyear(String schoolyear, int term) {
        String[] years = schoolyear.split("-");
        String year1 = years[0];
        String year2 = years[1];
        if (term == 1) {
            return year1 + "-09-01 00:00:00";
        }
        if (term == 2) {
            return year2 + "-02-01 00:00:00";
        }
        return null;
    }

    // 根据学年、学期得到开始时间
    public static String getEndTimeFromSchoolyear(String schoolyear, int term) {
        String[] years = schoolyear.split("-");
        String year1 = years[0];
        String year2 = years[1];
        if (term == 1) {
            return year2 + "-01-30 59:59:59";
        }
        if (term == 2) {
            return year2 + "-08-31 59:59:59";
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            // SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            // Date birthday = formatDate.parse("1981-04-17");

            // System.out.println(getAgeByBirthday(birthday));
            String njname = getNjName(8);
            System.out.println(njname);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
