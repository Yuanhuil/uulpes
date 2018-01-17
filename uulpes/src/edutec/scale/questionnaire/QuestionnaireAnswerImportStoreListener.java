package edutec.scale.questionnaire;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.util.MathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.njpes.www.dao.assessmentcenter.ExamdoMapper;
import com.njpes.www.dao.assessmentcenter.ExamdoMentalHealthMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.dao.scaletoollib.StudentDimExamResultMapper;
import com.njpes.www.dao.scaletoollib.TeacherDimExamResultMapper;
import com.njpes.www.entity.assessmentcenter.ExamdoMentalHealth;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherWithBLOBs;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.scaletoollib.ExamdoStudent;
import com.njpes.www.entity.scaletoollib.ExamdoTeacher;
import com.njpes.www.utils.AgeUitl;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.exam.ExamResult;
import edutec.scale.exam.ExamResultMgr;
import edutec.scale.exam.ExamScoreGrade;
import edutec.scale.exam.ExamWarning;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.util.ScaleUtils;
import heracles.db.ibatis.SequenceGenerator;
import heracles.util.UtilDateTime;
import heracles.util.UtilMisc;

@Component("QuestionnaireAnswerImportStoreListener")
public class QuestionnaireAnswerImportStoreListener extends QuestionnaireListener {
    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ExamResultMgr examResultMgr;
    @Autowired
    private SequenceGenerator sequenceGenerator;
    @Autowired
    private ExamResultMapper examResultDao;
    @Autowired
    ExamdoMapper examDoMapper;
    @Autowired
    private StudentDimExamResultMapper studentDimExamResultDao;
    @Autowired
    private TeacherDimExamResultMapper teacherDimExamResultDao;
    @Autowired
    private StudentMapper studentDao;
    @Autowired
    private TeacherMapper teacherDao;
    @Autowired
    ExamdoMentalHealthMapper exmadoMhMapper;
    @Autowired
    private PlatformTransactionManager txManager;
    public static final int EXAMMH_STUDENT_FLAG = 1; // 学生完成
    public static final int EXAMMH_TEACHER_FLAG = 2; // 班主任完成
    public static final int EXAMMH_PARENT_FLAG = 4; // 家长完成

    public QuestionnaireAnswerImportStoreListener() {

    }

