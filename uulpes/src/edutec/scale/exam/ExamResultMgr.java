package edutec.scale.exam;

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
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.constant.Constants;
import com.njpes.www.constant.SequenceConst;
import com.njpes.www.dao.assessmentcenter.ExamdoMapper;
import com.njpes.www.dao.assessmentcenter.ExamdoMentalHealthMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.dao.scaletoollib.ExamTempAnswerMapper;
import com.njpes.www.dao.scaletoollib.StudentDimExamResultMapper;
import com.njpes.www.dao.scaletoollib.TeacherDimExamResultMapper;
import com.njpes.www.entity.assessmentcenter.ExamdoMentalHealth;
import com.njpes.www.entity.baseinfo.Parent;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherWithBLOBs;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.scaletoollib.ExamdoStudent;
import com.njpes.www.entity.scaletoollib.ExamdoTeacher;
import com.njpes.www.utils.AgeUitl;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.model.Dimension;
import edutec.scale.questionnaire.DimensionBlock;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.ScaleUtils;
import heracles.db.ibatis.SequenceGenerator;
import heracles.util.UtilDateTime;
import heracles.util.UtilMisc;

/**
 * 查询、保存、导入、导出
 * 
 * @author Administrator
 */
@Component("ExamResultMgr")
public class ExamResultMgr implements ExamConsts {
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
    ExamTempAnswerMapper examTempAnswerMapper;
    @Autowired
    private PlatformTransactionManager txManager;
    private final Log logger = LogFactory.getLog(getClass());
    public static final int EXAMMH_STUDENT_FLAG = 1; // 学生完成
    public static final int EXAMMH_TEACHER_FLAG = 2; // 班主任完成
    public static final int EXAMMH_PARENT_FLAG = 4; // 家长完成

