package edutec.scale.questionnaire;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edutec.scale.descriptor.Descriptor;
import edutec.scale.exam.ExamConsts;
import edutec.scale.exception.QuestionnaireException;
import heracles.util.SimpleDateFormat;

@Scope("prototype")
@Component("QuestionnaireReportListener")
public class QuestionnaireReportListener extends QuestionnaireListener {
    static private final Log logger = LogFactory.getLog(QuestionnaireReportListener.class);

    public static final String CTL_BROW_REPORT = "browReport";
    public static final String FLT_DURATION = "duration";
    public static final String FLT_START_TM = "startTime";
    public static final String FLT_END_TM = "endTime";

    private long startTimeMillis;
    private long endTimeMillis;

    private boolean isNeedCaclulate = true;

    public boolean isNeedCaclulate() {
        return isNeedCaclulate;
    }

    public void setNeedCaclulate(boolean isNeedCaclulate) {
        this.isNeedCaclulate = isNeedCaclulate;
    }

    @Override
    public void onAnswerQuesiton(int questionIdx, String answer, boolean isIndividual) {
    }

    /**
     * 做题 添加做题的开始时间，兼容以前没有开始时间的结果
     */
    @Override
    public void onClose(Map<Object, Object> params) throws QuestionnaireException {
        try {
            // 当打开已经做到报表时使用会有这个值
            Object ctl = params.get(CTL_BROW_REPORT);
            if (ctl != null) {
                Timestamp st = (Timestamp) params.get(FLT_START_TM);
                Timestamp ed = (Timestamp) params.get(FLT_END_TM);
                if (st != null) {
                    startTimeMillis = st.getTime();
                } else {
                    startTimeMillis = 0;
                }
                endTimeMillis = ed.getTime();
            } else {
                endTimeMillis = System.currentTimeMillis();
            }

            if (startTimeMillis != 0) {
                params.put(FLT_DURATION,
                        DurationFormatUtils.formatDuration(endTimeMillis - startTimeMillis, "H:mm:ss"));
                params.put(FLT_START_TM, DateFormatUtils.format(startTimeMillis, SimpleDateFormat.DATE_PATTEN_TM));
                params.put(FLT_END_TM, DateFormatUtils.format(endTimeMillis, SimpleDateFormat.DATE_PATTEN_TM));
                params.put("testtime", DateFormatUtils.format(endTimeMillis, SimpleDateFormat.DATE_PATTEN_TM));
                // params.put("age",
                // AgeUitl.getAgeByBirthdayStr(params.get(FLT_END_TM).toString()));
            } else {
                params.put(FLT_DURATION, StringUtils.EMPTY);
                params.put(FLT_START_TM, StringUtils.EMPTY);
                params.put(FLT_END_TM, DateFormatUtils.format(endTimeMillis, SimpleDateFormat.DATE_PATTEN_TM));
                params.put("testtime", DateFormatUtils.format(endTimeMillis, SimpleDateFormat.DATE_PATTEN_TM));
                // params.put("age",
                // AgeUitl.getAgeByBirthdayStr(params.get(FLT_END_TM).toString()));
            }

            /* 结果描述 */
            Descriptor descriptor = questionnaire.getDescriptor();
            descriptor.setNeedRecalulate(isNeedCaclulate());
            // DataSetModel imgData = descriptor.getImgData();
            // descriptor.setSubjectUser(subjectUser);
            descriptor.setSubjectUserInfo(this.subjectUserInfo);//
            /* 观看报告的人observerUser */
            if (observerUserInfo == null) {
                descriptor.setObserverUserInfo(subjectUserInfo);
            } else {
                descriptor.setObserverUserInfo(observerUserInfo);
            }
            // 页面上将显示答题人信息
            params.put(ExamConsts.FTL_USER_KEY, subjectUserInfo);

            // 生成了html页面，显示到web页上
            String html = null;

            // 看报告的人
            // UserInfo observerUser = descriptor.getObserverUserInfo();

            int forbid_report = 0;
            // ==========================
            params.put(ExamConsts.FORBID_REPORT, forbid_report);
            // 如果允许看报告
            if (forbid_report == 0) {
                // JChartParam chartParam = descriptor.getChartParam();
                // if (chartParam != null) {
                // params.put(ExamConsts.CHART_PARAM, chartParam.paramToStr());
                // }
                if (params.get("download").toString().equals("no")) {
                    html = descriptor.toHtml(params);
                    params.put("reportResult", html);
                } else {
                    descriptor.toOfficeWord(params);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new QuestionnaireException(e.getMessage());
        }

    }

    @Override
    public void onOpen(Map<Object, Object> params) throws QuestionnaireException {
        if (logger.isDebugEnabled())
            logger.debug("报告监听..." + getQuestionnaire().getTitle());
        startTimeMillis = System.currentTimeMillis();
    }

}
