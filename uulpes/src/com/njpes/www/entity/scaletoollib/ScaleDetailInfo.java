package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class ScaleDetailInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Integer id;
    String code;
    String title;
    String shortname;
    Integer typeid;
    Integer questionnum;
    Integer assesstype;
    Integer source;
    List<String> suitegrades;
    Timestamp creatiomtime;
    Integer flag;
    Integer startage;
    Integer endage;
    String reportchart;
    Boolean totalscore;
    Integer examtime;
    Boolean prewarn;
    String xmlstr;

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getAssesstype() {
        return assesstype;
    }

    public void setAssesstype(Integer assesstype) {
        this.assesstype = assesstype;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public List<String> getSuitegrades() {
        return suitegrades;
    }

    public void setSuitegrades(List<String> suitegrades) {
        this.suitegrades = suitegrades;
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

    public Integer getQuestion_num() {
        return questionnum;
    }

    public void setQuestion_num(Integer qnum) {
        this.questionnum = qnum;
    }

    public Timestamp getCreationtime() {
        return creatiomtime;
    }

    public void setCreationtime(Timestamp creatiomtime) {
        this.creatiomtime = creatiomtime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getStartage() {
        return startage;
    }

    public void setStartage(Integer startage) {
        this.startage = startage;
    }

    public Integer getEndage() {
        return endage;
    }

    public void setEndage(Integer endage) {
        this.endage = endage;
    }

    public Integer getExamtime() {
        return examtime;
    }

    public void setExamtime(Integer examtime) {
        this.examtime = examtime;
    }

    public Boolean getPrewarn() {
        return prewarn;
    }

    public void setPrewarn(Boolean prewarn) {
        this.prewarn = prewarn;
    }

    public String getReportchart() {
        return reportchart;
    }

    public void setReportchart(String reportchart) {
        this.reportchart = reportchart;
    }

    public Boolean getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(Boolean totalscore) {
        this.totalscore = totalscore;
    }

    public String getXmlstr() {
        return xmlstr;
    }

    public void setXmlstr(String xmlstr) {
        this.xmlstr = xmlstr;
    }
}
