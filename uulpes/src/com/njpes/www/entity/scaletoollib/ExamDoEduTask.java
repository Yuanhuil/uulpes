package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class ExamDoEduTask implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8825066569125667453L;
    private long id;// id号
    private String taskname;// 任务名称
    private String scaleids;// 任务量表
    int xd;// 学段
    private String gradeids;// 年级号
    private String nj;
    private String njmc;
    private String areaids;// 区域号
    private String createtime;
    private String starttime;
    private String endtime;
    private long createuserid;// 分发者用户号
    private long createrorgid;// 创建者机构号
    private String orgids;// 机构号集合
    private String roleids;// 教师类别
    private String rolename;
    private String teacherRoleIds;
    private String schoolids;
    private int parentvisible;
    private int teachervisible;
    private int studentvisible;
    private int warnvisible;
    private int testlimit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getScaleids() {
        return scaleids;
    }

    public void setScaleids(String scaleids) {
        this.scaleids = scaleids;
    }

    public String getGradeids() {
        return gradeids;
    }

    public void setGradeids(String gradeids) {
        this.gradeids = gradeids;
    }

    public String getAreaids() {
        return areaids;
    }

    public void setAreaids(String areaids) {
        this.areaids = areaids;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public long getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(long createuserid) {
        this.createuserid = createuserid;
    }

    public String getOrgids() {
        return orgids;
    }

    public void setOrgids(String orgids) {
        this.orgids = orgids;
    }

    public int getXd() {
        return xd;
    }

    public void setXd(int xd) {
        this.xd = xd;
    }

    public String getNj() {
        return nj;
    }

    public void setNj(String nj) {
        this.nj = nj;
    }

    public String getNjmc() {
        return njmc;
    }

    public void setNjmc(String njmc) {
        this.njmc = njmc;
    }

    public long getCreaterorgid() {
        return createrorgid;
    }

    public void setCreaterorgid(long createrorgid) {
        this.createrorgid = createrorgid;
    }

    public String getRoleids() {
        return roleids;
    }

    public void setRoleids(String roleids) {
        this.roleids = roleids;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getSchoolids() {
        return schoolids;
    }

    public void setSchoolids(String schoolids) {
        this.schoolids = schoolids;
    }

    public String getTeacherRoleIds() {
        return teacherRoleIds;
    }

    public void setTeacherRoleIds(String teacherRoleIds) {
        this.teacherRoleIds = teacherRoleIds;
    }

    public int getParentvisible() {
        return parentvisible;
    }

    public void setParentvisible(int parentvisible) {
        this.parentvisible = parentvisible;
    }

    public int getTeachervisible() {
        return teachervisible;
    }

    public void setTeachervisible(int teachervisible) {
        this.teachervisible = teachervisible;
    }

    public int getStudentvisible() {
        return studentvisible;
    }

    public void setStudentvisible(int studentvisible) {
        this.studentvisible = studentvisible;
    }

    public int getWarnvisible() {
        return warnvisible;
    }

    public void setWarnvisible(int warnvisible) {
        this.warnvisible = warnvisible;
    }

    public int getTestlimit() {
        return testlimit;
    }

    public void setTestlimit(int testlimit) {
        this.testlimit = testlimit;
    }

}
