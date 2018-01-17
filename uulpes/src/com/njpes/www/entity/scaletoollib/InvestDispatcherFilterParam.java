package com.njpes.www.entity.scaletoollib;

import java.util.List;

public class InvestDispatcherFilterParam {
    private String dispatchTimeStart;
    private String dispatchTimeEnd;
    private String testStartTime;
    private String testEndTime;
    // 下面是学生的过滤条件
    private String scaleName;
    private String taskKeywords;
    private long createuserid;
    private long orgid;
    private String progressStatus;
    private int objectType;// 分发对象
    private long bjid = -1;
    private int roleid = -1;
    private List roleidList;
    private long userid;

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

    public long getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(long createuserid) {
        this.createuserid = createuserid;
    }

    public String getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public long getBjid() {
        return bjid;
    }

    public void setBjid(long bjid) {
        this.bjid = bjid;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public List getRoleidList() {
        return roleidList;
    }

    public void setRoleidList(List roleidList) {
        this.roleidList = roleidList;
    }

}
