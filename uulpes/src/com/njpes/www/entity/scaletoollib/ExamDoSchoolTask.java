package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class ExamDoSchoolTask implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 376423364031817765L;
    private long id;// id号
    private String taskname;// 任务名称
    private String scaleids;// 任务量表
    private String xd;// 学段
    private String nj;// 年级号
    private String njmc;
    private String bj;// 班号
    private String bjmc;
    private String roleids;
    private String rolename;
    private String studentids;
    private String teacherids;
    private String createtime;
    private String starttime;
    private String endtime;
    private long createuserid;// 创建者姓名
    private String cityid;
    private String countyid;
    private String townid;
    private long orgid;// 机构号集合
    private String gradeids;

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

    public String getBj() {
        return bj;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }

    public String getBjmc() {
        return bjmc;
    }

    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
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

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public String getStudentids() {
        return studentids;
    }

    public void setStudentids(String studentids) {
        this.studentids = studentids;
    }

    public String getTeacherids() {
        return teacherids;
    }

    public void setTeacherids(String teacherids) {
        this.teacherids = teacherids;
    }

    public String getXd() {
        return xd;
    }

    public void setXd(String xd) {
        this.xd = xd;
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

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCountyid() {
        return countyid;
    }

    public void setCountyid(String countyid) {
        this.countyid = countyid;
    }

    public String getTownid() {
        return townid;
    }

    public void setTownid(String townid) {
        this.townid = townid;
    }

    public String getGradeids() {
        return gradeids;
    }

    public void setGradeids(String gradeids) {
        this.gradeids = gradeids;
    }

}
