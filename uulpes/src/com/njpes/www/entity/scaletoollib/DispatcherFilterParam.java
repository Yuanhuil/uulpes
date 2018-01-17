package com.njpes.www.entity.scaletoollib;

public class DispatcherFilterParam {

    private String dispatchTimeStart;
    private String dispatchTimeEnd;
    private String testStartTime;
    private String testEndTime;
    // 下面是教委的过滤条件
    private int xzqh = -1; // 行政区划
    // 下面是学生的过滤条件
    private int sclassId = -1; // 班级名称
    // ---------------
    private int sgradeId = -1; // 年级名称
    private String scaleName;
    private String taskKeywords;
    private int createuserid;
    private String progressStatus;
    private String roleName;
    private int roleid;
    private long orgid;

    public String getDispatchTimeStart() {
        return dispatchTimeStart;
    }

    public void setDispatchTimeStart(String dispatchTimeStart) {
        this.dispatchTimeStart = dispatchTimeStart;
    }

    public String getDispatchTimeEnd() {
        return dispatchTimeEnd;
    }

    public void setDispatchTimeEnd(String dispatchTimeEnd) {
        this.dispatchTimeEnd = dispatchTimeEnd;
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

    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String scaleName) {
        this.scaleName = scaleName;
    }

    public String getTaskKeywords() {
        return taskKeywords;
    }

    public void setTaskKeywords(String taskKeywords) {
        this.taskKeywords = taskKeywords;
    }

    public int getSgradeId() {
        return sgradeId;
    }

    public void setSgradeId(int sgradeId) {
        this.sgradeId = sgradeId;
    }

    public int getSclassId() {
        return sclassId;
    }

    public void setSclassId(int sclassId) {
        this.sclassId = sclassId;
    }

    public int getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(int createuserid) {
        this.createuserid = createuserid;
    }

    public int getXzqh() {
        return xzqh;
    }

    public void setXzqh(int xzqh) {
        this.xzqh = xzqh;
    }

    public String getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

}
