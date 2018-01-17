package edutec.scale.online;

import java.io.Serializable;
import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.descriptor.ScaleReportDescriptor;
import edutec.scale.exam.ExamConsts;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.model.Scale;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.questionnaire.QuestionnaireCalcListener;
import edutec.scale.questionnaire.QuestionnaireCompistionListener;
import edutec.scale.questionnaire.QuestionnaireReportListener;
import edutec.scale.questionnaire.QuestionnaireStoreListener;
import edutec.scale.util.ScaleUtils;
import heracles.util.UtilCollection;

/**
 * 在ExamSpace中被构建实例，而ExamSpace被sevelet使用
 * 
 * @author
 */
@Component("OnlineExam")
@Scope("prototype")
public class OnlineExam implements ExamConsts, Serializable {
    @Autowired
    private CachedScaleMgr cachedScaleMgr;

    private static final Log logger = LogFactory.getLog(OnlineExam.class);

    public static final String PARAM_SCALE_ID = "scaleId";

    /* 答题参数 */
    private Scale scale;

    // @Autowired
    private Questionnaire questionnaire;
    @Autowired
    private ExamMachine onlineExamMachine;
    private RequestParser reqParser;
    private HttpServletRequest request;
    @Autowired
    public QuestionnaireCompistionListener listener;
    @Autowired
    private QuestionnaireCalcListener qCalcListener;
    @Autowired
    private QuestionnaireReportListener qRptListener;
    @Autowired
    private QuestionnaireStoreListener qStoreListener;

    @Autowired
    private ScaleReportDescriptor reportDescription;

    private String scaleId;

    public OnlineExam() {

    }

    /**
     * 三角度量表，含对学生的他评量表，所以他评时必有scaleId含一个他评量表和他评的对象
     * 
     * @param req
     * @param user
     * @throws QuestionnaireException
     * @throws NoSuchObjectException
     */
    public void init(HttpServletRequest req, Object user) throws QuestionnaireException, NoSuchObjectException {

        /* 创建量表服务， */

        /* 建立监听器 */
        listener.removeAllQuestionnaireListener();
        listener.addQuestionnaireListener(qCalcListener); // 计算题目分数和维度分数
        listener.addQuestionnaireListener(qStoreListener); // 存储结果,并为页面提供答题时间
        listener.addQuestionnaireListener(qRptListener);// 产生报告主要部分

        long resultid = Integer.parseInt(req.getParameter("resultid"));
        long taskid = Integer.parseInt(req.getParameter("taskid"));
        String teachervisible = req.getParameter("teachervisible");
        String studentvisible = req.getParameter("studentvisible");
        String parentvisible = req.getParameter("parentvisible");
        String normid = req.getParameter("normid");
        String threeAngleVersion = req.getParameter("typeflag");
        scaleId = req.getParameter(PARAM_SCALE_ID);
        String version = req.getParameter("v");
        if (version != null) {
            if (version.equals("2"))
                scaleId = ScaleUtils.getThreeAngleScaleForTeacherByScaleId(scaleId);
            else if (version.equals("3"))
                scaleId = ScaleUtils.getThreeAngleScaleForParentByScaleId(scaleId);
            threeAngleVersion = version;
        }
        // 量表+用户的id
        List<String> scaleid_ = UtilCollection.toList(scaleId, '|');
        Validate.notEmpty(scaleId, "量表id不能为空");
        /* 找到量表 */
        scale = cachedScaleMgr.get(scaleid_.get(0), true);
        Validate.notNull(scale, "此量表[" + scaleId + "]为空");
        // 心检卡测试产权量表时
        // 使用心检卡直接输入卡号进入,要为每个量表新建立一份对象，因为原来的设计是针对学生用户的
        /* 依据此量表建立问卷，并打开问卷 , 2010-2-24添加true，表示增加测谎题 */
        questionnaire = new Questionnaire();
        questionnaire.setScale(scale);
        questionnaire.setTaskId(taskid);
        questionnaire.setResultId(resultid);
        questionnaire.setTeachervisible(teachervisible);
        questionnaire.setStudentvisible(studentvisible);
        questionnaire.setParentvisible(parentvisible);
        questionnaire.setNormid(normid);
        questionnaire.setThreeAngleVersion(threeAngleVersion);
        questionnaire.setQuestionnaireLister(listener);
        questionnaire.setSubjectUserInfo(user);// 这里需要加入examdo的个人信息
        listener.setQuestionnaire(questionnaire);
        setObserOrSubjectUser(user, scaleid_); // 对lister设置用户
        questionnaire.open(null);

        // reportDescription = new ScaleReportDescriptor();
        reportDescription.clearDimList();
        reportDescription.setQuestionnaire(questionnaire);
        // reportDescription.setResultId(resultId);//用于三角视获取教师、家长测试结果
        // reportDescription.setThreeAngleUUID(threeAngleUUID);

        reportDescription.setObserverUserInfo(user);
        reportDescription.setSubjectUserInfo(user);
        questionnaire.setDescriptor(reportDescription);

        /* 创建测试机 */
        // onlineExamMachine = new ExamMachine(questionnaire);
        onlineExamMachine.resetExamMashine();
        onlineExamMachine.setQuestionnaire(questionnaire);

        // reqParser = new RequestParser();
        // reqParser.setFromQuery(true);
        // if (logger.isInfoEnabled()) {
        // logger.info(SystemInfo.JVMStatistics());
        // }

        // setReq(req);
        request = req;

    }

