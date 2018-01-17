package com.njpes.www.entity.workschedule;

/**
 * 页面查询封装类 目的是为了扩展页面查询可以不用修改controller
 */
public class JobNoticeWebQueryParam extends JobNoticeWithBLOBs {

    private String startTime;

    private String endTime;

    private boolean canAudit;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isCanAudit() {
        return canAudit;
    }

    public void setCanAudit(boolean canAudit) {
        this.canAudit = canAudit;
    }

}
