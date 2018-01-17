package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class InvestTask implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 4805650223521576541L;
    private long id;// id号
    private String taskname;// 任务名称
    private int scaleid;// 任务量表
    private long ownerid;
    private long orgid;
    private String createtime;
    private String starttime;
    private String endtime;

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

    public int getScaleid() {
        return scaleid;
    }

    public void setScaleid(int scaleid) {
        this.scaleid = scaleid;
    }

    public long getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(long ownerid) {
        this.ownerid = ownerid;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
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

}
