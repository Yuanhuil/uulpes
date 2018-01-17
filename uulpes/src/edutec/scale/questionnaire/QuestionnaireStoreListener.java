package edutec.scale.questionnaire;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edutec.scale.exam.ExamResult;
import edutec.scale.exam.ExamResultMgr;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.online.OnlineExam;

@Scope("prototype")
@Component("QuestionnaireStoreListener")
public class QuestionnaireStoreListener extends QuestionnaireListener {
    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ExamResultMgr examResultMgr;

    public QuestionnaireStoreListener() {

    }

    /**
     * subjectUser的设置是在{@link OnlineExam}的构造函数中
     */

    public void onClose(Map<Object, Object> params) throws QuestionnaireException {
        try {
            ExamResult examResult = new ExamResult();
            // 记录谁测试报告，可以是测别人的，也可以测试自己的
            // examResult.setObserverUser(observerUser);
            // examResult.setSubjectUser(subjectUser);

            examResult.setObserverUserInfo(observerUserInfo);
            examResult.setSubjectUserInfo(subjectUserInfo);
            // 被测试用户的一些信息拷贝给examResult对象，被测用户可能是测试人，也可能是别人
            examResult.setFieldValFromUserInfo(subjectUserInfo);
            // 将问卷的一些信息拷贝给examResult对象
            examResult.setFieldValFromQuestionnaire(questionnaire);
            // 将时间信息拷贝给examResult对象
            // 由QuestionnaireReportListener传递下来
            /*
             * if (params != null) { String str = (String)
             * params.get(QuestionnaireReportListener.FLT_START_TM); if
             * (StringUtils.isNotEmpty(str)) { Date starttime =
             * DateUtils.parseDate(str, new String[] {
             * SimpleDateFormat.DATE_PATTEN_TM }); examResult.setStartTime(new
             * Timestamp(starttime.getTime())); } } if
             * (examResult.getStartTime() == null) {
             * examResult.setStartTime(UtilDateTime.nowTimestamp()); }
             */

            // 保存
            // saveResult(examResult);

            //筛查无效问卷
            if(!isValid(examResult)){
                examResult.setValidVal(0);
            }
            examResultMgr.storeExamResult(questionnaire, examResult);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new QuestionnaireException(e.getMessage());
        }
    }

    @Override
    public void onAnswerQuesiton(int questionIdx, String answer, boolean isIndividual) {

    }

    @Override
    public void onOpen(Map<Object, Object> params) throws QuestionnaireException {

    }

}
