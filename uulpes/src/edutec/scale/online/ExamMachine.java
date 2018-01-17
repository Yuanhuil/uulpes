package edutec.scale.online;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.njpes.www.dao.scaletoollib.ExamTempAnswerMapper;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.scaletoollib.ExamTempAnswer;

import edutec.scale.exception.QuestionnaireException;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.questionnaire.QuestionBlock;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.render.QuestionRender;
import edutec.scale.render.Render;
import edutec.scale.util.ExamUtils;
import edutec.scale.util.ScaleUtils;
import heracles.util.UtilDateTime;
import heracles.util.UtilMisc;
import heracles.web.util.HtmlStr;
import jodd.util.StringUtil;

/*
 * 答题机类记录答题时每一页的状态信息，包括下一页的状态信息。包括该页的模板，状态码 zwf
 * 
 */
@Component("ExamMachine")
@Scope("prototype")
public class ExamMachine {
    private final Log logger = LogFactory.getLog(getClass());
    /* 页面 */
    private static final String PAGE_FIRST = "firstPage";// 答题首页模板
    private static final String PAGE_GUIDLANG = "guidlang";// 引导语页面模板
    private static final String PAGE_CLOSURE = "closure";// 答题结束页面模板
    private static final String PAGE_REPORT = "report";// 答题结束后出现的报告模板

    public static final byte STATE_INDI_QUESTION = 1;
    public static final byte STATE_GUIDLANG = 2;
    public static final byte STATE_SCALE_QUESTION = 3;
    public static final byte STATE_CLOSURE = 4;
    public static final byte STATE_REPORT = 5;
    public static final byte STATE_CANCEL = 6;
    public static final byte ALPHA_Z = 0;

    private byte nextState = -1;
    private int questionIdx = -1; // 由-1开始
    private int reqQuestionIdx = 0;
    private String answer = null;
    private String nextPage = null;
    private Questionnaire questionnaire;
    private String subjoin;
    private int countNeedQuestion;
    private int passPlacehoderQuestion = 0;
    private int position = 0;
    private IntList qIdxList;
    private String subjectTitle;
    private String subjectTitleType;

    private String judgeAnswer;// 判断题答案
    private String currentJudgeId;// 当前判断题id
    private boolean isTimeout;
    private int countdown;// 剩余时间
    private List<ExamTempAnswer> examTempAnswerList = new ArrayList<ExamTempAnswer>();
    @Autowired
    ExamTempAnswerMapper examTempAnswerMapper;
    private String tempAnswerTable;

    public void resetExamMashine() {
        nextState = -1;
        questionIdx = -1;
        reqQuestionIdx = 0;
        answer = null;
        nextPage = null;
        if (questionnaire != null)
            questionnaire.clear();
        questionnaire = null;
        position = 0;
        subjoin = null;
        passPlacehoderQuestion = 0;
        countNeedQuestion = 0;
        qIdxList = null;
        currentJudgeId = "";
        subjectTitle = null;
        subjectTitleType = null;
        isTimeout = false;
        tempAnswerTable = null;
        countdown = 0;
    }

    public ExamMachine() {

    }

    public ExamMachine(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
        // countNeedQuestion = questionnaire.getCountNeedQuestion();
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
        countNeedQuestion = questionnaire.getCountNeedQuestion();
    }

