package com.njpes.www.entity.scaletoollib;

import java.util.Date;

//教委分发实体类
public class ExamdoTaskEducommission {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.id
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.tasknames
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private String taskname;
    private int taskfrom;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.scaleids
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private String scaleids;

    private String scalenames;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.nj
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private String nj;

    private String gradeids;

    private String gradeTitles;

    private String njmc;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.orgids
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private String orgids;

    private String orgnames;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.areaids
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private String areaids;

    private String areanames;

    // 用来判断给学生发还是给老师发
    private String roleids;
    private String rolename;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.createtime
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.starttime
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private Date starttime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.endtime
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private Date endtime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column examdo_task_educommission.createuserid
     * 
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    private Long createuserid;
    private Long createrorgid;
    // 添加两个标志位,一个下发flag（1：显示；0：不显示），一个撤回flag(1:显示；0：不显示)
    private int xfflag;// 下发标识

    private int chflag;// 撤回标识

    private int expireflag;// 过期失效标识

    public int getXfflag() {
        return xfflag;
    }

    public void setXfflag(int xfflag) {
        this.xfflag = xfflag;
    }

    public int getChflag() {
        return chflag;
    }

    public void setChflag(int chflag) {
        this.chflag = chflag;
    }

    public int getExpireflag() {
        return expireflag;
    }

    public void setExpireflag(int expireflag) {
        this.expireflag = expireflag;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examdo_task_educommission.id
     * 
     * @return the value of examdo_task_educommission.id
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examdo_task_educommission.id
     * 
     * @param id
     *            the value for examdo_task_educommission.id
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public int getTaskfrom() {
        return taskfrom;
    }

    public void setTaskfrom(int taskfrom) {
        this.taskfrom = taskfrom;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examdo_task_educommission.scaleids
     * 
     * @return the value of examdo_task_educommission.scaleids
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public String getScaleids() {
        return scaleids;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examdo_task_educommission.scaleids
     * 
     * @param scaleids
     *            the value for examdo_task_educommission.scaleids
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public void setScaleids(String scaleids) {
        this.scaleids = scaleids;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examdo_task_educommission.orgids
     * 
     * @return the value of examdo_task_educommission.orgids
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public String getOrgids() {
        return orgids;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examdo_task_educommission.orgids
     * 
     * @param orgids
     *            the value for examdo_task_educommission.orgids
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public void setOrgids(String orgids) {
        this.orgids = orgids;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examdo_task_educommission.areaids
     * 
     * @return the value of examdo_task_educommission.areaids
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public String getAreaids() {
        return areaids;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examdo_task_educommission.areaids
     * 
     * @param areaids
     *            the value for examdo_task_educommission.areaids
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public void setAreaids(String areaids) {
        this.areaids = areaids;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examdo_task_educommission.createtime
     * 
     * @return the value of examdo_task_educommission.createtime
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examdo_task_educommission.createtime
     * 
     * @param createtime
     *            the value for examdo_task_educommission.createtime
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examdo_task_educommission.starttime
     * 
     * @return the value of examdo_task_educommission.starttime
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examdo_task_educommission.starttime
     * 
     * @param starttime
     *            the value for examdo_task_educommission.starttime
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column examdo_task_educommission.endtime
     * 
     * @return the value of examdo_task_educommission.endtime
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column examdo_task_educommission.endtime
     * 
     * @param endtime
     *            the value for examdo_task_educommission.endtime
     * @mbggenerated Thu Jun 25 14:40:22 CST 2015
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getScalenames() {
        return scalenames;
    }

    public void setScalenames(String scalenames) {
        this.scalenames = scalenames;
    }

    public String getOrgnames() {
        return orgnames;
    }

    public void setOrgnames(String orgnames) {
        this.orgnames = orgnames;
    }

    public String getAreanames() {
        return areanames;
    }

    public void setAreanames(String areanames) {
        this.areanames = areanames;
    }

    public String getNj() {
        return nj;
    }

    public void setNj(String nj) {
        this.nj = nj;
    }

    public String getGradeids() {
        return gradeids;
    }

    public void setGradeids(String gradeids) {
        this.gradeids = gradeids;
    }

    public Long getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(Long createuserid) {
        this.createuserid = createuserid;
    }

    public Long getCreaterorgid() {
        return createrorgid;
    }

    public void setCreaterorgid(Long createrorgid) {
        this.createrorgid = createrorgid;
    }

    public String getGradeTitles() {
        return gradeTitles;
    }

    public void setGradeTitles(String gradeTitles) {
        this.gradeTitles = gradeTitles;
    }

    public String getRoleids() {
        return roleids;
    }

    public void setRoleids(String roleids) {
        this.roleids = roleids;
    }

    public String getNjmc() {
        return njmc;
    }

    public void setNjmc(String njmc) {
        this.njmc = njmc;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

}
