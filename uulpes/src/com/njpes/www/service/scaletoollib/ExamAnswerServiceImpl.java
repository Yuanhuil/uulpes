package com.njpes.www.service.scaletoollib;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.assessmentcenter.ExamresultStudentMhMapper;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultTeacherMapper;
import com.njpes.www.entity.assessmentcenter.ExamresultStudentMhWithBLOBs;
import com.njpes.www.entity.scaletoollib.StudentExamAnswer;
import com.njpes.www.entity.scaletoollib.TeacherExamAnswer;
import com.njpes.www.invoker.DownloadScaleAnswerInvoker;
import com.njpes.www.invoker.DownloadScaleAnswerTmpInvoker;
import com.njpes.www.invoker.ScaleAnswerInvoker;
import com.njpes.www.service.baseinfo.DistrictService;

import edutec.group.domain.DimWarning;
import edutec.group.domain.DimWarningAndGradeStore;
import edutec.scale.db.CachedScaleMgr;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.QuestionnaireUtils;
import heracles.util.UtilMisc;

@Service("ExamAnswerServiceI")
public class ExamAnswerServiceImpl implements ExamAnswerServiceI {
    @Autowired
    ScaleAnswerInvoker scaleAnswerInvoker;
    @Autowired
    DownloadScaleAnswerTmpInvoker downloadScaleAnswerTmpInvoker;
    @Autowired
    DownloadScaleAnswerInvoker downloadScaleAnswerInvoker;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    CachedScaleMgr cachedScaleMgr;
    @Autowired
    private ExamResultMapper examResultDao;
    @Autowired
    DistrictService districtService;
    @Autowired
    ExamresultStudentMapper examresultStudentMapper;
    @Autowired
    ExamresultTeacherMapper examresultTeacherMapper;
    @Autowired
    private ExamresultStudentMhMapper examresultStudentMhMapper;

    @Override
    public void importStudentAnswerFromXls(long orgid, InputStream inputStream, Map<Object, Object> page)
            throws Exception {
        // TODO Auto-generated method stub
        scaleAnswerInvoker.importStudentAnswerFromXls(orgid, inputStream, page);
    }

    @Override
    public void importTeacherAnswerFromXls(long orgid, InputStream inputStream, Map<Object, Object> page)
            throws Exception {
        scaleAnswerInvoker.importTeacherAnswerFromXls(orgid, inputStream, page);

    }

    @Override
    public void downloadStuAnswerForSch(String orgid, String xd, String nj, String[] bjArray, String scaleId,
            String[] headnames, String[] headfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream) {
        // TODO Auto-generated method stub
        downloadScaleAnswerInvoker.downloadStuAnswerForSchool(orgid, xd, nj, bjArray, scaleId, headnames, headfields,
                attrIds, startTime, endTime, outputStream);
    }

    @Override
    public void downloadTchAnswerForSch(String orgid, String roleid, String scaleId, String[] headnames,
            String[] headfields, String[] attrIds, String startTime, String endTime, OutputStream outputStream) {
        // TODO Auto-generated method stub
        downloadScaleAnswerInvoker.downloadTchAnswerForSchool(orgid, roleid, scaleId, headnames, headfields, attrIds,
                startTime, endTime, outputStream);
    }

    @Override
    public void downloadStuAnswerForEdu(int orglevel, String nj, String njmc, List countyIdList, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream) {
        /*
         * List<Organization> orgList
         * =organizationMapper.getSchoolOrgByCountyIds(countyIdList);
         * StringBuilder sb = new StringBuilder(); for(int
         * i=0;i<orgList.size();i++){ Organization org = orgList.get(i);
         * sb.append(org.getId()); sb.append(","); } sb.deleteCharAt(sb.length()
         * - 1); downloadScaleAnswerInvoker.downloadStuAnswerForEdu(orglevel,sb.
         * toString(), nj, njmc, scaleId,attrIds,attrnames, attrfields,
         * startTime, endTime, outputStream);
         */}