    public byte nextState(HashMap<Object, Object> page) throws Exception {
        nextPage = null;
        if (-1 == nextState) {
            nextState = ALPHA_Z; /* 设置初始状态 */
            nextPage = PAGE_FIRST; /* 设置初始页 */
            String testtype = page.get("testtype").toString();
            if (testtype.equals("1"))
                questionnaire.setTesttype(1);
            if (testtype.equals("3"))
                questionnaire.setTesttype(3);
            String threeangletest = page.get("threeangletest") == null ? null : page.get("threeangletest").toString();
            questionnaire.setThreeangletest(threeangletest);
        } else if (nextState == ALPHA_Z) {
            nextState = STATE_GUIDLANG; /* 设置测试指导状态 */
            nextPage = PAGE_GUIDLANG; /* 设置测试指导页 */
            page.put("testtype", questionnaire.getTesttype());
            page.put("threeangletest", questionnaire.getThreeangletest());
        } else if (nextState == STATE_GUIDLANG) {
            nextState = STATE_SCALE_QUESTION; /* 设置量表答题状态 */
            initQuestionState(page);
            // questionIdx = getNextQuestionIdx();
            questionnaire.setStartTime(UtilDateTime.nowTimestamp());
            // questionIdx+=1;
            if (ScaleUtils.isMoralityScale(questionnaire.getScaleId())) {
                questionIdx = getNextTemplateQuestionId();
                nextTemplateQuestionPage(page, questionIdx);
            } else {
                questionIdx = getNextQuestionIdx();
                nextQuestionPage(page, questionIdx); /* 设置量表题目第一页 */
            }

        } else if (nextState == STATE_SCALE_QUESTION) {
            answerQuestionNextPage(page); /* 答题并设置量表题目的下一页 */
            if (questionIdx == qIdxList.size() - 1)
                page.put("lastquestion", "yes");
            // if (questionIdx >= questionnaire.getQuestionSize()) {
            if (questionIdx >= qIdxList.size()) {
                page.put("download", "no");
                questionnaire.close(page); // 关闭问卷,保存答案
                Timestamp okTime = questionnaire.getOkTime();
                DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String endTime = sdf.format(okTime);
                page.put("okTime", endTime);

                nextState = STATE_CLOSURE; /* 设置结束状态 */
                Object subjectuser = questionnaire.getSubjectUserInfo();
                if (subjectuser instanceof Student) {
                    String studentvisible = questionnaire.getStudentvisible();
                    if ("1".equals(studentvisible)) {
                        nextPage = PAGE_CLOSURE;
                    } else {
                        nextPage = "examclose";
                    }
                    page.put("typeflag", 1);
                }
                if (subjectuser instanceof Teacher) {
                    // String teachervisible =
                    // questionnaire.getStudentvisible();
                    // if("1".equals(teachervisible)){
                    nextPage = PAGE_CLOSURE;
                    // }
                    // else{
                    // nextPage = "examclose";
                    // }
                    page.put("typeflag", 2);
                }
                // nextPage = PAGE_CLOSURE; /* 设置结束页 */
            }
        } else if (nextState == STATE_CLOSURE) {
            // 这块一直没有运行
            nextState = STATE_REPORT; /* 设置报表状态 */
            nextPage = PAGE_REPORT; /* 设置报表页 */
        }
        if (logger.isDebugEnabled()) {
            logger.debug("nextState：" + this.nextState);
        }
        if (nextPage != null)
            // page.setTemplate(nextPage);
            page.put("template", nextPage);
        return nextState;
    }

    private void initQuestionState(HashMap<Object, Object> page) throws QuestionnaireException {
        questionIdx = -1; // 题目索引由-1开始
        position = 0; /* 题目的位置偏移0开始 */
        isTimeout = false;
        countdown = 0;

        Object user = questionnaire.getSubjectUserInfo();
        if (user instanceof Student) {
            Student stu = (Student) user;
            long acountid = stu.getAccountId();
            tempAnswerTable = ExamUtils.getStuPartAnswerTable(acountid);
        }
        if (user instanceof Teacher) {
            tempAnswerTable = "exam_tea_answer";
        }
        examTempAnswerList.clear();
        initQuestionIndex();// 初始化题目的索引号列表 -赵万锋
        countNeedQuestion = questionnaire.getNumofQuestions();// 赵万锋后加

        // 判断上次是否没有做完而进行了数据保护，那么从上一次结束的题目开始
        List<ExamTempAnswer> examTempAnswerList = examTempAnswerMapper.selectByResultId(tempAnswerTable,
                questionnaire.getResultId(), questionnaire.getScaleId());
        if (examTempAnswerList != null && examTempAnswerList.size() > 0) {
            for (int i = 0; i < examTempAnswerList.size(); i++) {
                ExamTempAnswer examAnswer = examTempAnswerList.get(i);
                int tempQind = examAnswer.getQindex();
                String answer = examAnswer.getAnswer();
                int countdowntime = examAnswer.getCountdown();

                questionnaire.answer(tempQind, answer, false);
                if (i == examTempAnswerList.size() - 1) {
                    if (countdowntime > 0)
                        page.put("countdown", countdowntime);
                    questionIdx = tempQind;
                    if (i == questionnaire.getScale().getQuestionNum() - 1) {// 如果数据保护最后一题刚好是整个量表的最后一题，这时候没有下一页，只能停留在最后一题
                        questionIdx = tempQind - 1;
                    }
                }
            }
        }

    }

