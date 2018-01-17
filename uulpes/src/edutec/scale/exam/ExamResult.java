package edutec.scale.exam;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
//import com.njpes.www.entity.baseinfo.UserInfo;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.model.Scale;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.questionnaire.QuestionnaireListener;
import edutec.scale.util.QuestionnaireUtils;
import edutec.scale.util.ScaleUtils;
import heracles.util.SimpleDateFormat;
import heracles.util.UtilCollection;
import heracles.util.UtilDateTime;

public class ExamResult {

    // public CachedScaleMgr cachedScaleMgr;

    @Override
    public boolean equals(Object obj) {
        ExamResult o = (ExamResult) obj;
        return this.scaleId == o.scaleId;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(scaleId).hashCode();
    }

    private final Log logger = LogFactory.getLog(getClass());
    public static final String LOOK = "详细查看";
    Organization organization;

    // ==============================
    // 测试结果部分
    private long id;
    private long taskid;
    /**
     * 量表id，所答的量表id
     */
    private int scaleId;
    /**
     * 个人问题的答案与分数 //fomat Q1:1:0\n, 编号：选择的项索引号(答案):分数<br>
     */
    private String individualScore;
    /**
     * 实际题目的答案与分数 fomat Q1:1:0\n, 编号：选择的项索引号(答案):分数<br>
     */
    private String questionScore;
    /**
     * 维度分数 //fomat W1:23:34\n, 维度编号：粗分:标准分<br>
     */
    private String dimScore;
    private int testtype;// 测试类型：1，：答题；2：答案导入；3：答题录入
    private Timestamp okTime;
    private Timestamp startTime;
    private int second;

    private int visitRoom;

    private String dimTitles;
    private String dimValues;
    // 为了三视角，三视角需要存储各自的标准分
    private String dimStdValues;

    private String warningValues;

    private String scoreGradeValues;

    private int version = 0;// 三角视，标志家长版、教师版、学生版

    private String threeAngleUUID;
    // 用户部分
    private long userId;
    private int userRoleFlag;
    private String userName;
    private String csrq;
    private String xmpy;
    private String mzm;
    private String userGender;
    private long userOrgId;
    private String xxmc;
    private int userGradeOrderId = -1;
    private long userClassId;
    private String userClassTitle;
    private String xh;
    private String gh;
    private String sfzjh;
    private int xd;
    private int grade;// 用户答题时的年级
    private long bj;// 用户答题时的班级id
    private int nj;
    private String njmc;
    private String bjmc;
    private String rolename;
    private String qxmc;// 区县名称
    private int roleid = -1;
    private String attrs;// 背景信息
    private Object observerUser;
    private Object subjectUser;
    private int warningGrade = 0;
    private int validVal = 1; // 判断测试结果是否有效，1-有效，0-无效
    // added by zhaowanfeng
    private String userCode;
    private Object observerUserInfo;
    private Object subjectUserInfo;
    private HashMap<String, Number> warningMap;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Object getObserverUserInfo() {
        return observerUserInfo;
    }

    public void setObserverUserInfo(Object observerUserInfo) {
        this.observerUserInfo = observerUserInfo;
    }

    public Object getSubjectUserInfo() {
        return subjectUserInfo;
    }

    public void setSubjectUserInfo(Object subjectUserInfo) {
        this.subjectUserInfo = subjectUserInfo;
    }

    public int getValidVal() {
        return validVal;
    }

    public void setValidVal(int validVal) {
        this.validVal = validVal;
    }

    public ExamResult(Organization organization) {
        this.organization = organization;
    }

