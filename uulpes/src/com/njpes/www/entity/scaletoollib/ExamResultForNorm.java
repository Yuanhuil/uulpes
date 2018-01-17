package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class ExamResultForNorm implements Serializable {
    private static final long serialVersionUID = -1184620769808229259L;
    private int realgradeid;
    private int gender;
    private String dim_score;
    private String csrq;

    public int getRealgradeid() {
        return realgradeid;
    }

    public void setRealgradeid(int realgradeid) {
        this.realgradeid = realgradeid;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDim_score() {
        return dim_score;
    }

    public void setDim_score(String dim_score) {
        this.dim_score = dim_score;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

}