    public void onClose(Map<Object, Object> params) throws QuestionnaireException {
        try {
            ExamResult examResult = new ExamResult();
            // 记录谁测试报告，可以是测别人的，也可以测试自己的
            examResult.setObserverUserInfo(observerUserInfo);
            examResult.setSubjectUserInfo(subjectUserInfo);
            // 被测试用户的一些信息拷贝给examResult对象，被测用户可能是测试人，也可能是别人
            examResult.setFieldValFromUserInfo(subjectUserInfo);
            // 将问卷的一些信息拷贝给examResult对象
            examResult.setFieldValFromQuestionnaire(questionnaire);

            // List<ExamResult> erList = (List<ExamResult>)params.get("erList");
            // erList.add(examResult);
            if(!isValid(examResult)){
                examResult.setValidVal(0);
            }
            storeExamResult(questionnaire, examResult, params);
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

    public void storeExamResult(Questionnaire questionnaire, ExamResult examResult, Map<Object, Object> params)
            throws QuestionnaireException {
        logger.info("保存用户信息！");
        List<ExamResult> erList = (List<ExamResult>) params.get("erList");
        erList.add(examResult);
        int rowCount = 0;
        if (params.get("rowCount") != null)
            rowCount = Integer.parseInt(params.get("rowCount").toString());
        // 将examdo表里面的基本信息保存到examresult表里面去，赵万锋
        // ExamdoStudent examdoStudent =
        Object userinfo = questionnaire.getSubjectUserInfo();
        int statusflag = 0;
        if (userinfo instanceof Student) {
            ExamdoStudent examdoStudent = (ExamdoStudent) questionnaire.getExamdo();
            if (examdoStudent == null) {
                examdoStudent = examDoMapper.selectStudentExamdoByResultId(examResult.getId());
                questionnaire.setExamdo(examdoStudent);
            }
            statusflag = examdoStudent.getStatusflag();
            setStudentExamResult(examResult, examdoStudent);
        }
        if (userinfo instanceof Teacher) {
            ExamdoTeacher examdoTeacher = (ExamdoTeacher) questionnaire.getExamdo();
            if (examdoTeacher == null) {
                examdoTeacher = examDoMapper.selectTeacherExamdoByResultId(examResult.getId());
                questionnaire.setExamdo(examdoTeacher);
            }
            setTeacherExamResult(examResult, examdoTeacher);
        }
        try {
            int scaleId = examResult.getScaleId();
            // 如果是三角视
            if (ScaleUtils.isThirdAngleScale(scaleId + "")) {
                // examResultDao.insertExamMHResult(examResult);//存放到examresult_student_MH表
                String threeAngleUUID = examResult.getThreeAngleUUID();// 三角视识别码
                // if(threeAngleUUID==null)//在线答题
                // {
                List<DimensionBlock> list = questionnaire.getDimensionBlocks();
                List<ExamResult> mhExamResultList = null;
                if (threeAngleUUID == null)// 在线答题
                {
                    mhExamResultList = examResultDao.getMhExamResult(examResult.getId());
                } else {// 是导入的
                    mhExamResultList = examResultDao.getMhExamREsultByMhCode(threeAngleUUID);
                }
                setMhOktime(examResult);// 设置三角视完成时间
                // 家长、老师和学生第一次做测试，直接入结果表
                if (mhExamResultList == null || mhExamResultList.size() == 0) {
                    examResultDao.insertExamMHResult(examResult);// 存放到examresult_student_MH表

                    Map<?, ?> param = UtilMisc.toMap("id", examResult.getId(), "table", examResult.getTable());
                    ExamResult existResult = null;
                    if (userinfo instanceof Student)
                        existResult = examResultDao.getStudentExamResult(param);
                    if (userinfo instanceof Teacher)
                        existResult = examResultDao.getTeacherExamResult(param);
                    if (existResult != null)
                        examResultDao.deleteExamByResultID(examResult);

                    examResultDao.insertExamResult(examResult);
                    // examResultDao.insertExamDimResult(examResult);
                } else {// 如果有人做过三角视测试，对分数进行平均计算，再入结果表
                    examResultDao.insertExamMHResult(examResult);// 存放到examresult_student_MH表
                    Map<?, ?> param = UtilMisc.toMap("id", examResult.getId(), "table", "examresult_student");
                    ExamResult oldExamResult = examResultDao.getStudentExamResult(param);
                    calcThreeAngle(list, mhExamResultList);

                    // }
                    // 重新计算维度得分等级和告警等级
                    if (questionnaire.getScale().isWarningOrNot()) {
                        ExamWarning warning = SpringContextHolder.getBean("ExamWarning", ExamWarning.class);
                        warning.setQuestionnaire(questionnaire);
                        warning.calGrade();
                    }
                    ExamScoreGrade examScoreGrade = SpringContextHolder.getBean("ExamScoreGrade", ExamScoreGrade.class);
                    examScoreGrade.setQuestionnaire(questionnaire);
                    examScoreGrade.calGrade();

                    // evalMentalHealthRank(questionnaire);//重新计算总分的得分等级
                    if (oldExamResult != null) {
                        String newExamresult = updateThreeAngleExamResult(questionnaire, oldExamResult);

                        // String dimResults =
                        // QuestionnaireUtils.buildDimensionReslut(questionnaire);
                        examResult.setDimScore(newExamresult);
                    }
                    // String[] dimAndVals =
                    // QuestionnaireUtils.buildDimAndVals(questionnaire, false);
                    // examResult.setDimValues(dimAndVals[1]);
                    // 教师版、家长版的量表id在结果表examresult_student里面依然是学生版的id
                    if (scaleId == 111000002 || scaleId == 111000003)
                        examResult.setScaleId(111000001);
                    if (scaleId == 110110002 || scaleId == 110110003)
                        examResult.setScaleId(110110001);
                    examResultDao.updateDimScoreExamResult(examResult);
                    // examdim表目前已经不用，暂时注释
                    // Map param = getDimValueMap(examResult);
                    // if(examResult.getSubjectUserInfo() instanceof Student)
                    // studentDimExamResultDao.updateDimScore(param);
                    // else
                    // teacherDimExamResultDao.updateDimScore(param);
                }
                // setMhOktime(examResult);//设置三角视完成时间
                if (scaleId == 111000001 || scaleId == 110110001)
                    statusflag = EXAMMH_STUDENT_FLAG | statusflag;
                if (scaleId == 111000002 || scaleId == 110110002)
                    statusflag = EXAMMH_PARENT_FLAG | statusflag;
                if (scaleId == 111000003 || scaleId == 110110003)
                    statusflag = EXAMMH_TEACHER_FLAG | statusflag;
                setOktime(questionnaire, statusflag, examResult.getId());// 设置examdo的完成时时间
            } else {
                Map<?, ?> param = UtilMisc.toMap("id", examResult.getId(), "table", examResult.getTable());
                ExamResult existResult = null;
                if (userinfo instanceof Student)
                    existResult = examResultDao.getStudentExamResult(param);
                if (userinfo instanceof Teacher)
                    existResult = examResultDao.getTeacherExamResult(param);
                if (existResult != null)
                    examResultDao.deleteExamByResultID(examResult);
                examResultDao.insertExamResult(examResult);

                /*
                 * if(erList.size()==rowCount){ if(erList.size()>0){
                 * examResultDao.insertExamResultBatch(erList);
                 * 
                 * } }
                 */

                statusflag = 1;
                setOktime(questionnaire, statusflag, examResult.getId());
            }
        } catch (Exception e) {
            throw new QuestionnaireException("答案保存错误!");
        } finally {
        }

    }

    public void batchCommit(Map<Object, Object> params) {
        List<ExamResult> erList = (List<ExamResult>) params.get("erList");
        examResultDao.insertExamResultBatch(erList);
    }

    private void calcThreeAngle(List<DimensionBlock> dimList, List<ExamResult> mhExamResultList) throws Exception {
        // String sjssbm = this.getThreeAngleUUID();//三角视识别码
        {
            // List<ExamResult> mhExamResultList =
            // examResultDao.getMhExamResult(resultid);
            if (mhExamResultList == null)
                return;
            int sz = mhExamResultList.size();
            // 最多3个
            if (sz != 0) {
                for (DimensionBlock detail : dimList) {
                    String dimId = detail.getId();
                    // 如果是三角视，那么W0维度不参与算分。added by 赵万锋
                    if (dimId.equals("W0"))
                        continue;

                    // 当前人的测试结果
                    double dimV = (Double) detail.getStdScore();
                    double dimR = (Double) detail.getRawScore();

                    if (sz == 1) {
                        ExamResult er = mhExamResultList.get(0);
                        String dimScore = er.getDimScore();
                        String[] dimScoreArray = dimScore.split("#");
                        for (int j = 0; j < dimScoreArray.length; j++) {
                            String dimScoreInfo = dimScoreArray[j];
                            String[] ds = dimScoreInfo.split(",");
                            String wid = ds[0];
                            double raw = Double.valueOf(ds[1]);
                            double std = Double.valueOf(ds[2]);
                            if (dimId.equals(wid)) {
                                detail.setRawScore(MathUtils.round((raw + dimR) / 2D, 2));
                                detail.setStdScore(MathUtils.round((std + dimV) / 2D, 2));
                                detail.setFinalScore(MathUtils.round((std + dimV) / 2D, 2));
                                detail.setTScore(5.5 + 1.5 * MathUtils.round((std + dimV) / 2D, 2));
                                break;
                            }
                        }
                    } else if (sz == 2) {
                        ExamResult er1 = mhExamResultList.get(0);
                        ExamResult er2 = mhExamResultList.get(1);
                        String dimScore1 = er1.getDimScore();
                        String dimScore2 = er2.getDimScore();
                        String[] dimScoreArray1 = dimScore1.split("#");
                        String[] dimScoreArray2 = dimScore1.split("#");
                        double raw1 = 0, raw2 = 0, std1 = 0, std2 = 0;
                        for (int j = 0; j < dimScoreArray1.length; j++) {
                            String dimScoreInfo = dimScoreArray1[j];
                            String[] ds = dimScoreInfo.split(",");
                            String wid = ds[0];
                            double raw = Double.valueOf(ds[1]);
                            double std = Double.valueOf(ds[2]);
                            if (dimId.equals(wid)) {
                                raw1 = raw;
                                std1 = std;
                                break;
                            }
                        }
                        for (int j = 0; j < dimScoreArray2.length; j++) {
                            String dimScoreInfo = dimScoreArray2[j];
                            String[] ds = dimScoreInfo.split(",");
                            String wid = ds[0];
                            double raw = Double.valueOf(ds[1]);
                            double std = Double.valueOf(ds[2]);
                            if (dimId.equals(wid)) {
                                raw2 = raw;
                                std2 = std;
                                break;
                            }
                        }
                        detail.setRawScore(MathUtils.round((raw1 + raw2 + dimR) / 2D, 2));
                        detail.setStdScore(MathUtils.round((std1 + std2 + dimV) / 2D, 2));
                        detail.setFinalScore(MathUtils.round((std1 + std2 + dimV) / 2D, 2));
                        detail.setTScore(5.5 + 1.5 * MathUtils.round((std1 + std2 + dimV) / 2D, 2));

                    }
                }
            }
        }
    }

    private String updateThreeAngleExamResult(Questionnaire questionnaire, ExamResult oldExamResult) throws Exception {
        List<DimensionBlock> dimList = questionnaire.getDimensionBlocks();
        String dimScore = oldExamResult.getDimScore();
        String[] dimScoreArray = dimScore.split("#");

        Map<String, String> oldDimScoreMap = new HashMap<String, String>();
        for (int j = 0; j < dimScoreArray.length; j++) {
            String dimScoreInfo = dimScoreArray[j];
            String[] ds = dimScoreInfo.split(",");
            String wid = ds[0];
            String rscore = ds[1];
            String stdscore = ds[2];
            String scoregrade = ds[3];
            String warning = ds[4];
            oldDimScoreMap.put(wid, dimScoreInfo);

        }
        for (DimensionBlock detail : dimList) {
            String dimId = detail.getId();
            if (dimId.equals("W0"))
                continue;

            String rscore = detail.getRawScore().toString();
            String stdscore = detail.getStdScore().toString();
            String scoregrade = String.valueOf(detail.getScoreGrade());
            String warning = String.valueOf(detail.getWarningGrade());

            oldDimScoreMap.put(dimId, dimId + "," + rscore + "," + stdscore + "," + scoregrade + "," + warning);
        }

        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (String key : oldDimScoreMap.keySet()) {
            String value = oldDimScoreMap.get(key);
            if (index > 0)
                value = "#" + value;
            sb.append(value);
            index++;
        }

        return sb.toString();

    }

    private void setOktime(Questionnaire questionnaire, int statusflag, long resultid) {
        String okTime = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());
        Map para = UtilMisc.toMap("okTime", okTime, "statusflag", statusflag, "resultid", resultid);
        if (questionnaire.getSubjectUserInfo() instanceof Student)
            examDoMapper.setStudentOkTime(para);
        else
            examDoMapper.setTeacherOkTime(para);
        questionnaire.setOkTime(UtilDateTime.nowTimestamp());
    }

    private void setMhOktime(ExamResult er) {
        ExamdoMentalHealth emh = new ExamdoMentalHealth();
        emh.setResultId(er.getId());
        // String okTime = (new java.text.SimpleDateFormat("yyyy-MM-dd
        // hh:mm:ss")).format(new Date());
        // Map para = UtilMisc.toMap("okTime",okTime,"resultid",resultid);
        String scaleid = er.getScaleId() + "";
        Object observerUser = er.getObserverUserInfo();
        if (ScaleUtils.isThirdAngleScaleForStudent(scaleid)) {
            emh.setOkTimeS(new Date());
            exmadoMhMapper.updateOkTimeForStudent(emh);
        }
        if (ScaleUtils.isThirdAngleScaleForTeacher(scaleid)) {
            emh.setOkTimeT(new Date());
            exmadoMhMapper.updateOkTimeForTeacher(emh);
        }
        if (ScaleUtils.isThirdAngleScaleForParent(scaleid)) {
            emh.setOkTimeP(new Date());
            exmadoMhMapper.updateOkTimeForParent(emh);
        }
    }

    private void setStudentExamResult(ExamResult examResult, ExamdoStudent examdoResult) {
        long userid = examdoResult.getUserid();
        StudentWithBLOBs student = studentDao.getStudentByid(userid);
        examResult.setXxmc(student.getXxmc());
        // examResult.setBj(examdoResult.getBj());
        examResult.setUserClassId(examdoResult.getBj());
        examResult.setBjmc(examdoResult.getBjmc());
        examResult.setNj(examdoResult.getNj());
        String njmc = AgeUitl.getNjName(examdoResult.getGradeid());
        examResult.setNjmc(njmc);
        examResult.setXd(examdoResult.getXd());
        // examResult.setAttrs(student.getAttrs());
        examResult.setUserGender(student.getXbm());
        examResult.setXh(student.getXh());
        examResult.setSfzjh(student.getSfzjh());
        examResult.setUserName(student.getXm());
        examResult.setXmpy(student.getXmpy());
        examResult.setCsrq(student.getCsrq());
        examResult.setXxmc(student.getXxmc());
        examResult.setUserGradeOrderId(examdoResult.getGradeid());
        examResult.setUserOrgId(examdoResult.getOrgid());
        examResult.setAttrs(student.getBjxx());
        // examResult.setVersion(AcountTypeFlag.student.getId());

    }

    private void setTeacherExamResult(ExamResult examResult, ExamdoTeacher examdoResult) {
        long userid = examdoResult.getUserid();
        TeacherWithBLOBs teacher = teacherDao.selectByPrimaryKey(userid);
        examResult.setXxmc(teacher.getXxmc());
        // examResult.setAttrs(student.getAttrs());
        examResult.setUserGender(teacher.getXbm());
        examResult.setSfzjh(teacher.getSfzjh());
        examResult.setUserName(teacher.getXm());
        examResult.setXmpy(teacher.getXmpy());
        examResult.setCsrq(teacher.getCsrq());
        examResult.setGh(teacher.getGh());
        // examResult.setTeacherRole(teacher.getTeacherRole());
        examResult.setRoleid(examdoResult.getRoleid());
        examResult.setUserOrgId(examdoResult.getOrgid());
        examResult.setAttrs(teacher.getBjxx());
        examResult.setVersion(AcountTypeFlag.teacher.getId());
    }
}