    public ExamResult() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String gender) {
        this.userGender = gender;
    }

    public long getUserOrgId() {
        return userOrgId;
    }

    public void setUserOrgId(long orgId) {
        this.userOrgId = orgId;
    }

    public int getUserGradeOrderId() {
        return userGradeOrderId;
    }

    public void setUserGradeOrderId(int gradeOrderId) {
        this.userGradeOrderId = gradeOrderId;
    }

    public long getUserClassId() {
        return userClassId;
    }

    public void setUserClassId(long classId) {
        this.userClassId = classId;
    }

    public String getUserClassTitle() {
        return userClassTitle;
    }

    public void setUserClassTitle(String classTitle) {
        this.userClassTitle = classTitle;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * 为了传给ibats的params中的表名
     * 
     * @return
     */
    // public String getTable() {
    // return ExamUtils.getExamResultTable(userRoleFlag, visitRoom);
    // }
    public String getTable() {
        if (subjectUserInfo instanceof Student)
            return ExamUtils.getExamResultTable(1);
        else if (subjectUserInfo instanceof Teacher)
            return ExamUtils.getExamResultTable(2);
        return "";
    }

    /**
     * 为了传给ibats的params中的表名
     * 
     * @return
     */
    public String getDimTable1() {
        return ExamUtils.getExamDimResultTable(userRoleFlag, visitRoom);
    }

    public String getDimTable() {
        // return ExamUtils.getExamDimResultTable(userRoleFlag,
        // visitRoom);//原始代码，modified bu zhaowanfeng
        // return
        // ExamUtils.getExamDimResultTable(this.subjectUserInfo.getUserType());
        if (subjectUserInfo instanceof Student)
            return ExamUtils.getExamDimResultTable(1);
        else if (subjectUserInfo instanceof Teacher)
            return ExamUtils.getExamDimResultTable(2);
        return "";
    }

    public int getVisitRoom() {
        return visitRoom;
    }

    public void setVisitRoom(int visitRoom) {
        this.visitRoom = visitRoom;
    }

    public Timestamp getOkTime() {
        return okTime;
    }

    public void setOkTime(Timestamp okTime) {
        this.okTime = okTime;
    }

    public String getScaleTitle() {
        Scale scale = getCachedScaleMgr().get(scaleId + "");
        return scale.getTitle();
    }

    public Scale getScale() {
        return getCachedScaleMgr().get(scaleId + "");
    }

    public int getScaleId() {
        return scaleId;
    }

    public String getFinishTime() {
        return SimpleDateFormat.format(getOkTime(), SimpleDateFormat.DATE_PATTEN_TM);
    }

    public void setScaleId(int scaleId) {
        this.scaleId = scaleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getIndividualScore() {
        return individualScore;
    }

    public void setIndividualScore(String individualSore) {
        this.individualScore = individualSore;
    }

    public String getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(String questionSore) {
        this.questionScore = questionSore;
    }

    public String getDimScore() {
        return dimScore;
    }

    public void setDimScore(String dimSore) {
        this.dimScore = dimSore;
    }

    /**
     * 看别人的报告时，将使用这个方法，载入用户的属性
     * 
     * @return
     */
    /*
     * public User getUser() { User user = organization.createUser(getUserId(),
     * getUserRoleFlag()); PropObject propObject = (PropObject) user;
     * propObject.load(); return user; }
     */

    public Map<String, Double> getDims(String dimResults) {
        Map<String, String> dims = UtilCollection.compositConvertMap(dimResults, '#', ',');
        Map<String, Double> dims2 = new HashMap<String, Double>();
        for (Map.Entry<String, String> en : dims.entrySet()) {
            String scores[] = UtilCollection.toArray(en.getValue(), ',');
            dims2.put(en.getKey(), Double.valueOf(scores[1]));
        }
        return dims2;
    }

    public void setFieldValFromQuestionnaire(Questionnaire questionnaire) {
        try {
            setScaleId(Integer.valueOf(questionnaire.getScaleId()));
            // String answers1 =
            // QuestionnaireUtils.buildAnswerReslut(questionnaire, true);
            String answers2 = QuestionnaireUtils.buildAnswerReslut(questionnaire, false);
            // 放置维度结果，因为有的量表的维度分两级，所以这里保留所有的维度（一级和/或二级）
            String dimResults = QuestionnaireUtils.buildDimensionReslut(questionnaire);

            // 设置警告值
            // if(questionnaire.hasWarning())//如果有预警
            // {
            // ExamWarningAndScoreGrade statis = new
            // ExamWarningAndScoreGrade(questionnaire);
            // }

            // 保存每个维度的预警等级
            // String dimWarningResults =
            // QuestionnaireUtils.buildDimensionWarningResult(questionnaire);
            // setWarningValues(dimWarningResults);

            // String dimScoreGradeResults =
            // QuestionnaireUtils.buildDimensionScoreGradeResult(questionnaire);
            // setScoreGradeValues(dimScoreGradeResults);

            // 设置量表测试有效值1-有效（默认） 0-无效,暂时注释，赵万锋
            // if (!LieThink.validate(questionnaire)){
            // this.setValidVal(0);
            // }

            // 放置维度结果，为了统计分析，所以将一级维度以及它的特定分数，存入另外一个表中
            String[] dimAndVals = QuestionnaireUtils.buildDimAndVals(questionnaire, false);

            // 设置量表分数（题目和维度）
            // setIndividualScore(answers1); // 个人信息分数
            setQuestionScore(answers2); // 问题信息分数
            setDimScore(dimResults); // 所有维度信息分数
            // 将存入另一个表的一级维度以及它的分数。用于统计分析的维度分数
            setDimTitles(dimAndVals[0]);
            setDimValues(dimAndVals[1]);
            if (ScaleUtils.isThirdAngleScale(questionnaire.getScaleId())) {
                dimAndVals = QuestionnaireUtils.buildDimAndVals(questionnaire, true);
                this.setDimStdValues(dimAndVals[1]);
                setThreeAngleUUID(questionnaire.getThreeAngleUUID());// 设置三角视识别码
            }
            if (questionnaire.getThreeAngleVersion() == null) {
                this.setVersion(AcountTypeFlag.student.getId());
            } else {
                this.setVersion(Integer.parseInt(questionnaire.getThreeAngleVersion()));
            }

            // setThreeAngleUUID(questionnaire.getThreeAngleUUID());//设置三角视识别码
            if (questionnaire.getStartTime() != null) {
                setStartTime(questionnaire.getStartTime());
            }
            if (questionnaire.getOkTime() == null)
                questionnaire.setOkTime(UtilDateTime.nowTimestamp()); // 测试时间
            setOkTime(questionnaire.getOkTime());
            setTesttype(questionnaire.getTesttype());
            setId(questionnaire.getResultId());
            setTaskid(questionnaire.getTaskId());
            setWarningGrade(questionnaire.getWarningGrade());// 设置告警等级，并存到结果表。
            setValidVal(questionnaire.getValidVal());

        } catch (Exception e) {
            logger.error("出现错误", e);
        }
    }

    // added by zhaowanfeng
    public void setFieldValFromUserInfo(Object userInfo) {
        if (userInfo instanceof Student) {
            Student student = (Student) userInfo;
            setUserId(student.getId());
            setUserGender(student.getXbm());
            setMzm(student.getMzm());
            setXmpy(student.getXmpy());
            setUserOrgId(student.getOrgid());
            setUserGradeOrderId(student.getGradecodeid());
            setUserClassId(student.getBjid());
        }
        if (userInfo instanceof Teacher) {
            Teacher teacher = (Teacher) userInfo;
            setUserId(teacher.getId());
            setUserGender(teacher.getXbm());
            setMzm(teacher.getMzm());
            setXmpy(teacher.getXmpy());
            setUserOrgId(teacher.getOrgid());
        }
        // setUserCode(userInfo.getUserID());
        // setUserId(Integer.parseInt(userInfo.getUserID()));
        // setUserGender(userInfo.getGender());
        // setVisitRoom(userInfo.getVisitRoom());

    }

    public void buildAndSetDimScore(List<QuestionnaireUtils.DimensionInfo> list) {
        this.setDimScore(QuestionnaireUtils.generateDimScoreStr(list));
    }

    /**
     * 将已经保存在数据库中的测试结果，转换为问卷对象
     * 
     * @param questionnaireListener
     * @return
     */
    public Questionnaire toQuestionnaire(QuestionnaireListener questionnaireListener) {
        Validate.notNull(questionnaireListener.getObserverUser());
        Scale scale = getCachedScaleMgr().get(scaleId + "", true);
        Object user = questionnaireListener.getObserverUser();
        Questionnaire questionnaire = new Questionnaire(scale, questionnaireListener);
        try {
            questionnaire.open(null);
            // 设置个人信息分数
            QuestionnaireUtils.fillQuestionContentFromStr(questionnaire, getIndividualScore(), true);
            // 设置题目的分数
            QuestionnaireUtils.fillQuestionContentFromStr(questionnaire, getQuestionScore(), false);
            // 设置维度维度的分数
            QuestionnaireUtils.fillDimContentFromStr(questionnaire, getDimScore());
            // 设置答题时间
            questionnaire.setOkTime(getOkTime());

            return questionnaire;
        } catch (QuestionnaireException e) {
            throw new RuntimeException(e);
        }
    }

    // added by zhaowanfeng
    public Questionnaire toNewQuestionnaire(QuestionnaireListener questionnaireListener) {
        Validate.notNull(questionnaireListener.getObserverUserInfo());
        Scale scale = getCachedScaleMgr().get(scaleId + "", true);
        if (scale == null)
            return null;
        // UserInfo user = questionnaireListener.getObserverUserInfo();

        Questionnaire questionnaire = new Questionnaire(scale, questionnaireListener);
        try {
            questionnaire.open(null);
            // 设置个人信息分数
            // QuestionnaireUtils.fillQuestionContentFromStr(questionnaire,
            // getIndividualScore(), true);
            // 设置题目的分数
            QuestionnaireUtils.fillQuestionContentFromStr(questionnaire, getQuestionScore(), false);
            // 设置维度维度的分数
            QuestionnaireUtils.fillDimContentFromStr1(questionnaire, getDimScore());//
            // 设置答题时间
            questionnaire.setOkTime(getOkTime());

            return questionnaire;
        } catch (QuestionnaireException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将已经保存在数据库中的测试结果，转换为问卷对象
     * 
     * @param questionnaireListener
     * @return
     */
    public Questionnaire toQuestionnaireA(QuestionnaireListener questionnaireListener) {
        Scale scale = getCachedScaleMgr().get(scaleId + "", true);
        Questionnaire questionnaire = new Questionnaire(scale, questionnaireListener);
        try {
            questionnaire.open(null);
            // 设置个人信息分数
            QuestionnaireUtils.fillQuestionContentFromStr(questionnaire, getIndividualScore(), true);
            // 设置维度维度的分数
            QuestionnaireUtils.fillDimContentFromStr(questionnaire, getDimScore());
            // 设置答题时间
            questionnaire.setOkTime(getOkTime());
            return questionnaire;
        } catch (QuestionnaireException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUserRoleFlag() {
        return userRoleFlag;
    }

    public void setUserRoleFlag(int userFlag) {
        this.userRoleFlag = userFlag;
    }

    public int getTesttype() {
        return testtype;
    }

    public void setTesttype(int testtype) {
        this.testtype = testtype;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getUserGradeName() {
        // return
        // organization.getSchoolPartEnum().getGradeName(getUserGradeOrderId() +
        // "");
        return "";
    }

    public String getUserRoleName() {
        // return RoleFlags.ROLE_DESCN.get(getUserRoleFlag() + "");
        return "";
    }

    public String getDimTitles() {
        return dimTitles;
    }

    public void setDimTitles(String dimTitles) {
        this.dimTitles = dimTitles;
    }

    public String getDimValues() {
        return dimValues;
    }

    public void setDimValues(String dimValues) {
        this.dimValues = dimValues;
    }

    public Object getObserverUser() {
        return observerUser;
    }

    public void setObserverUser(Object observerUser) {
        this.observerUser = observerUser;
    }

    public Object getSubjectUser() {
        return subjectUser;
    }

    public void setSubjectUser(Object subjectUser) {
        this.subjectUser = subjectUser;
    }

    public String getDimStdValues() {
        return dimStdValues;
    }

    public void setDimStdValues(String dimStdValues) {
        this.dimStdValues = dimStdValues;
    }

    public int getWarningGrade() {
        return warningGrade;
    }

    public void setWarningGrade(int warningGrade) {
        this.warningGrade = warningGrade;
    }

    public HashMap<String, Number> getWarningMap() {
        return warningMap;
    }

    public void setWarningMap(HashMap<String, Number> warningMap) {
        this.warningMap = warningMap;
    }

    public String getWarningValues() {
        return warningValues;
    }

    public void setWarningValues(String warningValues) {
        this.warningValues = warningValues;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getThreeAngleUUID() {
        return threeAngleUUID;
    }

    public void setThreeAngleUUID(String threeAngleUUID) {
        this.threeAngleUUID = threeAngleUUID;
    }

    public String getDuration() {
        if (okTime != null && startTime != null && okTime.after(startTime)) {
            return DurationFormatUtils.formatDuration(okTime.getTime() - startTime.getTime(), "HH:mm:ss");
        }
        return StringUtils.EMPTY;
    }

    public String getScoreGradeValues() {
        return scoreGradeValues;
    }

    public void setScoreGradeValues(String scoreGradeValues) {
        this.scoreGradeValues = scoreGradeValues;
    }

    public CachedScaleMgr getCachedScaleMgr() {
        return SpringContextHolder.getBean("CachedScaleMgr", CachedScaleMgr.class);

    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public long getBj() {
        return bj;
    }

    public void setBj(long bj) {
        this.bj = bj;
    }

    public int getXd() {
        return xd;
    }

    public void setXd(int xd) {
        this.xd = xd;
    }

    public int getNj() {
        return nj;
    }

    public void setNj(int nj) {
        this.nj = nj;
    }

    public String getNjmc() {
        return njmc;
    }

    public void setNjmc(String njmc) {
        this.njmc = njmc;
    }

    public String getBjmc() {
        return bjmc;
    }

    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
    }

    public String getQxmc() {
        return qxmc;
    }

    public void setQxmc(String qxmc) {
        this.qxmc = qxmc;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getXxmc() {
        return xxmc;
    }

    public void setXxmc(String xxmc) {
        this.xxmc = xxmc;
    }

    public String getXmpy() {
        return xmpy;
    }

    public void setXmpy(String xmpy) {
        this.xmpy = xmpy;
    }

    public String getMzm() {
        return mzm;
    }

    public void setMzm(String mzm) {
        this.mzm = mzm;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getGh() {
        return gh;
    }

    public void setGh(String gh) {
        this.gh = gh;
    }

    public String getSfzjh() {
        return sfzjh;
    }

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    public String getAttrs() {
        return attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

}