    private void initQuestionIndex() {
        Object user = questionnaire.getSubjectUserInfo();
        String gender = null;
        if (user instanceof Student) {
            Student stu = (Student) user;
            gender = stu.getXbm();
            // if(gender.equals("男") || gender.equals("0"))

        }
        if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            gender = teacher.getXbm();
        }
        qIdxList = new ArrayIntList();
        List<Question> qList = questionnaire.getQuestions();
        int idx = 0;
        boolean genderM = false;
        boolean genderW = false;
        for (Question q : qList) {
            String qID = q.getId();

            if (qID.contains("QPD") && !qID.contains("_")) {// 判断题
                q.setType(QuestionConsts.TYPE_JUDGE);// 临时添加，表示这是判断题，不需要计数
            }

            if (qID.contains("QN")) {
                q.setType(QuestionConsts.TYPE_TITLE);// 临时添加，表示这是题干题，不需要计数
                genderM = false;
                genderW = false;
                if (gender.equals("男") || gender.equals("1"))// 如果是男的
                {
                    if (qID.contains("_M")) {
                        genderM = true;
                        genderW = false;
                    }
                    if (qID.contains("_W"))// 过滤掉女性题的题干
                    {
                        genderM = false;
                        genderW = true;
                        idx++;
                        continue;
                    }
                }
                if (gender.equals("女") || gender.equals("2")) {// 如果是女的
                    if (qID.contains("_M"))// 过滤掉男性题的题干
                    {
                        genderM = true;
                        genderW = false;
                        idx++;
                        continue;
                    }
                    if (qID.equals("_W")) {
                        genderW = true;
                        genderM = false;
                    }
                }
                // if(qID.contains("_M")){
                // genderQ = true;
                // }
                // else
                // genderQ = false;
                // idx++;
                // continue;
            }
            if (qID.contains("_")) {
                if (gender.equals("男") || gender.equals("1"))// 如果是男的
                {
                    if (genderM == true) {
                    }
                    if (genderW == true) {
                        idx++;
                        continue;
                    }
                }
                if (gender.equals("女") || gender.equals("2"))// 如果是男的
                {
                    if (genderM == true) {
                        idx++;
                        continue;
                    }
                }
            }
            qIdxList.add(idx);
            idx++;
        }

    }

    public int getQuestionIdx() {
        return questionIdx;
    }

    /**
     * 先回答问题，然后再递增显示下一道题,题目的模板也由QuestionRender决定，<br>
     * 见nextQuestionPage方法
     * 
     * @param page
     */
    private void answerQuestionNextPage(HashMap<Object, Object> page) throws Exception {
        List<QuestionBlock> questions = questionnaire.getQuestionBlocks(false);
        // if (questionIdx >= questions.size()) {
        if (questionIdx >= qIdxList.size()) {
            return;
        }

        int index = qIdxList.get(questionIdx);
        QuestionBlock qblk = questions.get(index);

        // 如果是道德量表，模板题。一次回答一组题
        if (ScaleUtils.isMoralityScale(questionnaire.getScaleId())) {

            // 此处保存答题临时结果，放在客户端掉电导致数据丢失
            ExamTempAnswer eta = new ExamTempAnswer();
            eta.setId(questionnaire.getResultId());
            String table = null;
            eta.setAnswer(answer);
            eta.setQid(qblk.getId());
            eta.setQindex(index);
            eta.setScaleid(Integer.parseInt(questionnaire.getScaleId()));
            examTempAnswerList.add(eta);
            if (examTempAnswerList.size() == 1) {
                Map param = new HashMap<String, Object>();
                param.put("table", tempAnswerTable);
                param.put("examTempAnswerList", examTempAnswerList);
                examTempAnswerMapper.insertBatch(param);
                examTempAnswerList.clear();
            }

            questionnaire.answer(index, answer, false);
            questionIdx = getNextTemplateQuestionId();
            nextTemplateQuestionPage(page, questionIdx);
            return;
        }

        // ------------------如果超出限制时间，直接跳到下一部分题目-------------------------------------------
        if (isTimeout) {
            String qInd = qblk.getId();
            while (!qInd.contains("QN")) {
                qblk.setScore(0);
                qblk = questions.get(index);
                questionIdx = getNextQuestionIdx();
                if (questionIdx >= qIdxList.size()) {
                    isTimeout = false;
                    return;
                }
                index = qIdxList.get(questionIdx);
                qblk = questions.get(index);
                qInd = qblk.getId();
            }
            nextQuestionPage(page, questionIdx);
            isTimeout = false;
            return;
        }
        // ---------------------------------------------------------------------------------------------------
        /*
         * 当用户刷新页面时，reqQuestionIdx == questionIdx，不相等且答案为空，就是刷新的情况
         * answer可能为空,如果不需要答题时，或者刷新页面时
         */
        if (!qblk.isFinish()) {
            // if (StringUtils.isNotBlank(answer) && reqQuestionIdx == index)
            // {//2016-2-5，赵万锋
            if (StringUtils.isNotBlank(answer) && reqQuestionIdx == questionIdx) {
                if (subjoin != null)
                    questionnaire.setQuestionSubjoin(index, subjoin);
                // 此处保存答题临时结果，放在客户端掉电导致数据丢失
                ExamTempAnswer eta = new ExamTempAnswer();

                eta.setId(questionnaire.getResultId());
                String table = null;
                eta.setAnswer(answer);
                eta.setQid(qblk.getId());
                eta.setQindex(index);
                eta.setScaleid(Integer.parseInt(questionnaire.getScaleId()));
                eta.setCountdown(countdown);
                examTempAnswerList.add(eta);
                if (examTempAnswerList.size() == 5) {
                    Map param = new HashMap<String, Object>();
                    param.put("table", tempAnswerTable);
                    param.put("examTempAnswerList", examTempAnswerList);
                    examTempAnswerMapper.insertBatch(param);
                    examTempAnswerList.clear();
                }

                questionnaire.answer(index, answer, false);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug(qblk.getId() + ":用户是做页面刷新");
                }
            }
        }

        /* 已经回答了问题，或不必回答可以向下移动题 */
        // if (qblk.isFinish() || !qblk.isMustAnswer()) {//暂时注释掉，赵万锋
        if (reqQuestionIdx == questionIdx) {
            // ++questionIdx;
            questionIdx = getNextQuestionIdx();
        }
        // }
        nextQuestionPage(page, questionIdx);
    }

    private int getNextVisibleQuestionIdx(boolean isIndividual) {
        List<QuestionBlock> questions = questionnaire.getQuestionBlocks(isIndividual);
        int idx = questionIdx + 1;
        for (; idx < questions.size(); ++idx) {
            QuestionBlock question = questions.get(idx);
            if (question.getQuestion().isVisible()) {
                break;
            }
        }
        return idx;
    }

    private int getNextVisibleQuestionIdx() {
        int idx = questionIdx + 1;
        return idx;
    }

    private int getNextQuestionIdx() throws QuestionnaireException {
        List<QuestionBlock> questions = questionnaire.getQuestionBlocks(false);
        int idx = questionIdx + 1;

        for (; idx < qIdxList.size(); ++idx) {
            int index = qIdxList.get(idx);
            QuestionBlock question = questions.get(index);
            String qId = question.getQuestion().getId();
            if (qId.contains("QPD") && qId.contains("_")) {// 如果是判断题，忽略。并且需要记录当前的判断题题干id，以便找到相应的判断题
                currentJudgeId = qId;
                continue;
            }
            // 如果是题干，忽略，但是题干必须存下来，用于和题干下面的题目一起显示.如果下一题依然是题干，则此题干为大题干，作为单独页显示
            else if (qId.contains("QN")) {
                QuestionBlock nextquestion = questions.get(index + 1);
                if (nextquestion != null) {
                    String nextquestionid = nextquestion.getQuestion().getId();
                    if (nextquestionid.contains("QN")) {
                        break;
                    } else {
                        currentJudgeId = "";
                        // subjectTitle =
                        // StringEscapeUtils.unescapeXml(question.getQuestion().getTitle());
                        subjectTitle = HtmlStr.decodeString(question.getQuestion().getTitle());
                        if (qId.contains("_M") || qId.contains("_W"))
                            subjectTitleType = qId;

                        continue;
                    }

                }
                // currentJudgeId = "";
                // subjectTitle =
                // StringEscapeUtils.unescapeXml(question.getQuestion().getTitle());
                // if(qId.contains("_M")||qId.contains("_W"))
                // subjectTitleType = qId;

                // continue;
            }

            else { // 处理判断题
                if (currentJudgeId.contains("_Y")) {
                    if (judgeAnswer.equals("0")) {
                        break;
                    }
                    if (judgeAnswer.equals("1")) {
                        continue;
                    }
                } else if (currentJudgeId.contains("_N")) {
                    if (judgeAnswer.equals("0")) {
                        continue;
                    }
                    if (judgeAnswer.equals("1")) {
                        break;
                    }
                } else {
                    if (StringUtil.isNotEmpty(subjectTitleType)) {
                        if (subjectTitleType.contains("_M") || subjectTitleType.contains("_W")) {
                            subjectTitle = null;
                            subjectTitleType = null;
                        }
                    }
                }
                break;
            }
        }
        return idx;
    }

    private int getNextTemplateQuestionId() {
        List<QuestionBlock> questions = questionnaire.getQuestionBlocks(false);
        int idx = questionIdx + 1;
        for (; idx < qIdxList.size(); ++idx) {
            int index = qIdxList.get(idx);
            QuestionBlock questionBlk = questions.get(index);
            String qId = questionBlk.getQuestion().getId();
            if (questionBlk.getQuestion().isTemplate()) {
                return idx;
            }
        }
        return idx;
    }

    @SuppressWarnings("unchecked")
    private void nextQuestionPage(HashMap<Object, Object> page, int qidx) {
        if (qidx >= qIdxList.size())
            return;
        List<Question> questions = questionnaire.getQuestions();
        int id = qIdxList.get(qidx);
        Question question = questions.get(id);

        // 如果是模板题，则另行处理
        if (ScaleUtils.isMoralityScale(questionnaire.getScaleId())) {
            if (question.isTemplate()) {
                nextTemplateQuestionPage(page, qidx);
                return;
            }
        }

        String jyLabel = null, jnLabel = null;
        // 以下处理判断题
        if (question.getType() == QuestionConsts.TYPE_JUDGE) {
            String jId = question.getId();
            String s = jId.substring(3);
            int jid = Integer.parseInt(s);
            String jY = "QPD" + String.valueOf(jid + 1) + "_Y";
            String jN = "QPD" + String.valueOf(jid + 1) + "_N";
            Question qY = questionnaire.findQuestion(jY);
            Question qN = questionnaire.findQuestion(jN);
            jyLabel = qY.getTitle();
            jnLabel = qN.getTitle();

        }
        String progress = "";
        if (question.isPlaceholder()) {
            passPlacehoderQuestion++;
        }
        /* 答题进度 */
        if (question.isPlaceholder()) {
            if (qidx <= 0) {
                progress = "开始";
            } else {
                progress = "已经完成" + position + "道题";
            }
        } else {
            position++;
            progress = question.getQIndex() + "/" + countNeedQuestion;
        }
        // 将大标题（题干）一直显示在答题页上
        String qID = question.getId();
        // if(qID.contains("QN"))
        // page.put("subjecttitle", question.getTitle());
        // else
        page.put("subjecttitle", subjectTitle);
        page.put("subjecttitletype", subjectTitleType);
        page.put("currentQIdx", question.getQIndex());
        page.put("totalQNum", countNeedQuestion);
        page.put("progress", progress);
        page.put("haspic", question.getHaspic());
        /* 题目 */
        page.put("headtitle", question.getHeadtitle());
        page.put("title", HtmlStr.decodeString(question.getTitle()));
        /* 题目索引 */
        page.put(RequestParamConsts.QUESTION, qidx);

        Object user = questionnaire.getSubjectUserInfo();
        String gender = null;
        String tester = "";
        if (user instanceof Student) {
            Student stu = (Student) user;
            tester = stu.getXm();
        }
        if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            tester = teacher.getXm();
        }
        page.put("tester", tester);// 答题人姓名

        // 设定按钮的标题
        String btnTitle = Render.TITLE_BTN_NQ;
        if (qidx == questions.size() - 1) { // 最后一道题
            btnTitle = Render.TITLE_BTN_END;
        }
        Map map = UtilMisc.toMap(Render.PARA_BTN_TITLE, btnTitle);
        if (question.getType() == QuestionConsts.TYPE_JUDGE) {
            map.put("jyLabel", jyLabel);
            map.put("jnLabel", jnLabel);
        }

        /* 题目显示 */
        QuestionRender.getInstance().doRender(question, page, map);

    }

    private void nextTemplateQuestionPage(HashMap<Object, Object> page, int qidx) {
        if (qidx >= qIdxList.size())
            return;
        List<Question> questions = questionnaire.getQuestions();
        int id = qIdxList.get(qidx);
        Question question = questions.get(id);
        Question templateQ = questions.get(id);

        // 大标题
        String qID = question.getId();
        page.put("headtitle", question.getTitle());

        // 判断题标题
        qidx++;
        question = questions.get(qidx);
        page.put("pdtitle", question.getTitle());

        String jyLabel = null, jnLabel = null;
        // 以下处理判断题
        if (question.getType() == QuestionConsts.TYPE_JUDGE) {
            String jId = question.getId();
            String s = jId.substring(3);
            int jid = Integer.parseInt(s);
            String jY = "QPD" + String.valueOf(jid) + "_Y";
            String jN = "QPD" + String.valueOf(jid) + "_N";
            Question qY = questionnaire.findQuestion(jY);
            Question qN = questionnaire.findQuestion(jN);
            jyLabel = qY.getTitle();
            jnLabel = qN.getTitle();

            String btnTitle = Render.TITLE_BTN_NQ;
            Map map = UtilMisc.toMap(Render.PARA_BTN_TITLE, btnTitle);
            if (question.getType() == QuestionConsts.TYPE_JUDGE) {
                page.put("jyLabel", jyLabel);
                page.put("jnLabel", jnLabel);
            }
            qidx++;
            String[] tempQList = new String[8];
            templateQ.setTemplateQeslist(tempQList);
            List qList1 = new ArrayList();
            for (int i = 1; i < 5; i++) {
                qidx++;
                id = qIdxList.get(qidx);
                question = questions.get(id);
                tempQList[i - 1] = question.getId();
                SelectionQuestion sQ = (SelectionQuestion) question;
                qList1.add(sQ);
            }
            page.put("qlist_yes", qList1);
            qidx++;
            List qList2 = new ArrayList();
            for (int i = 1; i < 5; i++) {
                qidx++;
                id = qIdxList.get(qidx);
                question = questions.get(id);
                tempQList[3 + i] = question.getId();
                SelectionQuestion sQ = (SelectionQuestion) question;
                qList2.add(sQ);
            }
            page.put("qlist_no", qList2);
        }

        /* 题目索引 */
        page.put(RequestParamConsts.QUESTION, qidx);

        Object user = questionnaire.getSubjectUserInfo();
        String gender = null;
        String tester = "";
        if (user instanceof Student) {
            Student stu = (Student) user;
            tester = stu.getXm();
        }
        if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            tester = teacher.getXm();
        }
        page.put("tester", tester);// 答题人姓名

        // 设定按钮的标题
        String btnTitle = Render.TITLE_BTN_NQ;
        if (qidx == questions.size() - 1) { // 最后一道题
            btnTitle = Render.TITLE_BTN_END;
        }
        Map map = UtilMisc.toMap(Render.PARA_BTN_TITLE, btnTitle);
        /* 题目显示 */
        // QuestionRender.getInstance().doRender(question, page, map);
        page.put("template", "moralityQuestion");

    }

    private void nextQuestionPage1(HashMap<Object, Object> page, int qidx) {
        boolean isIndividual = (nextState == STATE_INDI_QUESTION);

        List<QuestionBlock> questions = questionnaire.getQuestionBlocks(false);
        if (qidx >= questions.size()) {
            return;
        }

        Question question = questions.get(qidx).getQuestion();

        String progress = "";
        if (question.isPlaceholder()) {
            passPlacehoderQuestion++;
        }
        /* 答题进度 */
        if (question.isPlaceholder()) {
            if (position <= 0) {
                progress = "开始";
            } else {
                progress = "已经完成" + position + "道题";
            }
        } else {
            position++;
            if (!isIndividual) {
                // progress = (qidx - passPlacehoderQuestion + 1) + "/" +
                // countNeedQuestion;
                progress = position + "/" + countNeedQuestion;
            }
        }

        page.put("progress", progress);
        /* 题目 */
        page.put("title", HtmlStr.decodeString(question.getTitle()));
        /* 题目索引 */
        page.put(RequestParamConsts.QUESTION, qidx);

        // 设定按钮的标题
        String btnTitle = Render.TITLE_BTN_NQ;
        if (qidx == questions.size() - 1) { // 最后一道题
            if (!isIndividual) {
                btnTitle = Render.TITLE_BTN_END;
            } else {
                btnTitle = "进入指导页";
            }
        }
        Map map = UtilMisc.toMap(Render.PARA_BTN_TITLE, btnTitle);

        /* 题目显示 */
        QuestionRender.getInstance().doRender(question, page, map);

    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getReqQuestionIdx() {
        return reqQuestionIdx;
    }

    public void setReqQuestionIdx(int reqQuestionIdx) {
        this.reqQuestionIdx = reqQuestionIdx;
    }

    public String getSubjoin() {
        return subjoin;
    }

    public void setSubjoin(String subjoin) {
        this.subjoin = subjoin;
    }

    public void setSubjectTitle(String title) {
        this.subjectTitle = title;
    }

    public String getSubjectTitleType() {
        return subjectTitleType;
    }

    public void setSubjectTitleType(String subjectTitleType) {
        this.subjectTitleType = subjectTitleType;
    }

    public void setJudgeAnswer(String judgeAnswer) {
        this.judgeAnswer = judgeAnswer;
    }

    public void setCurrentJudgeId(String currentJudgeId) {
        this.currentJudgeId = currentJudgeId;
    }

    public void setTimeout(boolean isTimeout) {
        this.isTimeout = isTimeout;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

}