    @Override
    public void downloadTchAnswerForCityEdu(String countyid, String roleid, String rolename, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream) {
        downloadScaleAnswerInvoker.downloadTchAnswerForCityEdu(countyid, roleid, rolename, scaleId, attrnames,
                attrfields, attrIds, startTime, endTime, outputStream);
    }

    @Override
    public void downloadTchAnswerForCountyEdu(String schoolid, String roleid, String rolename, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream) {
        downloadScaleAnswerInvoker.downloadTchAnswerForCountyEdu(schoolid, roleid, rolename, scaleId, attrnames,
                attrfields, attrIds, startTime, endTime, outputStream);
    }

    @Override
    public Map<String, Object> getStudentAnswer(long resultid) {
        // TODO Auto-generated method stub
        StudentExamAnswer examResult = examresultStudentMapper.getStudentAnswerByResultid(resultid);
        String scaleid = examResult.getScaleid();
        String qscore = examResult.getAnswer();
        Scale scale = cachedScaleMgr.get(scaleid);
        Questionnaire questionnaire = new Questionnaire(scale, null);
        try {
            questionnaire.open(null);
        } catch (QuestionnaireException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        QuestionnaireUtils.fillQuestionContentFromStr(questionnaire, qscore, false);
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("questionnaire", questionnaire);
        page.put("tester", examResult.getXm());
        page.put("xbm", examResult.getXbm());
        page.put("resultMap", resultMap);
        return page;
    }

    @Override
    public Map<String, Object> getTeacherAnswer(long resultid) {
        // TODO Auto-generated method stub
        TeacherExamAnswer examResult = examresultTeacherMapper.getTeacherAnswerByResultid(resultid);
        String scaleid = examResult.getScaleid();
        String qscore = examResult.getAnswer();
        Scale scale = cachedScaleMgr.get(scaleid);
        Questionnaire questionnaire = new Questionnaire(scale, null);
        try {
            questionnaire.open(null);
        } catch (QuestionnaireException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        QuestionnaireUtils.fillQuestionContentFromStr(questionnaire, qscore, false);
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("questionnaire", questionnaire);
        page.put("tester", examResult.getXm());
        page.put("xbm", examResult.getXbm());
        page.put("resultMap", resultMap);
        return page;
    }

    @Override
    public Map<String, Object> getThreeAngleAnswer(long resultid, int typeflag) {
        // TODO Auto-generated method stub

        ExamresultStudentMhWithBLOBs examResultMh = examresultStudentMhMapper.selectByPrimaryKey(resultid, typeflag);
        String scaleid = examResultMh.getScaleId();
        String qscore = examResultMh.getQuestionScore();
        Scale scale = cachedScaleMgr.get(scaleid);
        Questionnaire questionnaire = new Questionnaire(scale, null);
        try {
            questionnaire.open(null);
        } catch (QuestionnaireException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        QuestionnaireUtils.fillQuestionContentFromStr(questionnaire, qscore, false);
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("questionnaire", questionnaire);
        page.put("tester", examResultMh.getXm());
        page.put("xbm", examResultMh.getGender());
        page.put("resultMap", resultMap);
        return page;
    }

    public static Map<String, String> resultMap = new HashMap<String, String>();

    static {
        resultMap.put("0", "A");
        resultMap.put("1", "B");
        resultMap.put("2", "C");
        resultMap.put("3", "D");
        resultMap.put("4", "E");
        resultMap.put("5", "F");
        resultMap.put("6", "G");
        resultMap.put("7", "H");
        resultMap.put("8", "I");
        resultMap.put("9", "J");
        resultMap.put("10", "K");
        resultMap.put("11", "L");
        resultMap.put("12", "M");
        resultMap.put("13", "N");

    }

    @Override
    public void downloadStuAnswerForCityEdu(String countyid, String xd, String nj, String scaleid, String[] headnames,
            String[] headfields, String[] attrIds, String startTime, String endTime, OutputStream outputStream) {
        downloadScaleAnswerInvoker.downloadStuAnswerForCityEdu(countyid, xd, nj, scaleid, headnames, headfields,
                attrIds, startTime, endTime, outputStream);

    }

    @Override
    public void downloadStuAnswerForCountyEdu(String countyid, String schoolid, String xd, String nj, String scaleid,
            String[] headnames, String[] headfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream) {
        downloadScaleAnswerInvoker.downloadStuAnswerForCountyEdu(countyid, schoolid, xd, nj, scaleid, headnames,
                headfields, attrIds, startTime, endTime, outputStream);

    }

    @Override
    public void ScaleWarningReCalculate(String scaleid, int typeflag) {
        String table;
        if (typeflag == 1)
            table = "examresult_student";
        else
            table = "examresult_teacher";
        Scale scale = cachedScaleMgr.get(scaleid);
        Map param = UtilMisc.toMap("table", table, "scaleid", scaleid);
        List<DimWarningAndGradeStore> dimWarningAndGradeList = examResultDao.getDimWarningAndGradeStore(param);
        for (int i = 0; i < dimWarningAndGradeList.size(); i++) {
            DimWarningAndGradeStore dimWG = dimWarningAndGradeList.get(i);
            long id = dimWG.getId();
            String dimScoreStr = dimWG.getDim_score();
            String[] dimScoreArray = dimScoreStr.split("#");
            int maxWg = 0;// 告警等级最大值
            String totalScore = "";
            for (int j = 0; j < dimScoreArray.length; j++) {
                int dimWarningScore = 0;
                String dimScore = dimScoreArray[j];
                String[] dimScoreInfoArray = dimScore.split(",");

                String wid = dimScoreInfoArray[0];
                String rawScore = dimScoreInfoArray[1];
                String zScore = dimScoreInfoArray[2];
                String gradeScore = dimScoreInfoArray[3];
                double z = Double.valueOf(zScore);

                // if(wid.equals("W0")){
                // if (ScaleUtils.isThirdAngleScale(scaleid) )
                // continue;
                // }
                Map<?, ?> map = UtilMisc.toMap(Constants.SCALEID_PROP, scaleid, Constants.WID_PROP, wid);
                DimWarning dimWarning = examResultDao.getWarning(map);
                if (dimWarning == null) {
                    totalScore = totalScore + wid + "," + rawScore + "," + zScore + "," + gradeScore + ",0" + "#";
                    continue;
                }
                // 预警等级，这些先注释掉
                double w1 = dimWarning.getW1();
                double w2 = dimWarning.getW2();
                double w3 = dimWarning.getW3();
                int lv = getWarningLevel(z, w1, w2, w3);
                if (maxWg < lv)
                    maxWg = lv;
                if (scale.isWarningOrNot()) {
                    // 如果有总分维度，或者只有一个维度
                    if (wid.equals(Dimension.SUM_SCORE_DIM) || scale.getDimensionSize() == 1)
                        dimWarningScore = lv;
                }
                totalScore = totalScore + wid + "," + rawScore + "," + zScore + "," + gradeScore + "," + lv + "#";
            }
            totalScore = totalScore.substring(0, totalScore.length() - 1);
            Map<?, ?> map1 = UtilMisc.toMap("table", table, "id", id, "dimScore", totalScore, "warningGrade", maxWg);
            examResultDao.updateDimScoreAndWarning(map1);

        }
    }

    private int getWarningLevel(double val, double w1, double w2, double w3) {
        if (w1 > 0) {
            if (val > w3)
                return 3;
            else if ((val <= w3) && (val > w2))
                return 2;
            else if ((val <= w2) && (val > w1))
                return 1;
        } else {
            if (val < w3)
                return 3;
            else if ((val < w2) && (val >= w3))
                return 2;
            else if ((val < w1) && (val >= w2))
                return 1;
        }
        return 0;
    }

	@Override
	public void downloadAllStudentResult(String[] headnames, String[] headfields, String startTime, String endTime,
			OutputStream outputStream) {
		try {
			downloadScaleAnswerInvoker.downloadAllStudentResult(headnames, headfields,
			        startTime, endTime, outputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
