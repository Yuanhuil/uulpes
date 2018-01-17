package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

public class QueryInfo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String code;
    Integer id;
    String title;
    Integer typeid;
    Integer assesstypeid;
    Integer source;
    Integer grade;
    Boolean prewarn;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean getPrewarn() {
        return prewarn;
    }

    public void setPrewarn(Boolean prewarn) {
        this.prewarn = prewarn;
    }

    public Integer getAssesstypeid() {
        return assesstypeid;
    }

    public void setAssesstypeid(Integer assesstypeid) {
        this.assesstypeid = assesstypeid;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
