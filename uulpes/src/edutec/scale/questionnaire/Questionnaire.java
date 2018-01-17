package edutec.scale.questionnaire;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edutec.scale.descriptor.Descriptor;
import edutec.scale.exception.CalcException;
import edutec.scale.exception.DimensionException;
import edutec.scale.exception.QuestionException;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.model.Dimension;
import edutec.scale.model.Question;
import edutec.scale.model.Scale;
import heracles.util.OrderValueMap;
import heracles.util.UtilMisc;
import net.sf.json.JSONObject;

/**
 * 问卷对象
 * 
 * @author Administrator
 */
@Scope("prototype")
@Component("Questionnaire")
public class Questionnaire {
    private final Log logger = LogFactory.getLog(getClass());
    private Scale scale; // 量表
    private QuestionnaireListener questionnaireLister;
    private OrderValueMap<String, DimensionBlock> dimsMap = new OrderValueMap<String, DimensionBlock>();
    private OrderValueMap<String, QuestionBlock> quesMap = new OrderValueMap<String, QuestionBlock>();
    private OrderValueMap<String, QuestionBlock> indivMap = null;

    private Object subjectUserInfo;// 答题人信息，added by zhaowanfeng
    private Object subjectUser;

    private Descriptor descriptor = null;
    private Timestamp startTime;
    private Timestamp okTime;
    private int testtype;// 测试类型，导入，自测，录入等
    private String threeangletest;
    private boolean loadExplain = true;
    private int numOfLies;
    private boolean addLies = false;
    private JSONObject detectRule;
    private String title;
    private String scaleId;
    private boolean warningOrNot = false;// 是否预警
    private int warningGrade;// 告警等级
    private int validVal = 1;// 是否有效
    private long taskId;
    private long resultId;// 测试结果在数据库的编号，added by zhaowanfeng
    private String threeAngleUUID;
    private String threeAngleVersion;
    private int numofQuestions;
    private Object examdo;
    private String studentvisible;
    private String teachervisible;
    private String parentvisible;
    private String normid;

    public Object getSubjectUser() {
        return subjectUser;
    }

    public void setSubjectUser(Object subjectUser) {
        this.subjectUser = subjectUser;
    }

    public Object getSubjectUserInfo() {
        return subjectUserInfo;
    }

    public void setSubjectUserInfo(Object subjectUserInfo) {
        this.subjectUserInfo = subjectUserInfo;
    }

