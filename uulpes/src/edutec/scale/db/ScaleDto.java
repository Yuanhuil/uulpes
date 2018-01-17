package edutec.scale.db;

import java.io.Serializable;
import java.util.Date;

public class ScaleDto implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private String code;
    private String shortname;
    private int assesstype;
    private int source;
    private int startage;
    private int endage;
    private String reportchart;
    private boolean prewarn;
    private boolean totalscore;
    private long examtime;
    private String createdBy;
    private Date creationTime;
    private long orgId;
    private int typeId;
    private int flag;
    private int questionNum;
    private String title;
    private String normGradeOrAgeFlag;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public int getAssesstype() {
        return assesstype;
    }

    public void setAssesstype(int assesstype) {
        this.assesstype = assesstype;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getStartage() {
        return startage;
    }

    public void setStartage(int startage) {
        this.startage = startage;
    }

    public int getEndage() {
        return endage;
    }

    public void setEndage(int endage) {
        this.endage = endage;
    }

    public String getReportchart() {
        return reportchart;
    }

    public void setReportchart(String reportchart) {
        this.reportchart = reportchart;
    }

    public boolean isPrewarn() {
        return prewarn;
    }

    public void setPrewarn(boolean prewarn) {
        this.prewarn = prewarn;
    }

    public boolean isTotalscore() {
        return totalscore;
    }

    public void setTotalscore(boolean totalscore) {
        this.totalscore = totalscore;
    }

    public long getExamtime() {
        return examtime;
    }

    public void setExamtime(long examtime) {
        this.examtime = examtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public String getNormGradeOrAgeFlag() {
        return normGradeOrAgeFlag;
    }

    public void setNormGradeOrAgeFlag(String normGradeOrAgeFlag) {
        this.normGradeOrAgeFlag = normGradeOrAgeFlag;
    }

}
