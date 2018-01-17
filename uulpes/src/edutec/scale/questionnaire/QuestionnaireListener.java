package edutec.scale.questionnaire;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import edutec.scale.exam.ExamResult;

//import com.njpes.www.entity.baseinfo.UserInfo;

import edutec.scale.exception.QuestionnaireException;

public abstract class QuestionnaireListener {
    protected Questionnaire questionnaire;
    protected Object subjectUser;
    protected Object observerUser;
    // 4.0里面只记录用户id信息，不涉及到具体用户信息。added by zhaowanfeng
    protected String subjectUserId;
    protected String observerUserId;

    protected Object subjectUserInfo;
    protected Object observerUserInfo;

    public Object getSubjectUserInfo() {
        return subjectUserInfo;
    }

    public void setSubjectUserInfo(Object subjectUserInfo) {
        this.subjectUserInfo = subjectUserInfo;
    }

    public Object getObserverUserInfo() {
        return observerUserInfo;
    }

    public void setObserverUserInfo(Object observerUserInfo) {
        this.observerUserInfo = observerUserInfo;
    }

    public String getSubjectUserId() {
        return subjectUserId;
    }

    public void setSubjectUserId(String subjectUserId) {
        this.subjectUserId = subjectUserId;
    }

    public String getObserverUserId() {
        return observerUserId;
    }

    public void setObserverUserId(String observerUserId) {
        this.observerUserId = observerUserId;
    }

    public Object getObserverUser() {
        return observerUser;
    }

    public void setObserverUser(Object observerUser) {
        this.observerUser = observerUser;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    abstract public void onAnswerQuesiton(int questionIdx, String answer, boolean isIndividual)
            throws QuestionnaireException;;

    /**
     * 结束问卷
     * 
     * @throws QuestionnaireException
     */

    // added by zhaowanfeng
    abstract public void onClose(Map<Object, Object> params) throws QuestionnaireException;

    /**
     * 结束问卷
     * 
     * @throws QuestionnaireException
     */
    abstract public void onOpen(Map<Object, Object> params) throws QuestionnaireException;

    public Object getSubjectUser() {
        return subjectUser;
    }

    public void setSubjectUser(Object user) {
        this.subjectUser = user;
    }

    /**
     * 无效问卷判断
     * @Author zhangj
     * @Version 4.0
     * @param examResult
     * @return boolean
     * @Date 2017年3月23日 下午5:17:54
     */
    public boolean isValid(ExamResult examResult) {
        String questionScore = examResult.getQuestionScore();
        String[] split = StringUtils.split(questionScore, "#");
        if(examResult.getTesttype() == 1){    //测试类型为答题时,判断答题时间
            try {
                Date startTime =  examResult.getStartTime();
                Date okTime =  examResult.getOkTime();
                if((okTime.getTime()-startTime.getTime()) <= split.length*500){
                    return false;   //平均答题时间小于0.5秒,返回false
                }
            } catch (Exception e) {
                return true;
            }
        }
        //检测答案是否全部相同
        String answer = "";
        for (String str : split) {
            if(StringUtils.isNotBlank(str)){
                String[] split2 = StringUtils.split(str, ",");
                if(StringUtils.equals(answer, "")){ //先拿到第一题答案
                    answer = split2[1];
                }else if(!StringUtils.equals(answer, split2[1])){ //只要有一题答案不同,则跳出循环
                    answer = "";
                    break;
                }
            }
        }
        if(!StringUtils.equals(answer, "") )
            return false;   //如果答案相同,返回false
        return true;
    }
}