    /**/
    /**
     * QuestionnaireStoreListener中使用
     * 
     * @param user
     * @param scaleId
     * @param examResult
     */
    public void storeExamResult(Questionnaire questionnaire, ExamResult examResult) throws Exception {
        // logger.info("保存用户信息！");

        // 将examdo表里面的基本信息保存到examresult表里面去，赵万锋
        // ExamdoStudent examdoStudent =
        String tempTable = null;
        Object userinfo = questionnaire.getSubjectUserInfo();
        int statusflag = 0;
        if (userinfo instanceof Student) {
            tempTable = ExamUtils.getStuPartAnswerTable(((Student) userinfo).getAccountId());
            ExamdoStudent examdoStudent = (ExamdoStudent) questionnaire.getExamdo();
            if (examdoStudent == null) {
                examdoStudent = examDoMapper.selectStudentExamdoByResultId(examResult.getId());
                questionnaire.setExamdo(examdoStudent);
            }
            statusflag = examdoStudent.getStatusflag();
            setStudentExamResult(examResult, examdoStudent);
        }
        if (userinfo instanceof Teacher) {
            tempTable = "exam_tea_answer";
            ExamdoTeacher examdoTeacher = (ExamdoTeacher) questionnaire.getExamdo();
            if (examdoTeacher == null) {
                examdoTeacher = examDoMapper.selectTeacherExamdoByResultId(examResult.getId());
                questionnaire.setExamdo(examdoTeacher);
            }
            setTeacherExamResult(examResult, examdoTeacher);
        }
        // ExamdoStudent examdoStudent =
        // examDoMapper.selectStudentExamdoByResultId(examResult.getId());
        // setSubjectUserInfo(examResult,examdoStudent);
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
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
                    // modified by zwf 2006-09-22
                    /*
                     * ExamResult existResult=null; if(userinfo instanceof
                     * Student) existResult =
                     * examResultDao.getStudentExamResult(param); if(userinfo
                     * instanceof Teacher) existResult =
                     * examResultDao.getTeacherExamResult(param);
                     * if(existResult!=null)
                     * examResultDao.deleteExamByResultID(examResult);
                     */
                    if (scaleId == 111000002 || scaleId == 111000003)
                        examResult.setScaleId(111000001);
                    if (scaleId == 110110002 || scaleId == 110110003)
                        examResult.setScaleId(110110001);
                    examResultDao.insertExamResult(examResult);
                    // 删除临时表记录
                    if (tempTable != null) {
                        param = UtilMisc.toMap("table", tempTable, "id", examResult.getId(), "scaleid", scaleId);
                        examTempAnswerMapper.deleteByPrimaryKey(param);
                    }
                } else if (mhExamResultList.size() > 0) {// 如果有人做过三角视测试，对分数进行平均计算，再入结果表
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

                    if (tempTable != null) {
                        param = UtilMisc.toMap("table", tempTable, "id", examResult.getId(), "scaleid", scaleId);
                        examTempAnswerMapper.deleteByPrimaryKey(param);
                    }

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
                statusflag = 1;
                setOktime(questionnaire, statusflag, examResult.getId());
                // 删除临时表结果
                if (tempTable != null) {
                    param = UtilMisc.toMap("table", tempTable, "id", examResult.getId(), "scaleid", scaleId);
                    examTempAnswerMapper.deleteByPrimaryKey(param);
                }
            }

            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            // logger.error(e.getMessage());
            throw new Exception("保存测评结果出错！");
        } finally {
        }

    }

    private Map getDimValueMap(ExamResult er) {
        Map map = new HashMap<String, String>();
        String[] dimTitles = er.getDimTitles().split(",");
        String[] dimValues = er.getDimValues().split(",");
        for (int i = 0; i < dimTitles.length; i++) {
            map.put(dimTitles[i], dimValues[i]);
        }
        return map;
        // Map map = HashMap<String,int>();
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

    /**
     * 《心理健康量表》需要重新计算总分的等级rank; 《心理健康量表》根据已经计算好的等级再次做计算；
     * 心理健康量表的总分不是根据题目得分计算出来的，而是根据三个维度的得分水平判断出来的
     */
    private void evalMentalHealthRank(Questionnaire questionnaire) {
        /**
         * 中学生心理健康量表，有三个父维度和12子维度，这里使用3个父维度
         */
        String scaleId = questionnaire.getScaleId();
        if (ScaleUtils.isThirdAngleScale(questionnaire.getScaleId())) {
            // 算出总分等级，因为总分的结果解释需要从数据库中取
            DimensionBlock dimBlk0 = questionnaire.findDimensionBlock(Dimension.SUM_SCORE_DIM);
            DimensionBlock dimBlk10 = questionnaire.findDimensionBlock("W2");// 困扰
            DimensionBlock dimBlk5 = questionnaire.findDimensionBlock("W1");// 适应
            DimensionBlock dimBlk15 = questionnaire.findDimensionBlock("W3");// 复原力

            int i1 = dimBlk10.getRank(); // 困扰等级
            int i2 = dimBlk5.getRank(); // 适应力等级
            int i3 = dimBlk15.getRank(); // 复原力等级

            if (i1 >= 4) {
                dimBlk0.setRank(1); // 一级
            } else {
                if (i2 <= 2 && i3 <= 2)
                    dimBlk0.setRank(5); //
                if (i2 <= 2 && i3 >= 3)
                    dimBlk0.setRank(4); //
                if (i2 >= 3 && i3 <= 2)
                    dimBlk0.setRank(3); //
                if (i2 >= 3 && i3 >= 3)
                    dimBlk0.setRank(2); //
            }

        }
    }

    /**
     * 删除所有用户的测试相关信息
     * 
     * @param user
     */
    public void deleteExamResult(Object userinfo) {
        long userId;
        String examdoTable = null;
        String examResTable = null;
        String examDimTable = null;
        if (userinfo instanceof Student) {
            userId = ((Student) userinfo).getId();
            examdoTable = "examdo_student";
            examResTable = "examresult_student";
            // examDimTable = "examresult_dim_student";
        } else if (userinfo instanceof Teacher) {
            userId = ((Teacher) userinfo).getId();
            examdoTable = "examdo_teacher";
            examResTable = "examresult_teacher";
            // examDimTable = "examresult_dim_teacher";
        } else {
            userId = ((Parent) userinfo).getId();
            // examResTable = "examresult_individual";
        }

        Map<String, Object> params = UtilMisc.toMap(Constants.USER_ID_PROP, userId, "examdoTable", examdoTable,
                "examResTable", examResTable, "examDimTable", examDimTable);

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            examResultDao.deleteExamMhDoByUserId(params);// 三视角测试信息
            examResultDao.deleteExamMhDimByUserId(params);
            examResultDao.deleteExamResultByUserId(params);// 测试结果
            examResultDao.deleteExamDoByUserId(params);// 测试发放
            // examResultDao.deleteExamDimByUserId(params);// 测试发放

            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
        } finally {
        }
    }

    private long getNextExamResultId() {
        long nextId = 0;
        nextId = sequenceGenerator.nextID(SequenceConst.EXAMRESULT);
        if (nextId == 0)
            throw new RuntimeException();
        return nextId;
    }

    /**
     * 为测试数据的移植而编写的方法,直接插入数据
     * 
     * @param examResult
     */
    public void storeScaleExamResult(ExamResult examResult) {
        // long nextId = getNextExamResultId();
        // examResult.setId(nextId);
        examResultDao.insertExamResult(examResult);
    }

    private void updateOkTime(ExamResult examResult) {
        Map<?, ?> params = UtilMisc.toMap(Constants.TABLE_PROP, ExamUtils.getExamDoTable(examResult.getUserRoleFlag()),
                Constants.USER_ID_PROP, examResult.getUserId(), Constants.SCALEID_PROP, examResult.getScaleId());
        // 中学心理健康家长评定
        if (examResult.getScaleId() == 200012) {
            examResultDao.updateParentExamDoOk(params);
        } else if (examResult.getScaleId() == 200013) { // 中学心理健康教师评定
            examResultDao.updateTeacherExamDoOk(params);
        } else {
            examResultDao.updateExamDoOk(params);
        }

    }

    /**
     * @param examResult
     *            已经包含id值
     */

    private void insertExamResult(ExamResult examResult) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        // 首先保存到examresult表里面
        int scaleId = examResult.getScaleId();
        if (!ScaleUtils.isThirdAngleScale(scaleId + "")) {
            examResultDao.insertExamResult(examResult);
        }
        try {
            // 得到量表号，以根据量表号判断如何处理，数据保存
            // int scaleId = examResult.getScaleId();
            // 3.0系统只有中学生心理健康量表才有三角视
            // 如果是三角视，并且是在线答题（通过量表分发）
            // Object subjectUserInfo = examResult.getSubjectUserInfo();
            // String subjectUserId = subjectUserInfo.getUserID();
            // String threeAngleUUID = examResult.getThreeAngleUUID();//三角视识别码
            examResult.setVersion(1);
            if (ScaleUtils.isThirdAngleScale(scaleId + "")) {
                String threeAngleUUID = examResult.getThreeAngleUUID();// 三角视识别码
                if (threeAngleUUID == null)// 在线答题
                {
                    // 找出examdo_mh表里面的分发记录

                    examResultDao.insertExamMHResult(examResult);// 存放到examresult_student_MH表
                    // 再存老师、家长、学生三方平均结果到examresult_student表中
                    List<ExamResult> mhExamResultList = examResultDao.getMhExamResult(examResult.getId());
                    if (mhExamResultList != null) {
                        if (mhExamResultList.size() > 0) {
                            Map rawMap = new HashMap();
                            Map stdMap = new HashMap();
                            for (int i = 0; i < mhExamResultList.size(); i++) {
                                ExamResult er = mhExamResultList.get(i);
                                String dimScore = er.getDimScore();
                                String[] dimScoreArray = dimScore.split("#");
                                for (int j = 0; j < dimScoreArray.length; j++) {
                                    String dimScoreInfo = dimScoreArray[j];
                                    String[] ds = dimScoreInfo.split(",");
                                    String wid = ds[0];
                                    double raw = Double.valueOf(ds[1]);
                                    double std = Double.valueOf(ds[2]);
                                    if (rawMap.get(wid) == null)
                                        rawMap.put(wid, 0);

                                    rawMap.put(wid,
                                            Double.valueOf(rawMap.get(wid).toString() + raw) / mhExamResultList.size());
                                    if (stdMap.get(wid) == null)
                                        stdMap.put(wid, 0);

                                    stdMap.put(wid,
                                            Double.valueOf(stdMap.get(wid).toString() + std) / mhExamResultList.size());

                                }

                            }
                            // examResultDao.updateExamResult(examResult);//这里只要更新examresult_student表，因为已经有一方做过
                        }
                    } else
                        examResultDao.insertExamResult(examResult);

                } else// 答案直接导入
                {

                }
                // 查询学生结果表里面是否已经入了同一批次的三角视结果
                /*
                 * Long existStuRId =
                 * queryForObject("selectResultIdByThreeAngleUUId",
                 * threeAngleUUID); if (existStuRId == null || existStuRId == 0)
                 * {
                 * 
                 * } else{ //如果已经有了同一批次的三角视结果，那么这一批都有同一个resultid
                 * examResult.setId(existStuRId); } if (scaleId ==
                 * 100011||scaleId == 200011 ) { examResult.setVersion(1); } if
                 * (scaleId == 100012||scaleId == 200012 ) {
                 * examResult.setVersion(2); } if (scaleId == 100013||scaleId ==
                 * 200013 ) { examResult.setVersion(3); }
                 */
                examResultDao.insertExamDimResultMh(examResult);
            }
            // examResultDao.insertExamResult(examResult);
            // 插入题目答案\个人题目答案\维度分数,会根据用户的角色分别插入不同的表
            // 目前有三个表：1. examresult_student 2.examresult_teacher
            // 3.examresult_individual
            // 如果是教师评定或家长评定，都做为200011处理，放入维度统计表,以适应群体分析
            if (scaleId == 100012 || scaleId == 100013) {
                examResult.setScaleId(100011);
            }
            if (scaleId == 200012 || scaleId == 200013) {
                examResult.setScaleId(200011);
            }
            // insert("insertExamResult1", examResult);
            // 如果是学校用户，还要填写维度表，用来做统计分析
            // if (examResult.getSubjectUserInfo().isShoolUser()) {//add by
            // zhaowanfeng

            // 插入维度"最终"计算得分，请注意是最终,即最终值
            examResultDao.insertExamDimResult(examResult);
            // }

            txManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
        }
    }

    public void deleteExamResultById(String resultId, String flag) {
        long delId = Long.valueOf(resultId);
        if ("1".equals(flag)) {
            examResultDao.deleteExamResultById(delId);
            examResultDao.deleteExamDimResultById(delId);
            examResultDao.deleteExamMhDimById(delId);
        } else {
            examResultDao.deleteTeaExamResultById(delId);
            examResultDao.deleteTeaExamDimResultById(delId);
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
        examResult.setMzm(student.getMzm());
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
        examResult.setMzm(teacher.getMzm());
        examResult.setCsrq(teacher.getCsrq());
        examResult.setGh(teacher.getGh());
        // examResult.setTeacherRole(teacher.getTeacherRole());
        examResult.setRoleid(examdoResult.getRoleid());
        examResult.setUserOrgId(examdoResult.getOrgid());
        examResult.setAttrs(teacher.getBjxx());
        examResult.setVersion(AcountTypeFlag.teacher.getId());
    }
}
