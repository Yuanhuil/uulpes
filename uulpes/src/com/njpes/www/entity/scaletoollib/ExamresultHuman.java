package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;
import java.util.Date;

public class ExamresultHuman implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long user_id;
    private String csrq;
    private String xm;
    private String gender;
    private Integer scale_id;
    private String scalename;
    private Date ok_time;
    private Date start_time;
    private Integer warning_grade;
    private int iswarnsure;
    private String question_score;
    private String dim_score;
    private String attrs;
    private Integer orgid; // 学校或教委组织机构id
    private String sfzjh; // 身份证件号
    private String orgname; // 学校名称
    private int nj; // 入学年份
    private int classid; // 班级id
    private int xd; // 学段
    private String bjmc; // 班级名
    private String njmc; // 年级名
    private String xh; // 学号
    private String gh; // 工号
    private int roleid; // 角色id
    private int gradeorderid; // 年级id

    public Integer getOrgid() {
        return orgid;
    }

    public void setOrgid(Integer orgid) {
        this.orgid = orgid;
    }

    public String getSfzjh() {
        return sfzjh;
    }

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String xxmc) {
        this.orgname = xxmc;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getGh() {
        return gh;
    }

    public void setGh(String gh) {
        this.gh = gh;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getGradeorderid() {
        return gradeorderid;
    }

    public void setGradeorderid(int gradeorderid) {
        this.gradeorderid = gradeorderid;
    }

    public String getAttrs() {
        return attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Integer getScale_id() {
        return scale_id;
    }

    public void setScale_id(Integer scale_id) {
        this.scale_id = scale_id;
    }

    public Date getOk_time() {
        return ok_time;
    }

    public void setOk_time(Date oktime) {
        this.ok_time = oktime;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date starttime) {
        this.start_time = starttime;
    }

    public Integer getWarning_grade() {
        return warning_grade;
    }

    public void setWarning_grade(Integer warninggrade) {
        this.warning_grade = warninggrade;
    }

    public int getIswarnsure() {
        return iswarnsure;
    }

    public void setIswarnsure(int iswarnsure) {
        this.iswarnsure = iswarnsure;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getNj() {
        return nj;
    }

    public void setNj(int nj) {
        this.nj = nj;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getNjmc() {
        return njmc;
    }

    public void setNjmc(String njmc) {
        this.njmc = njmc;
    }

    public String getScalename() {
        return scalename;
    }

    public void setScalename(String scalename) {
        this.scalename = scalename;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getXd() {
        return xd;
    }

    public void setXd(int xd) {
        this.xd = xd;
    }

    public String getBjmc() {
        return bjmc;
    }

    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
    }

    public String getQuestion_score() {
        return question_score;
    }

    public void setQuestion_score(String questionscore) {
        this.question_score = questionscore;
    }

    public String getDim_score() {
        return dim_score;
    }

    public void setDim_score(String dimscore) {
        this.dim_score = dimscore;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }
}