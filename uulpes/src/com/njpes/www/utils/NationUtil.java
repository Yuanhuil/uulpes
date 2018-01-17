package com.njpes.www.utils;

import org.hsqldb.lib.StringUtil;

public class NationUtil {
    public static String getMz(String mzm) {
        if (StringUtil.isEmpty(mzm))
            return "汉族";
        else if (mzm.equals("01"))
            return "汉族";
        else if (mzm.equals("02"))
            return "蒙古族";
        else if (mzm.equals("03"))
            return "回族";
        else if (mzm.equals("04"))
            return "藏族";
        else if (mzm.equals("05"))
            return "维吾尔族";
        else if (mzm.equals("06"))
            return "苗族";
        else if (mzm.equals("07"))
            return "彝族";
        else if (mzm.equals("08"))
            return "壮族";
        else if (mzm.equals("09"))
            return "布依族";
        else if (mzm.equals("10"))
            return "朝鲜族";
        else if (mzm.equals("11"))
            return "满族";
        else if (mzm.equals("12"))
            return "汉侗族";
        else if (mzm.equals("13"))
            return "瑶族";
        else if (mzm.equals("14"))
            return "白族";
        else if (mzm.equals("15"))
            return "土家族";
        else if (mzm.equals("16"))
            return "哈尼族";
        else if (mzm.equals("17"))
            return "哈萨克族";
        else if (mzm.equals("18"))
            return "傣族";
        else if (mzm.equals("19"))
            return "黎族";
        else if (mzm.equals("20"))
            return "傈僳族";
        else if (mzm.equals("21"))
            return "佤族";
        else if (mzm.equals("22"))
            return "畲族";
        else if (mzm.equals("23"))
            return "高山族";
        else if (mzm.equals("24"))
            return "拉祜族";
        else if (mzm.equals("25"))
            return "水族";
        else if (mzm.equals("26"))
            return "东乡族";
        else if (mzm.equals("27"))
            return "纳西族";
        else if (mzm.equals("28"))
            return "景颇族";
        else if (mzm.equals("29"))
            return "柯尔克孜族";
        else if (mzm.equals("30"))
            return "土族";
        else if (mzm.equals("31"))
            return "达斡尔族";
        else if (mzm.equals("32"))
            return "仫佬族";
        else if (mzm.equals("33"))
            return "羌族";
        else if (mzm.equals("34"))
            return "布朗族";
        else if (mzm.equals("35"))
            return "撒拉族";
        else if (mzm.equals("36"))
            return "毛难族";
        else if (mzm.equals("37"))
            return "仡佬族";
        else if (mzm.equals("38"))
            return "锡伯族";
        else if (mzm.equals("39"))
            return "阿昌族";
        else if (mzm.equals("40"))
            return "普米族";
        else if (mzm.equals("41"))
            return "塔吉克族";
        else if (mzm.equals("42"))
            return "怒族";
        else if (mzm.equals("43"))
            return "乌孜别克族";
        else if (mzm.equals("44"))
            return "俄罗斯族";
        else if (mzm.equals("45"))
            return "鄂温克族";
        else if (mzm.equals("46"))
            return "德昂族";
        else if (mzm.equals("47"))
            return "保安族";
        else if (mzm.equals("48"))
            return "裕固族";
        else if (mzm.equals("49"))
            return "京族";
        else if (mzm.equals("50"))
            return "塔塔尔族";
        else if (mzm.equals("51"))
            return "独龙族";
        else if (mzm.equals("52"))
            return "鄂伦春族";
        else if (mzm.equals("53"))
            return "赫哲族";
        else if (mzm.equals("54"))
            return "门巴族";
        else if (mzm.equals("55"))
            return "珞巴族";
        else if (mzm.equals("55"))
            return "基诺族";
        else
            return "汉族";
    }
}
