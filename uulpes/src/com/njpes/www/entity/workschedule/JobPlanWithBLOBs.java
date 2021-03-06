package com.njpes.www.entity.workschedule;

public class JobPlanWithBLOBs extends JobPlan {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_plan.title
     *
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    private String title;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_plan.target
     *
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    //多文本编辑器未用字段，目前多文本编辑器使用content字段
    private String target;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_plan.ideology
     *
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    //多文本编辑器未用字段，目前多文本编辑器使用content字段
    private String ideology;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_plan.content
     *
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    private String content;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_plan.auditext
     *
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    private String auditext;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_plan.jobmainpoint
     *
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    //多文本编辑器未用字段，目前多文本编辑器使用content字段
    private String jobmainpoint;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_plan.arrange
     *
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    //多文本编辑器未用字段，目前多文本编辑器使用content字段
    private String arrange;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_plan.guarantee
     *
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    //多文本编辑器未用字段，目前多文本编辑器使用content字段
    private String guarantee;

    private long[] org_ids;

    private String jobstyle;

    private String personarr;

    private boolean canAudit;

    public String getJobstyle() {
        return jobstyle;
    }

    public void setJobstyle(String jobstyle) {
        this.jobstyle = jobstyle;
    }

    public String getPersonarr() {
        return personarr;
    }

    public void setPersonarr(String personarr) {
        this.personarr = personarr;
    }

    public long[] getOrg_ids() {
        return org_ids;
    }

    public void setOrg_ids(long[] org_ids) {
        this.org_ids = org_ids;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_plan.title
     *
     * @return the value of job_plan.title
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_plan.title
     *
     * @param title
     *            the value for job_plan.title
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_plan.target
     *
     * @return the value of job_plan.target
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public String getTarget() {
        return target;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_plan.target
     *
     * @param target
     *            the value for job_plan.target
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_plan.ideology
     *
     * @return the value of job_plan.ideology
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public String getIdeology() {
        return ideology;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_plan.ideology
     *
     * @param ideology
     *            the value for job_plan.ideology
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public void setIdeology(String ideology) {
        this.ideology = ideology;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_plan.content
     *
     * @return the value of job_plan.content
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_plan.content
     *
     * @param content
     *            the value for job_plan.content
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_plan.auditext
     *
     * @return the value of job_plan.auditext
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public String getAuditext() {
        return auditext;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_plan.auditext
     *
     * @param auditext
     *            the value for job_plan.auditext
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public void setAuditext(String auditext) {
        this.auditext = auditext;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_plan.jobmainpoint
     *
     * @return the value of job_plan.jobmainpoint
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public String getJobmainpoint() {
        return jobmainpoint;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_plan.jobmainpoint
     *
     * @param jobmainpoint
     *            the value for job_plan.jobmainpoint
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public void setJobmainpoint(String jobmainpoint) {
        this.jobmainpoint = jobmainpoint;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_plan.arrange
     *
     * @return the value of job_plan.arrange
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public String getArrange() {
        return arrange;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_plan.arrange
     *
     * @param arrange
     *            the value for job_plan.arrange
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public void setArrange(String arrange) {
        this.arrange = arrange;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_plan.guarantee
     *
     * @return the value of job_plan.guarantee
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public String getGuarantee() {
        return guarantee;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_plan.guarantee
     *
     * @param guarantee
     *            the value for job_plan.guarantee
     * @mbggenerated Wed Jul 01 00:28:08 CST 2015
     */
    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public boolean isCanAudit() {
        return canAudit;
    }

    public void setCanAudit(boolean canAudit) {
        this.canAudit = canAudit;
    }

}