package edutec.scale.online;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.njpes.www.dao.assessmentcenter.ExamdoMapper;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.exam.ExamConsts;
//import edutec.scale.exam.ExamQuery;
import edutec.scale.exam.ExamResult;
import edutec.scale.exam.Examdo;

@Component("ExamSpace")
@Scope("prototype")
public class ExamSpace implements ExamConsts, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2626323506785720113L;
    /**
     * 
     */
    private static final Log logger = LogFactory.getLog(ExamSpace.class);
    private static final String EXAM_SPACE_KEY = ExamSpace.class.getSimpleName();
    private static final String ONLINEEXAM_KEY = OnlineExam.class.getName();
    public static final String EXAM_IMG_KEY = ExamSpace.class.getName() + ".imgdata";

    private HttpServletRequest req;
    private Object user;
    private Map<String, Examdo> examdoContext;
    // @Autowired
    // OnlineExam onlineExam;
    @Autowired
    ExamdoMapper examDoMapper;
    private long taskid;// 任务id
    private long resultid;// 结果id

    static public ExamSpace getInstance(HttpServletRequest req) {
        ExamSpace examSpace = (ExamSpace) WebUtils.getSessionAttribute(req, EXAM_SPACE_KEY);
        if (examSpace == null) {
            examSpace = new ExamSpace();
            WebUtils.setSessionAttribute(req, EXAM_SPACE_KEY, examSpace);
            logger.info("WebUtils.setSessionAttribute(req, EXAM_SPACE_KEY, examSpace)");
        }
        /* 每次都要设置req，保持最新的req */
        examSpace.setReq(req);
        return examSpace;
    }

    static public void clearSession(HttpServletRequest req) {
        logger.info("clearSession...");
        ExamSpace examSpace = (ExamSpace) WebUtils.getSessionAttribute(req, EXAM_SPACE_KEY);
        if (examSpace != null) {
            if (examSpace.examdoContext != null) {
                examSpace.examdoContext.clear();
                examSpace.examdoContext = null;
            }
        }
        WebUtils.setSessionAttribute(req, EXAM_SPACE_KEY, null);
        WebUtils.setSessionAttribute(req, ONLINEEXAM_KEY, null);
        WebUtils.setSessionAttribute(req, EXAM_IMG_KEY, null);
    }

    public void clearOnlineExam() {
        // onlineExam = null;
    }

    public ExamSpace() {
    }

    public HttpServletRequest getReq() {
        return req;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getImgData() {
        Object result = WebUtils.getSessionAttribute(req, EXAM_IMG_KEY);
        return (Map<String, Object>) result;
    }

    public void createOnlineExam() throws Exception {
        OnlineExam onlineExam1 = (OnlineExam) WebUtils.getSessionAttribute(req, ONLINEEXAM_KEY);
        if (onlineExam1 != null) {
            onlineExam1.listener.getQuestionnaire().clear();
            onlineExam1 = null;
            WebUtils.setSessionAttribute(req, ONLINEEXAM_KEY, null);
            if (logger.isDebugEnabled())
                logger.debug("clear online exam...");
        }
        // 此时的user内容，已经为量表而填写完毕相应的信息
        // onlineExam = new OnlineExam(req, user);
        OnlineExam onlineExam = (OnlineExam) SpringContextHolder.getBean("OnlineExam", OnlineExam.class);
        onlineExam.init(req, user);
        WebUtils.setSessionAttribute(req, ONLINEEXAM_KEY, onlineExam);
        if (logger.isDebugEnabled())
            logger.debug("WebUtils.setSessionAttribute(req, ONLINEEXAM_KEY, onlineExam)");
    }

    public OnlineExam getOnlineExam() {
        OnlineExam onlineExam = (OnlineExam) WebUtils.getSessionAttribute(req, ONLINEEXAM_KEY);
        if (onlineExam == null) {
            throw new RuntimeException("测验对象不存在Session中！");
        }
        /* 每次都要设置req，保持最新的req */
        onlineExam.setReq(req);
        return onlineExam;
    }

    public void nextPage(HashMap<Object, Object> page) throws Exception {
        OnlineExam onlineExam = getOnlineExam();
        try {
            byte examState = onlineExam.nextPage(page);
            page.put(FTL_USER_KEY, user);
            if (examState == ExamMachine.STATE_CLOSURE) {
                /*
                 * 设置完成时间,在系统中用户可能要同时做多个题， 页面要添加显示完成时间，为了性能，在这里添加 完成时间
                 */
                // if (visitRoom == ROOM_APP) {
                // String scaleId = onlineExam.getScaleId();
                // getExamdoList();
                // Examdo ed = examdoContext.get(scaleId);
                // ed.setOkTime(new Date());
                // }
                // String okTime = (new java.text.SimpleDateFormat("yyyy-MM-dd
                // hh:mm:ss")).format(new Date());
                // Map para =
                // UtilMisc.toMap("taskid",taskid,"okTime",okTime,"resultid",resultid);
                // examDoMapper.setOkTime(para);

                // page.put("oktime", okTime);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
            // e.printStackTrace();
            // logger.error(e.getMessage());
        }
    }

    /**
     * 获得测试结果
     * 
     * @return
     */
    public ExamResult getReqExamResultDetail() {
        /*
         * ExamQuery qry = ExamQuery.instance(); long reusltId =
         * ServletRequestUtils.getLongParameter(req,
         * Constants.EXAMRESULT_ID_PROP, -1L); int visitRoom =
         * ServletRequestUtils.getIntParameter(req, Constants.VISITROOM_PROP,
         * 0); int userFlag = ServletRequestUtils.getIntParameter(req,
         * Constants.FLAG_PROP, 0); if (reusltId == -1) { throw new
         * RuntimeException("编号有问题。"); } if (userFlag == 0) { userFlag =
         * user.getFlag(); } ExamResult examResult = qry.getExamResult(reusltId,
         * userFlag, visitRoom); if (examResult == null) {
         * logger.error("测试结果不存在，请查明原因"); } return examResult;
         */
        return null;
    }

    /**
     * 获得所有的测试结果
     * 
     * @return
     */
    public List<ExamResult> getReqExamResultDetails() {
        // ExamQuery qry = ExamQuery.instance();
        // <ExamResult> examResults = qry.getExamResultListForUser(user.getId(),
        // user.getFlag());
        // return examResults;
        return null;
    }

    /**
     * 设置req，并根据account设置user
     * 
     * @param req
     */
    public void setReq(HttpServletRequest req) {
        this.req = req;
        if (user == null) {
            // Account accountInSession = (Account)
            // WebUtils.getSessionAttribute(req, "account");
            // Validate.notNull(accountInSession, "session内的用户为空，没有用户登录");
            // user = accountInSession.getUser();
            // ExamUtils.loadUser(user);
            // int visitRoom = ROOM_APP;
            // if (visitRoom<=0) {
            // 默认的房间是应用程序
            // visitRoom = ServletRequestUtils.getIntParameter(req,
            // Constants.VISITROOM_PROP, ExamConsts.ROOM_APP);
            // }
            /*
             * if (RoleFlags.isAnonymity(user.getFlag()) ||
             * RoleFlags.isIndividual(user.getFlag())) { if (visitRoom ==
             * ExamConsts.ROOM_APP) { throw new
             * IllegalArgumentException("您不能进入系统测试环境"); } }
             */
            // ExamUtils.setVisitRoomForUser(user, visitRoom);
        }
    }

    /**
     * 获得要做的测试列表
     * 
     * @return
     */
    public Collection<Examdo> getExamdoList() {
        /*
         * if (examdoContext == null) { if (logger.isDebugEnabled())
         * logger.debug("getExamdoList..."); ExamQuery q = ExamQuery.instance();
         * examdoContext = q.getExamdoForUser(user); } return
         * examdoContext.values();
         */
        return null;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    public long getResultid() {
        return resultid;
    }

    public void setResultid(long resultid) {
        this.resultid = resultid;
    }

}
