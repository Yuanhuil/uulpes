package com.njpes.www.entity.assessmentcenter;

public class InvesttaskWithBLOBs extends Investtask {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column investtask.areanames
     * 
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    private String areanames;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column investtask.schoolnames
     * 
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    private String schoolnames;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column investtask.bjnames
     * 
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    private String bjnames;

    private String bjids;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column investtask.rolenames
     * 
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    private String rolenames;

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column investtask.areanames
     * 
     * @return the value of investtask.areanames
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    public String getAreanames() {
        return areanames;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column investtask.areanames
     * 
     * @param areanames
     *            the value for investtask.areanames
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    public void setAreanames(String areanames) {
        this.areanames = areanames;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column investtask.schoolnames
     * 
     * @return the value of investtask.schoolnames
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    public String getSchoolnames() {
        return schoolnames;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column investtask.schoolnames
     * 
     * @param schoolnames
     *            the value for investtask.schoolnames
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    public void setSchoolnames(String schoolnames) {
        this.schoolnames = schoolnames;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column investtask.bjnames
     * 
     * @return the value of investtask.bjnames
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    public String getBjnames() {
        return bjnames;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column investtask.bjnames
     * 
     * @param bjnames
     *            the value for investtask.bjnames
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    public void setBjnames(String bjnames) {
        this.bjnames = bjnames;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column investtask.rolenames
     * 
     * @return the value of investtask.rolenames
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    public String getRolenames() {
        return rolenames;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column investtask.rolenames
     * 
     * @param rolenames
     *            the value for investtask.rolenames
     * @mbggenerated Thu Dec 31 15:49:38 CST 2015
     */
    public void setRolenames(String rolenames) {
        this.rolenames = rolenames;
    }

    public String getBjids() {
        return bjids;
    }

    public void setBjids(String bjids) {
        this.bjids = bjids;
    }

    // 添加两个标志位,一个下发flag（1：显示；0：不显示），一个撤回flag(1:显示；0：不显示)
    private int xfflag;

    private int chflag;

    private int expireflag;

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

}