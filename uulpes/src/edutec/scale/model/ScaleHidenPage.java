package edutec.scale.model;

import java.io.Serializable;

public class ScaleHidenPage implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3446883232667274945L;
    String id;
    String testType; // 自评类型
    String source; // 量表来源
    String scaleType; // 量表类型
    String applicablePerson; // 适用人群
    String reportGraph; // 图表
    long examineTime; // 测试时间

    public long getExamineTime() {
        return examineTime;
    }

    public void setExamineTime(long examineTime) {
        this.examineTime = examineTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
