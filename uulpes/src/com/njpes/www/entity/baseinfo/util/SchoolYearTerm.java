package com.njpes.www.entity.baseinfo.util;

public class SchoolYearTerm {

    private String schoolyear;

    private String term;

    private String termName;

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

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    @Override
    public String toString() {
        return schoolyear + "#" + term + "#" + termName;
    }

}
