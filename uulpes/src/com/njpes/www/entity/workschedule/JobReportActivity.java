package com.njpes.www.entity.workschedule;

import java.util.Date;

public class JobReportActivity {

    private Date startTime;

    private Date endTime;

    private String plancatalog;

    private String orgLevel;

    private String queryOrgtype;

    private String schoolyear;

    private String term;

    public String getSchoolyear() {
        return schoolyear;
    }

    public void setSchoolyear(String schoolyear) {
        this.schoolyear = schoolyear;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getQueryOrgtype() {
        return queryOrgtype;
    }

    public void setQueryOrgtype(String queryOrgtype) {
        this.queryOrgtype = queryOrgtype;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPlancatalog() {
        return plancatalog;
    }

    public void setPlancatalog(String plancatalog) {
        this.plancatalog = plancatalog;
    }

}