    /**
     * scaleid_含量表和主题用户(被他评的用户)的信息,如果只含一个量表，表示在做自评量表
     * 
     * @param user
     * @param scaleid_
     */
    private void setObserOrSubjectUser(Object user, List<String> scaleid_) {
        if (scaleid_.size() == 1) {
            listener.setSubjectUserInfo(user);
        } else {
            String stuId = scaleid_.get(1);
            // StudentImpl studentImpl = new StudentImpl();
            // studentImpl.setObjectIdentifier(Long.valueOf(stuId));
            // ExamUtils.loadUser(studentImpl);
            // ExamUtils.setVisitRoomForUser(studentImpl, ExamConsts.ROOM_APP);
            // listener.setObserverUser(user);
            // listener.setSubjectUser(studentImpl);
        }
    }

    public void setReq(HttpServletRequest req) {
        if (req != null)
            // reqParser.setReq(req);
            this.request = req;
    }

    public byte nextPage(HashMap<Object, Object> page) throws Exception {
        // byte examState =
        // reqParser.getByteParameter(RequestParamConsts.EXAM_STATE);
        String ss = request.getParameter("s");
        String qs = request.getParameter("q");
        String as = request.getParameter("a");
        String ts = request.getParameter("t");
        String st = request.getParameter("st");// 题干
        String stt = request.getParameter("stt");// 题干类型
        String js = request.getParameter("j");
        String timeout = request.getParameter("timeout");
        String countdown = request.getParameter("countdown");// 倒计时剩余时间
        // Map params = request.getParameterMap();
        int a, s, q, t;
        // Object temps = params.get(RequestParamConsts.EXAM_STATE);
        // String sss = request.getQueryString();
        if (ss == null) {
            s = -1;
        } else {
            // String ss= ((String[])temps)[0];
            s = Integer.parseInt(ss);
        }
        // Object tempq = params.get(RequestParamConsts.QUESTION);
        if (qs == null) {
            q = -1;
        } else {
            // String qq = ((String[])tempq)[0];
            q = Integer.parseInt(qs);
        }
        // s = Integer.parseInt(temps.toString());
        byte examState = (byte) s;
        if (examState == ExamMachine.STATE_SCALE_QUESTION) {
            /* 题目索引号 */
            // int questionIdx =
            // reqParser.getIntParameter(RequestParamConsts.QUESTION);
            int questionIdx = q;
            if (questionIdx == -1) {
                throw new QuestionnaireException("没有题目索引");
            }
            String answer = null;
            String text = null;
            if (as == null)
                answer = null;
            else {
                answer = as;
            }
            if (ts == null)
                text = null;
            else
                text = ts;

            // answer = reqParser.getStringParameter(RequestParamConsts.ANSWER,
            // null);
            // answer = params.get(RequestParamConsts.ANSWER).toString();
            // text = reqParser.getStringParameter(RequestParamConsts.EXAM_TEXT,
            // null);
            // text = params.get(RequestParamConsts.EXAM_TEXT).toString();
            onlineExamMachine.setAnswer(answer);
            onlineExamMachine.setReqQuestionIdx(questionIdx);
            onlineExamMachine.setSubjoin(text);
            onlineExamMachine.setSubjectTitle(st);
            onlineExamMachine.setSubjectTitleType(stt);
            if (js != null)
                onlineExamMachine.setJudgeAnswer(js);
            if (timeout != null && timeout.equals("yes"))
                onlineExamMachine.setTimeout(true);

            if (!StringUtils.isEmpty(countdown))
                onlineExamMachine.setCountdown(Integer.parseInt(countdown));
        }
        // 向页面传递信息
        page.put(FTL_SCALE_KEY, scale); // 再结束时scale为空，所以先置此变量
        examState = onlineExamMachine.nextState(page);
        page.put(RequestParamConsts.EXAM_STATE, examState);
        return examState;
    }

    public String getScaleId() {
        return scaleId;
    }

}
