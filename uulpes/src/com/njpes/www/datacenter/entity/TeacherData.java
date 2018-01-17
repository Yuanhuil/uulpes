package com.njpes.www.datacenter.entity;

import org.apache.commons.lang3.StringUtils;

public class TeacherData {
    public String id;

    /**
     * 工号
     */
    public String gh;

    /**
     * 姓名
     */
    public String xm;

    /**
     * 性别码 1:男 2:女
     */
    public String xbm;

    /**
     * 籍贯号
     */
    public String jg;

    /**
     * 民族码
     */
    public String mzm;

    /**
     * 民族名称
     */
    public String mzName;

    /**
     * 身份证件号
     */
    public String sfzjh;

    /**
     * 现住址
     */
    public String xzz;

    /**
     * 联系电话
     */
    public String lxdh;

    /**
     * 职务
     */
    public String job;

    /**
     * 班级id
     */
    public String classId;

    /**
     * 班级名称
     */
    public String className;

    /**
     * 年级id
     */
    public String gradeId;

    /**
     * 年级名称
     */
    public String gradeName;

    /**
     * 学校id
     */
    public String schoolId;

    /**
     * 学校名称
     */
    public String schoolName;

    /**
     * 学段id
     */
    public String groupId;

    /**
     * 学段名称
     */
    public String groupName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGh() {
        return gh;
    }

    public void setGh(String gh) {
        this.gh = gh;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getXbm() {
        return xbm;
    }

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg;
    }

    public String getMzm() {
        return mzm;
    }

    public void setMzm(String mzm) {
        this.mzm = mzm;
        setMzName(mzm);
    }

    public String getSfzjh() {
        return sfzjh;
    }

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    public String getXzz() {
        return xzz;
    }

    public void setXzz(String xzz) {
        this.xzz = xzz;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMzName() {
        return mzName;
    }

    /**
     * 民族码(mzm)转换为民族名称
     * @param mzm
     * @return void
     * @Date 2017年4月15日 下午6:10:31
     */
    public void setMzName(String mzm) {
        String str = "01=汉族;02=蒙古族;03=回族;04=藏族;05=维吾尔族;06=苗族;07=彝族;" + "08=壮族;09=布依族;10=朝鲜族;11=满族;12=侗族;13=瑶族;14=白族;"
                + "15=土家族;16=哈尼族;17=哈萨克族;18=傣族;19=黎族;20=傈僳族;21=佤族;" + "22=畲族;23=高山族;24=拉祜族;25=水族;26=东乡族;27=纳西族;28=景颇族;"
                + "29=柯尔克孜族;30=土族;31=达斡尔族;32=仫佬族;33=羌族;34=布朗族;35=撒拉族;"
                + "36=毛难族;37=仡佬族;38=锡伯族;39=阿昌族;40=普米族;41=塔吉克族;42=怒族;"
                + "43=乌孜别克族;44=俄罗斯族;45=鄂温克族;46=德昂族;47=保安族;48=裕固族;49=京族;"
                + "50=塔塔尔族;51=独龙族;52=鄂伦春族;53=赫哲族;54=门巴族;55=珞巴族;56=基诺族";
        String[] split = StringUtils.split(str, ";");
        if (StringUtils.isNotBlank(mzm)) {
            for (String string : split) {
                if (StringUtils.contains(string, mzm)) {
                    this.mzName = StringUtils.substring(string, 3);
                    break;
                }
            }
        }
    }

}
