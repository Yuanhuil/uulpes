package com.njpes.www.entity.baseinfo.enums;

import java.util.HashMap;

/**
 * @Description:
 * @author zhangchao
 * @Date 2015-5-27 下午9:45:52
 */
public enum TimeEnum {
    th8(16, "08:00"), th8_(17, "08:30"), th9(18, "09:00"), th9_(19, "09:30"), th10(20, "10:00"), th10_(21,
            "10:30"), th11(22, "11:00"), th11_(23, "11:30"), th12(24, "12:00"), th12_(25, "12:30"), th13(26,
                    "13:00"), th13_(27, "13:30"), th14(28, "14:00"), th14_(29, "14:30"), th15(30, "15:00"), th15_(31,
                            "15:30"), th16(32, "16:00"), th16_(33, "16:30"), th17(34, "17:00"), th17_(35,
                                    "17:30"), th18(36, "18:00"), th18_(37, "18:30"), th19(38, "19:00"), th19_(39,
                                            "19:30"), th20(40, "20:00"), th41_(41,
                                                    "20:30"), th42(42, "21:00"), th43_(43, "21:30"), th44(44, "22:00");

    /*
     * th8(8, "08:00"), th9(9, "09:00"), th10(10, "10:00"), th11(11, "11:00"),
     * th12( 12, "12:00"), th13(13, "13:00"), th14(14, "14:00"), th15(15,
     * "15:00"), th16(16, "16:00"), th17(17, "17:00"), th18(18, "18:00"), th19(
     * 19, "19:00"), th20(20, "20:00"), th21(21, "21:00"), th22(22, "22:00");
     */
    private final int value;
    private final String info;

    private TimeEnum(int value, String info) {
        this.value = value;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public int getValue() {
        return value;
    }

    public static int info2value(String info) {
        for (TimeEnum timeEnum : TimeEnum.values()) {
            if (info.contains(timeEnum.info)) {
                return timeEnum.value;
            }
        }
        return 0;
    }

    public static String value2info(int value) {
        for (TimeEnum timeEnum : TimeEnum.values()) {
            if (value == timeEnum.value) {
                return timeEnum.info;
            }
        }
        return "";
    }

    public static HashMap valueMap() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();

        for (TimeEnum timeEnum : TimeEnum.values()) {
            map.put(timeEnum.value, timeEnum.info);
        }
        return map;
    }
}
