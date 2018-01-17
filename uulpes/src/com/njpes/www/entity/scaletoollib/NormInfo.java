package com.njpes.www.entity.scaletoollib;

import java.util.Date;

public class NormInfo {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column scale_norminfo.id
     *
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column scale_norminfo.name
     *
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column scale_norminfo.type
     *
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    // private Byte type;
    private int type;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column scale_norminfo.createtime
     *
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    private Date createtime;

    private Integer scaleId;

    private long createuserid;

    private String description;

    private int areaid;

    private int orglevel;

    private long orgid = -1;

    private String editer;

    private String orgname;

    private String edittime;

    public Integer getScaleId() {
        return scaleId;
    }

    public void setScaleId(Integer scaleId) {
        this.scaleId = scaleId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column scale_norminfo.id
     *
     * @return the value of scale_norminfo.id
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column scale_norminfo.id
     *
     * @param id
     *            the value for scale_norminfo.id
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column scale_norminfo.name
     *
     * @return the value of scale_norminfo.name
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column scale_norminfo.name
     *
     * @param name
     *            the value for scale_norminfo.name
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column scale_norminfo.type
     *
     * @return the value of scale_norminfo.type
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    // public Byte getType() {
    // return type;
    // }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column scale_norminfo.type
     *
     * @param type
     *            the value for scale_norminfo.type
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    // public void setType(Byte type) {
    // this.type = type;
    // }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column scale_norminfo.createtime
     *
     * @return the value of scale_norminfo.createtime
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    public Date getCreatetime() {
        return createtime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column scale_norminfo.createtime
     *
     * @param createtime
     *            the value for scale_norminfo.createtime
     * @mbggenerated Sat Oct 03 18:22:11 CST 2015
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public long getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(long createuserid) {
        this.createuserid = createuserid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAreaid() {
        return areaid;
    }

    public void setAreaid(int areaid) {
        this.areaid = areaid;
    }

    public int getOrglevel() {
        return orglevel;
    }

    public void setOrglevel(int orglevel) {
        this.orglevel = orglevel;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public String getEditer() {
        return editer;
    }

    public void setEditer(String editer) {
        this.editer = editer;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }

}