package com.njpes.www.entity.workschedule;

public class JobNoticeWithBLOBs extends JobNotice {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.title
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column job_notice.content
     *
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    private String content;

    private String audittext;

    private long[] org_ids;

    private String firstpageimageUrl;

    public String getFirstpageimageUrl() {
        return firstpageimageUrl;
    }

    public void setFirstpageimageUrl(String firstpageimageUrl) {
        this.firstpageimageUrl = firstpageimageUrl;
    }

    public long[] getOrg_ids() {
        return org_ids;
    }

    public void setOrg_ids(long[] org_ids) {
        this.org_ids = org_ids;
    }

    public String getAudittext() {
        return audittext;
    }

    public void setAudittext(String audittext) {
        this.audittext = audittext;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.title
     *
     * @return the value of job_notice.title
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.title
     *
     * @param title
     *            the value for job_notice.title
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column job_notice.content
     *
     * @return the value of job_notice.content
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column job_notice.content
     *
     * @param content
     *            the value for job_notice.content
     * @mbggenerated Thu Jun 04 00:14:06 CST 2015
     */
    public void setContent(String content) {
        this.content = content;
    }
}