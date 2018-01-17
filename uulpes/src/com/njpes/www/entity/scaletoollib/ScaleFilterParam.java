package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class ScaleFilterParam implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String scaleSourceId;
    private String scaleTypeId;
    private String scaleId;
    private String applicablePerson;
    private String isWarn;

    public ScaleFilterParam() {
        this.scaleSourceId = "-1";
        this.scaleTypeId = "-1";
        this.scaleId = "";
        this.applicablePerson = "-1";
        this.isWarn = "-1";
    }

    public String getScaleSourceId() {
        return scaleSourceId;
    }

    public void setScaleSourceId(String scaleSourceId) {
        this.scaleSourceId = scaleSourceId;
    }

    public String getScaleTypeId() {
        return scaleTypeId;
    }

    public void setScaleTypeId(String scaleTypeId) {
        this.scaleTypeId = scaleTypeId;
    }

    public String getScaleId() {
        return scaleId;
    }

    public void setScaleId(String scaleId) {
        this.scaleId = scaleId;
    }

    public String getApplicablePerson() {
        return applicablePerson;
    }

    public void setApplicablePerson(String applicablePerson) {
        this.applicablePerson = applicablePerson;
    }

    public String getIsWarn() {
        return isWarn;
    }

    public void setIsWarn(String isWarn) {
        this.isWarn = isWarn;
    }
}