    // added by zhaowanfeng
    public void setAddLies(boolean addLies) {
        this.addLies = addLies;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public void setQuestionnaireLister(QuestionnaireListener questionnaireLister) {
        this.questionnaireLister = questionnaireLister;
    }

    public void setScaleId(String scaleId) {
        this.scaleId = scaleId;
    }

    public int getNumofQuestions() {
        return scale.getQuestionNum();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLoadExplain() {
        return loadExplain;
    }

    public boolean isWarningOrNot() {
        return warningOrNot;
    }

    public void setWarningOrNot(boolean warningOrNot) {
        this.warningOrNot = warningOrNot;
    }

    public int getValidVal() {
        return validVal;
    }

    public void setValidVal(int validVal) {
        this.validVal = validVal;
    }

    public int getWarningGrade() {
        return warningGrade;
    }

    public void setWarningGrade(int warningGrade) {
        this.warningGrade = warningGrade;
    }

    public long getResultId() {
        return resultId;
    }

    public void setResultId(long resultId) {
        this.resultId = resultId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public void setLoadExplain(boolean loadExplain) {
        this.loadExplain = loadExplain;
    }

    public String getThreeAngleUUID() {
        return threeAngleUUID;
    }

    public void setThreeAngleUUID(String threeAngleUUID) {
        this.threeAngleUUID = threeAngleUUID;
    }

    public String getThreeAngleVersion() {
        return threeAngleVersion;
    }

    public void setThreeAngleVersion(String threeAngleVersion) {
        this.threeAngleVersion = threeAngleVersion;
    }

    public boolean getIsItem() {
        return ArrayUtils.indexOf(
                new String[] { "111001", "121211", "121212", "121213", "211001", "221211", "221212", "221213", "26" },
                scale.getId()) == ArrayUtils.INDEX_NOT_FOUND;
    }

    // added by zhaowanfeng,用于在springmvc中注册bean
    public Questionnaire() {

    }

    public Questionnaire(Scale scale, QuestionnaireListener questionnaireLister) {
        this.scale = scale; /* 放在最前面，保证与此对象相联系的对象正确的使用 */
        this.questionnaireLister = questionnaireLister;
        if (questionnaireLister != null) {
            questionnaireLister.setQuestionnaire(this);
        }
    }

    public Questionnaire(Scale scale, QuestionnaireListener questionnaireLister, boolean addLies) {
        this.scale = scale; /* 放在最前面，保证与此对象相联系的对象正确的使用 */
        this.questionnaireLister = questionnaireLister;
        if (questionnaireLister != null) {
            questionnaireLister.setQuestionnaire(this);
        }
        this.addLies = addLies;
    }

    private void makeBlocksAndRemountDimensions() {
        makeQuestionBlock();
        makeDimensionBlock();
        /* 建立起维度、与子维度，维度与问题之间的关系 */
        dimsMap.optimizeListStorage();
        quesMap.optimizeListStorage();
        if (indivMap != null)
            indivMap.optimizeListStorage();
        for (DimensionBlock dimseg : getDimensionBlocks()) {
            dimseg.mount();
        }
    }

    public void open(Map<Object, Object> params) throws QuestionnaireException {
        if (logger.isDebugEnabled())
            logger.debug("open Questionnaire...");
        makeBlocksAndRemountDimensions();
        if (questionnaireLister != null) {
            questionnaireLister.onOpen(params);
        }
    }

    public void openInvest() {
        makeQuestionBlock();
        quesMap.optimizeListStorage();
    }

    // 答题
    public void answer(int questionIdx, String answer, boolean isIndividual) throws QuestionnaireException {
        questionnaireLister.onAnswerQuesiton(questionIdx, answer, isIndividual);
    }

    public void setQuestionSubjoin(int questionIdx, String subjoin) {
        QuestionBlock blk = findQuestionBlock(questionIdx, false);
        blk.setSubjoin(subjoin);
    }

    /**
     * 答题结束
     * 
     * @param cxt
     *            答题结束后的结果上下文,如果不为空表示的是导入数据
     * @throws QuestionnaireException
     */

    // added by zhaowanfeng
    public void close(Map<Object, Object> params) throws QuestionnaireException {
        if (questionnaireLister != null)
            questionnaireLister.onClose(params);
        this.resetBlock();
        if (params != null) {
            if (params.get("answerimport") != null) // 如果是答题导入，不清空
                return;
            clear();
        }
    }

    public QuestionnaireListener getQuestionnaireLister() {
        return questionnaireLister;
    }

    public void clear() {
        if (logger.isDebugEnabled())
            logger.debug("Questionnaire clear...");
        if (dimsMap != null)
            dimsMap.clear();
        if (indivMap != null)
            indivMap.clear();
        if (quesMap != null)
            quesMap.clear();
        dimsMap = null;
        indivMap = null;
        quesMap = null;
    }

    private void makeQuestionBlock() {
        List<Question> questionList = scale.getQuestions();
        Validate.notEmpty(questionList, "量表问题不能为空");
        List<Question> tmepList = null;
        // 3.0的测谎题存在问题，目前暂时不用测谎题，后期再加。赵万锋
        /*
         * LieItem lieItem = LieThink.getLieItem(this.getScaleId()); if (addLies
         * && lieItem != null && lieItem.getType() == Detected.question) {
         * LieInsertPosition insertPosition; try { insertPosition =
         * lieItem.makeLieInsertPosition(); tmepList =
         * insertPosition.insertLies(this); numOfLies = lieItem.getNumOfAdded();
         * } catch (Exception e) { logger.error(e.getMessage(), e); throw new
         * RuntimeException(e); } } else {
         */
        tmepList = questionList;
        // }
        if (quesMap == null)
            quesMap = new OrderValueMap<String, QuestionBlock>();// 赵万锋临时添加。如果没有结束答题而再次点击做题，此对象为空
        for (Question q : tmepList) {
            quesMap.put(q.getId(), new QuestionBlock(q, this));
        }
    }

    private void makeDimensionBlock() {
        List<Dimension> dimensionList = scale.getDimensions();
        if (dimsMap == null)
            dimsMap = new OrderValueMap<String, DimensionBlock>();// 赵万锋临时添加
        for (Dimension d : dimensionList) {
            dimsMap.put(d.getId(), new DimensionBlock(d, this));
        }
        // 赵万锋临时添加，因为三角视没有W0维度，但是得分等级需要W0维度

        /*
         * if (ScaleUtils.isThirdAngleScale(getScaleId())) { Dimension dim0 =
         * new Dimension("W0"); dim0.setTitle("总分"); DimensionBlock dimBlk0 =
         * new DimensionBlock(dim0,this); getDimensionMap().put("W0",dimBlk0 );
         * }
         */
    }

    /**
     * 通过编号获得问题
     * 
     * @param questionId
     * @return
     */
    public QuestionBlock findQuestionBlock(String questionId) {
        Validate.notEmpty(questionId, "问题编号不能为空");
        QuestionBlock quesseg = quesMap.get(questionId);
        Validate.notNull(quesseg, getTitle() + ":问题 No." + questionId + " 不存在");
        return quesseg;
    }

    /**
     * 按位置获得题目
     * 
     * @param questionIdx
     * @return
     */
    public QuestionBlock findQuestionBlock(int questionIdx, boolean isIndividual) {
        List<QuestionBlock> list = getQuestionBlocks(isIndividual);
        Validate.isTrue(questionIdx < list.size(), "索引号[" + questionIdx + "]超过题目范围[" + list.size() + "]");
        QuestionBlock quesseg = list.get(questionIdx);
        return quesseg;
    }

    /**
     * 按编号获得题目
     * 
     * @param questionIdx
     * @return
     */
    public QuestionBlock findIndivQuestionBlock(String questionId) {
        Validate.notEmpty(questionId, "个人问题编号不能为空");
        // Validate.notEmpty(indivMap, "个人信息没有被设置");
        if (indivMap == null) {
            return null;
        }
        QuestionBlock quesseg = indivMap.get(questionId);
        return quesseg;
    }

    public DimensionBlock findDimensionBlock(String dimensionId) {
        DimensionBlock dimseg = dimsMap.get(dimensionId);
        // Validate.notNull(dimseg, "维度 No." + dimensionId + " 不存在");
        return dimseg;
    }

    public DimensionBlock findDimensionBlockByName(String title) {
        for (DimensionBlock dimseg : getDimensionBlocks()) {
            String name = dimseg.getTitle();
            if (name == title)
                return dimseg;
        }
        return null;
    }

    public List<QuestionBlock> getQuestionBlocks(boolean isIndividual) {
        if (!isIndividual)
            return quesMap.valueList();
        else if (indivMap != null) {
            return indivMap.valueList();
        } else {
            return Collections.emptyList();
        }
    }

    public List<QuestionBlock> getQuestionBlocks() {
        return getQuestionBlocks(false);
    }

    public List<QuestionBlock> getValidQuestionBlocks() {
        List<QuestionBlock> list = getQuestionBlocks(false);
        List<QuestionBlock> result = new ArrayList<QuestionBlock>(list.size());
        for (QuestionBlock blk : list) {
            if (blk.getQuestion().isLie() | blk.getQuestion().isPlaceholder() | blk.getQuestion().isTemplate()) {
                continue;
            }
            result.add(blk);
        }
        return result;
    }

    public List<DimensionBlock> getDimensionBlocks() {
        return dimsMap.valueList();
    }

    public OrderValueMap<String, DimensionBlock> getDimensionMap() {
        return dimsMap;
    }

    public List<DimensionBlock> getRootDimensionBlocks() {
        List<DimensionBlock> result = new ArrayList<DimensionBlock>();
        for (DimensionBlock dimensionBlock : getDimensionBlocks()) {
            if (dimensionBlock.isRoot()) {
                result.add(dimensionBlock);
            }
        }
        return result;
    }

    /**
     * 得到某题的分数
     * 
     * @param questionId
     * @return
     * @throws QuestionException
     */
    public Number findQuestionScore(String questionId) throws CalcException {
        QuestionBlock quess = findQuestionBlock(questionId);
        return quess.getScore();
    }

    /**
     * 按位置获得问题的分数
     * 
     * @param questionIdx
     *            题目位置
     * @return
     * @throws QuestionException
     */
    public Number findQuestionScore(int questionIdx, boolean isIndividual) throws CalcException {
        QuestionBlock quess = findQuestionBlock(questionIdx, isIndividual);
        return quess.getScore();
    }

    /**
     * 得到某维度的分数
     * 
     * @param questionId
     * @return
     * @throws DimensionException
     */
    public Number findDimensionRawScore(String dimensionId) throws DimensionException {
        DimensionBlock demseg = findDimensionBlock(dimensionId);
        return demseg.getRawScore();
    }

    public String getDescn() {
        return scale.getDescn();
    }

    public String getScaleId() {
        return StringUtils.defaultIfEmpty(scaleId, scale.getId());
    }

    public String getTitle() {
        return StringUtils.defaultIfEmpty(title, scale.getTitle());
    }

    public Dimension findDimension(String dimensionId) {
        return scale.findDimension(dimensionId);
    }

    public Question findQuestion(String questionId) {
        return scale.findQuestion(questionId);
    }

    public List<Question> getQuestions() {
        return scale.getQuestions();
    }

    public List<Dimension> getDimensions() {
        return scale.getDimensions();
    }

    public int getQuestionSize() {
        return quesMap.size();
    }

    public String getComputor() {
        return scale.getCalcExp();
    }

    public int getDimensionSize() {
        return scale.getDimensionSize();
    }

    public String getGuidance() {
        return scale.getGuidance();
    }

    public String getType() {
        return scale.getType();
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public String getDataTablename() {
        return scale.getDataTablename();
    }

    public String getTextTablename() {
        return scale.getTextTablename();
    }

    public String getReportTpl() {
        return scale.getReportname();
    }

    public Map<String, Object> getFactorDetail() {
        List<QuestionBlock> list = getQuestionBlocks(true);
        // Validate.notEmpty(list, "常模表数据获取需要“因素”数据！");
        Map<String, Object> result = new HashMap<String, Object>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            for (QuestionBlock quesBlk : list) {
                try {
                    result.put(quesBlk.getId(), quesBlk.getScore());
                } catch (Exception e1) {
                    throw new RuntimeException(e1.getMessage());
                }
            }
        }
        return result;
    }

    // added by zhaowanfeng
    public Map<String, Object> getUserInfoDetail() {
        List<QuestionBlock> list = getQuestionBlocks(true);
        // Validate.notEmpty(list, "常模表数据获取需要“因素”数据！");
        Map<String, Object> result = null;
        try {
            result = UtilMisc.objToHash(this.getSubjectUserInfo());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getFlag() {
        return scale.getFlag();
    }

    public Scale getScale() {
        return scale;
    }

    public int getCountNeedQuestion() {
        return scale.getCountNeedQuestion() + numOfLies;
    }

    public int getTesttype() {
        return testtype;
    }

    public void setTesttype(int testtype) {
        this.testtype = testtype;
    }

    public String getThreeangletest() {
        return threeangletest;
    }

    public void setThreeangletest(String threeangletest) {
        this.threeangletest = threeangletest;
    }

    public Timestamp getOkTime() {
        return okTime;
    }

    public void setOkTime(Timestamp okTime) {
        this.okTime = okTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Object getExamdo() {
        return examdo;
    }

    public void setExamdo(Object examdo) {
        this.examdo = examdo;
    }

    public String getStudentvisible() {
        return studentvisible;
    }

    public void setStudentvisible(String studentvisible) {
        this.studentvisible = studentvisible;
    }

    public String getTeachervisible() {
        return teachervisible;
    }

    public void setTeachervisible(String teachervisible) {
        this.teachervisible = teachervisible;
    }

    public String getParentvisible() {
        return parentvisible;
    }

    public void setParentvisible(String parentvisible) {
        this.parentvisible = parentvisible;
    }

    public String getNormid() {
        return normid;
    }

    public void setNormid(String normid) {
        this.normid = normid;
    }

    public void resetBlock() {
        List<QuestionBlock> list = getQuestionBlocks(false);
        for (QuestionBlock quesBlk : list) {
            quesBlk.setAnswer(null);
            quesBlk.setScore(null);
        }
        list = getQuestionBlocks(true);
        if (list != null) {
            for (QuestionBlock quesBlk : list) {
                quesBlk.setAnswer(null);
                quesBlk.setScore(null);
            }
        }
        List<DimensionBlock> dimList = getDimensionBlocks();
        for (DimensionBlock dimBlk : dimList) {
            dimBlk.clearCompleteFlag();
        }
    }

    public JSONObject getDetectRule() {
        if (detectRule == null) {
            Descriptor descriptor = getDescriptor();
            String detRuleVal = descriptor.getDescnPropStr("detect-rule", null);
            if (StringUtils.isNotEmpty(detRuleVal)) {
                detectRule = JSONObject.fromObject(detRuleVal);
            }
        }
        return detectRule;
    }
}
