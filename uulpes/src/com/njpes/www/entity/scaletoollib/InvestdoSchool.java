package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class InvestdoSchool implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 708176460165039372L;
    private long id;
    private long taskid;
    private long orgid;
    private int scaleid;
    private String starttime;
    private String endtime;
    private String oktime;
    private int objecttype;
    private int roleid;
    private long bjid;
    private int gradeid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public int getScaleid() {
        return scaleid;
    }

    public void setScaleid(int scaleid) {
        this.scaleid = scaleid;
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

    public String getOktime() {
        return oktime;
    }

    public void setOktime(String oktime) {
        this.oktime = oktime;
    }

    public int getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(int objecttype) {
        this.objecttype = objecttype;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public long getBjid() {
        return bjid;
    }

    public void setBjid(long bjid) {
        this.bjid = bjid;
    }

    public int getGradeid() {
        return gradeid;
    }

    public void setGradeid(int gradeid) {
        this.gradeid = gradeid;
    }

}
