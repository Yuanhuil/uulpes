package edutec.group.domain;

public class DimDescription {
    private String scaleId;
    private String wid;
    private String firstStr;
    private String otherStr;
    private String advice;
    private int grade;

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getScaleId() {
        return scaleId;
    }

    public void setScaleId(String scaleId) {
        this.scaleId = scaleId;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getFirstStr() {
        return firstStr;
    }

    public void setFirstStr(String firstStr) {
        this.firstStr = firstStr;
    }

    public String getOtherStr() {
        return otherStr;
    }

    public void setOtherStr(String otherStr) {
        this.otherStr = otherStr;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

}
