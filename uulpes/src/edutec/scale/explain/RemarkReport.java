package edutec.scale.explain;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.text.StrBuilder;

import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.descriptor.ScaleReportDescriptor;
import edutec.scale.exam.ExamResult;
import edutec.scale.model.Scale;
import edutec.scale.questionnaire.DimDetail;
import edutec.scale.util.ScaleUtils;
import heracles.util.Pools;
import heracles.util.SimpleDateFormat;

public class RemarkReport extends CompositeReport {

    public RemarkReport(Object subjectUser, Object observerUser) {
        super(subjectUser, observerUser);
    }

    public void exportRemarkReport(boolean isDownload) throws Exception {

        // 加载测试结果记录
        loadExamrs();
        if (MapUtils.isEmpty(examrsListMap)) {
            return;
        }
        scaleIds = examrsListMap.keySet().toArray(new String[0]);

        try {
            dataMap = new HashMap<String, Object>();
            getData(dataMap);
            // 设置个人信息
            String userName = "";
            if (subjectUser instanceof Student) {
                Student stu = (Student) subjectUser;
                userName = stu.getXm();
                // rootMap.put("student", stu);
            }

            else if (subjectUser instanceof Teacher) {
                Teacher teacher = (Teacher) subjectUser;
                userName = teacher.getXm();
                // rootMap.put("teacher", teacher);
            }
            makeupData(scaleIds, isDownload);

        } finally {

        }
    }

    protected void makeupData(String[] scaleIds, boolean isDownload) throws Exception {
        // List<Map<String,Object>> resultlist = new
        // ArrayList<Map<String,Object>>(scaleIds.length);
        // rootMap = new HashMap<String,Object>();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        for (int i = 0; i < scaleIds.length; i++) {
            CachedScaleMgr cachedScaleMgr = (CachedScaleMgr) SpringContextHolder.getBean("CachedScaleMgr",
                    CachedScaleMgr.class);
            Scale scale = cachedScaleMgr.get(scaleIds[i]);
            String scaletype = scale.getScaleType();
            // 心理健康量表
            if (ScaleUtils.isThirdAngleScaleForStudent(scaleIds[i])) {
                List erList = examrsListMap.get(scaleIds[i]);
                String result = buildScaleResult(erList);
                sb1.append(result);
            }
            // 人格量表
            else if (ScaleUtils.isPersonalityScale(scaleIds[i])) {
                List erList = examrsListMap.get(scaleIds[i]);
                String result = buildScaleResult(erList);
                sb2.append(result);
            }
            // 智能量表
            else if (ScaleUtils.isPotentialScale(scaleIds[i])) {
                List erList = examrsListMap.get(scaleIds[i]);
                String result = buildScaleResult(erList);
                sb3.append(result);
            }
        }
        dataMap.put("mentalDesc", sb1.toString());
        dataMap.put("personalityDesc", sb2.toString());
        dataMap.put("potentialDesc", sb3.toString());

        // 日期
        dataMap.put("now", SimpleDateFormat.format(new Date()));
        // 体育评语
        dataMap.put("medicalDesc", getMedicalSentence());
        // dataMap.put("resultlist", resultlist);

    }

    public String buildScaleResult(List erList) throws IOException {

        Map<String, Object> result = new HashMap<String, Object>();
        StrBuilder sb = null;
        for (int i = 0; i < erList.size(); i++) {
            ExamResult er = (ExamResult) erList.get(i);
            ScaleReportDescriptor desc = buildDescriptor(er);
            if (desc == null)
                continue;
            ScaleReportDescriptor descriptor = desc;
            List<DimDetail> dims = descriptor.getDimDetails();
            if (i == 0) {
                int typeFlag;
                if (observerUser instanceof Student)
                    typeFlag = 1;
                else if (observerUser instanceof Teacher)
                    typeFlag = 2;
                else
                    typeFlag = 3;
                ReportArticle article = new ReportArticle(descriptor.getQuestionnaire(), dims, typeFlag);
                article.buildRemarkReportComment();
                // Map<String, StrBuilder> dimDescnMap =
                // article.getSimpleDimDesciptor();
                String summarize = article.getSummarize();

                // String scaleTitle = descriptor.getQuestionnaire().getTitle();
                // result.put("scaletitle", scaleTitle);

                /*
                 * StrBuilder scaleDesc = new StrBuilder();
                 * Iterator<Map.Entry<String, StrBuilder>> it =
                 * dimDescnMap.entrySet().iterator(); while (it.hasNext()) {
                 * Map.Entry<String, StrBuilder> ent = it.next(); if(isDownload)
                 * scaleDesc.append(ent.getValue()+"<w:br/>"); else
                 * scaleDesc.append(ent.getValue()+"<br>");
                 * 
                 * }
                 */
                // result.put("scaledesc", scaleDesc.toString());
                // result.put("summarize", summarize);
                return summarize;
            }

        }
        return "";
    }

    private String getMedicalSentence() {
        StringBuilder buff = null;
        try {
            buff = Pools.getInstance().borrowStringBuilder();
            for (String[] sentence : MEDICAL_SENTENS) {
                int pos = RandomUtils.nextInt(sentence.length);
                buff.append(sentence[pos]);
                buff.append("，");
            }
            buff.setCharAt(buff.length() - 1, '。');
            return buff.toString();
        } finally {
            Pools.getInstance().returnStringBuilder(buff);
        }
    }

    static final String[] MEDICAL_SENTEN_1 = { "强健的身体是非常重要的", "身体健康是前提", "强健的体魄是第一位的", "健康的身体是身心愉悦的保证", "身体健康至关重要" };
    static final String[] MEDICAL_SENTEN_2 = { "要保持经常锻炼身体的好习惯", "要注意经常锻炼身体", "要多参加体育锻炼", "要经常进行体育运动", "要保证充足的时间锻炼身体" };
    static final String[] MEDICAL_SENTEN_3 = { "平时多散散步", "平时多打打球", "经常做做体操", "经常做做肢体运动", "功课之余多活动活动" };
    static final String[] MEDICAL_SENTEN_4 = { "保证充足的睡眠", "要注意劳逸结合", "既能学习也多休息", "不要学习太晚", "该休息的时候就应当休息" };
    static final String[] MEDICAL_SENTEN_5 = { "这样才能够全面发展", "这才是正确的学习之道", "均衡发展才是最重要的", "全面发展才是真正的好学生",
            " 社会需要全面发展的学生" };

    static final String[][] MEDICAL_SENTENS = { MEDICAL_SENTEN_1, MEDICAL_SENTEN_2, MEDICAL_SENTEN_3, MEDICAL_SENTEN_4,
            MEDICAL_SENTEN_5 };
}
