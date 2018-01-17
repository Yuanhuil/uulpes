package edutec.scale.model;

import java.io.Serializable;

public class ScaleHomePage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4161478935538003181L;
    String name; // 首页记录量表名称
    String abbr; // 量表英文简称
    String intro; // 量表简介
    String instr; // 量表指导语
    String testType; // 自评类型
    String source; // 量表来源
    String scaleType; // 量表类型
    String applicablePerson; // 适用人群
    String reportGraph; // 图表
    String endWord;

    public String getEndWord() {
        return endWord;
    }

    public void setEndWord(String endWord) {
        this.endWord = endWord;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getScaleType() {
        return scaleType;
    }

    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
    }

    public String getApplicablePerson() {
        return applicablePerson;
    }

    public void setApplicablePerson(String applicablePerson) {
        this.applicablePerson = applicablePerson;
    }

    public String getReportGraph() {
        return reportGraph;
    }

    public void setReportGraph(String reportGraph) {
        this.reportGraph = reportGraph;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getInstr() {
        return instr;
    }

    public void setInstr(String instr) {
        this.instr = instr;
    }
}
