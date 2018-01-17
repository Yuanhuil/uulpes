package com.njpes.www.entity.baseinfo.enums;

/**
 * @author 赵忠诚 学校类型，根据国家标准写，目前只是写了用到的枚举，其他没用到的暂时没有写入
 */
public enum SchoolBXLX {
    s_111("111", "幼儿园"), s_119("119", "附设幼儿班"),

    s_211("211", "小学"), s_218("218", "小学教学点"), s_219("219", "附设小学班"), s_221("221", "职工小学 "), s_222("222",
            "农民小学 "), s_228("228", "小学班 "), s_229("229", "扫盲班 "),

    primary_school("2", "初等教育"),

    junior_school_31("31", "普通初中"), junior_school_311("311", "初级中学"), junior_school_312("312",
            "九年一贯制学校"),junior_school_350("350",
                    "高等教育学校"), junior_school_319("319",
                    "附设普通初中班"), junior_school_32("32", "职业初中"), junior_school_33("33", "成人初中"),

    senior_school_34("34", "普通高中"), senior_school_341("341", "完全中学"), senior_school_345("345",
            "十二年一贯制学校"), senior_school_349("349", "附设普通高中班"), senior_school_35("35",
                    "成人高中"), senior_school_36("36", "中等职业学校"), senior_school_37("37", "工读学校");

    String info;
    String id;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {

        this.info = info;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    private SchoolBXLX(String id, String info) {
        this.info = info;
        this.id = id;
    }

    public String value() {
        return this.id;
    }
}
