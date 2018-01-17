package com.njpes.www.entity.baseinfo.enums;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

public class OrgLevelEnum extends ValuedEnum {
    /**
     * 
     */
    private static final long serialVersionUID = -2781425643298674837L;

    public static final int SYS_VALUE = 0;
    public static final int PROV_VALUE = 2;
    public static final int CITY_VALUE = 3;
    public static final int DIST_VALUE = 4;
    public static final int SHCOOL_VALUE = 5;
    public static final int INDI_VALUE = 6;

    // 新增
    public static final int GRADE_VALUE = 7;
    public static final int CLASS_VALUE = 8;
    public static final int STUDENT_VALUE = 9;
    public static final int TEACHER_VALUE = 10;

    // 九型人格给公司做，需要添加一个公司级别
    public static final int COMPANY_VALUE = 100;

    /**
     * name, int value,前面的是name，后面的是值
     */
    public static final OrgLevelEnum SYS_LEV = new OrgLevelEnum("系统级", SYS_VALUE);

    public static final OrgLevelEnum PROV_LEV = new OrgLevelEnum("省级", PROV_VALUE);
    public static final OrgLevelEnum CITY_LEV = new OrgLevelEnum("市级", CITY_VALUE);
    public static final OrgLevelEnum DIST_LEV = new OrgLevelEnum("区级", DIST_VALUE);
    public static final OrgLevelEnum SCHOOL_LEV = new OrgLevelEnum("校级", SHCOOL_VALUE);
    public static final OrgLevelEnum GRADE_LEV = new OrgLevelEnum("年级", GRADE_VALUE);
    public static final OrgLevelEnum CLASS_LEV = new OrgLevelEnum("班级", CLASS_VALUE);
    public static final OrgLevelEnum STUDENT_LEV = new OrgLevelEnum("学生", STUDENT_VALUE);
    public static final OrgLevelEnum TEACHER_LEV = new OrgLevelEnum("教师", TEACHER_VALUE);
    public static final OrgLevelEnum INDI_LEV = new OrgLevelEnum("个人", INDI_VALUE);

    public static final OrgLevelEnum COMPANY_LEV = new OrgLevelEnum("公司", INDI_VALUE);

    protected OrgLevelEnum(String name, int value) {
        super(name, value);
    }

    public OrgLevelEnum subLevel() {
        switch (this.getValue()) {
        case PROV_VALUE:
            return OrgLevelEnum.CITY_LEV;
        case CITY_VALUE:
            return OrgLevelEnum.DIST_LEV;
        case DIST_VALUE:
            return OrgLevelEnum.SCHOOL_LEV;
        case SHCOOL_VALUE:
            return OrgLevelEnum.GRADE_LEV;
        case GRADE_VALUE:
            return OrgLevelEnum.CLASS_LEV;
        case CLASS_VALUE:
            return OrgLevelEnum.STUDENT_LEV;
        default:
            return null;
        }

    }

    public static int nextLevel(int currLev) {
        switch (currLev) {
        case PROV_VALUE:
            return OrgLevelEnum.CITY_VALUE;
        case CITY_VALUE:
            return OrgLevelEnum.DIST_VALUE;
        case DIST_VALUE:
            return OrgLevelEnum.SHCOOL_VALUE;
        case SHCOOL_VALUE:
            return OrgLevelEnum.GRADE_VALUE;
        case GRADE_VALUE:
            return OrgLevelEnum.CLASS_VALUE;
        case CLASS_VALUE:
            return OrgLevelEnum.STUDENT_VALUE;
        default:
            return -1;
        }
    }

    public static OrgLevelEnum getEnum(String name) {
        return (OrgLevelEnum) getEnum(OrgLevelEnum.class, name);
    }

    public static OrgLevelEnum getEnum(int level) {
        return (OrgLevelEnum) getEnum(OrgLevelEnum.class, level);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, OrgLevelEnum> getEnumMap() {
        return getEnumMap(OrgLevelEnum.class);
    }

    @SuppressWarnings("unchecked")
    public static List<OrgLevelEnum> getEnumList() {
        return getEnumList(OrgLevelEnum.class);
    }

    @SuppressWarnings("unchecked")
    public static Iterator<OrgLevelEnum> iterator() {
        return iterator(OrgLevelEnum.class);
    }
}
