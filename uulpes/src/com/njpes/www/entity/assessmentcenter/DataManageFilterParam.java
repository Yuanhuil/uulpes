package com.njpes.www.entity.assessmentcenter;

public class DataManageFilterParam {

    // ---------------import------------
    private long orgid;
    private String nj;
    private int bj = -1;
    private int teacherRole = -1;// 教师角色
    private String scaleType;
    private String scaleName;
    private String scaleSourceId;
    private String scaleTypeId;
    private String scaleId;
    private String name;
    private String sfzjh;
    private int gender = -1;
    private String identify;
    private String testStartTime;
    private String testEndTime;
    // -----------------------
    private String scaleSource;
    // --------------------------
    private int schoolLevel;
    private String schoolName;
    private int xd = -1;
    private String xdmc;
    private String roleName;
    private int roleId;
    private long threeAngleUserId;

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    // 下面是教委的过滤条件
    private int xzqh = -1; // 行政区划
    // 下面是学生的过滤条件
    private int sclassId = -1; // 班级名称
    // ---------------
    private int sgradeId = -1; // 年级名称

    public String getNj() {
        return nj;
    }

    public void setNj(String nj) {
        this.nj = nj;
    }

    public int getBj() {
        return bj;
    }

    public void setBj(int bj) {
        this.bj = bj;
    }

    public int getTeacherRole() {
        return teacherRole;
    }

    public void setTeacherRole(int teacherRole) {
        this.teacherRole = teacherRole;
    }

    public String getScaleType() {
        return scaleType;
    }

    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
    }

    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String scaleName) {
        this.scaleName = scaleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(String testStartTime) {
        this.testStartTime = testStartTime;
    }

    public String getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(String testEndTime) {
        this.testEndTime = testEndTime;
    }

    public String getScaleSource() {
        return scaleSource;
    }

    public void setScaleSource(String scaleSource) {
        this.scaleSource = scaleSource;
    }

    public int getSchoolLevel() {
        return schoolLevel;
    }

    public void setSchoolLevel(int schoolLevel) {
        this.schoolLevel = schoolLevel;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getXd() {
        return xd;
    }

    public void setXd(int xd) {
        this.xd = xd;
    }

    public String getXdmc() {
        return xdmc;
    }

    public void setXdmc(String xdmc) {
        this.xdmc = xdmc;
    }

    public String getScaleId() {
        return scaleId;
    }

    public void setScaleId(String scaleId) {
        this.scaleId = scaleId;
    }

    public String getScaleSourceId() {
        return scaleSourceId;
    }

    public void setScaleSourceId(String scaleSourceId) {
        this.scaleSourceId = scaleSourceId;
    }

    public String getScaleTypeId() {
        return scaleTypeId;
    }

    public void setScaleTypeId(String scaleTypeId) {
        this.scaleTypeId = scaleTypeId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getSclassId() {
        return sclassId;
    }

    public void setSclassId(int sclassId) {
        this.sclassId = sclassId;
    }

    public int getSgradeId() {
        return sgradeId;
    }

    public void setSgradeId(int sgradeId) {
        this.sgradeId = sgradeId;
    }

    public void setXzqh(int xzqh) {
        this.xzqh = xzqh;
    }

    public int getXzqh() {
        return xzqh;
    }

    public long getThreeAngleUserId() {
        return threeAngleUserId;
    }

    public void setThreeAngleUserId(long threeAngleUserId) {
        this.threeAngleUserId = threeAngleUserId;
    }

    public String getSfzjh() {
        return sfzjh;
    }

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

}
