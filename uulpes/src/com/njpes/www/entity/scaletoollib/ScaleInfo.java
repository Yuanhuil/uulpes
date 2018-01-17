package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;
import java.util.List;

public class ScaleInfo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String code;
    Integer id;
    String title;
    String shortname;
    String scaletype;
    List<String> suitegrades;
    Integer questionnum;
    Long examtime;
    String creationtime;
    Integer scalesource;

    public String getCode() {
        return code;
    }

    public void setCode(String uuid) {
        this.code = uuid;
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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getScaletype() {
        return scaletype;
    }

    public void setScaletype(String scaletype) {
        this.scaletype = scaletype;
    }

    public List<String> getSuitegrades() {
        return suitegrades;
    }

    public void setSuitegrades(List<String> suitegrades) {
        this.suitegrades = suitegrades;
    }

    public Integer getQuestionnum() {
        return questionnum;
    }

    public void setQuestionnum(Integer qnum) {
        this.questionnum = qnum;
    }

    public Long getExamtime() {
        return examtime;
    }

    public void setExamtime(Long examtime) {
        this.examtime = examtime;
    }

    public String getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(String creationtime) {
        this.creationtime = creationtime;
    }

    public Integer getScalesource() {
        return scalesource;
    }

    public void setScalesource(Integer scalesource) {
        this.scalesource = scalesource;
    }

}
