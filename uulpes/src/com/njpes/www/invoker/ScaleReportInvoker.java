package com.njpes.www.invoker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.assessmentcenter.ExamresultStudentMhMapper;
import com.njpes.www.dao.baseinfo.ParentMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.baseinfo.TeacherMapper;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.entity.scaletoollib.ExamdoStudent;
import com.njpes.www.entity.scaletoollib.ExamdoTeacher;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.descriptor.ScaleReportDescriptor;
//import edutec.scale.exam.ExamQuery;
import edutec.scale.exam.ExamResult;
import edutec.scale.explain.CompositeReport;
import edutec.scale.explain.RemarkReport;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.questionnaire.QuestionnaireReportListener;
import edutec.scale.util.ScaleUtils;
import heracles.util.UtilMisc;

//@Scope("prototype")
@Service("ScaleReportInvoker")
public class ScaleReportInvoker {
    @Autowired
    private ExamResultMapper examResultDao;
    @Autowired
    private StudentMapper studentDao;
    @Autowired
    private ExamresultStudentMhMapper examResultMhMapper;
    @Autowired
    private TeacherMapper teacherDao;
    @Autowired
    private ParentMapper parentDao;
    // @Autowired
    // private QuestionnaireReportListener listener;
    // @Autowired
    // private ScaleReportDescriptor reportDescription;

    private final Log logger = LogFactory.getLog(getClass());

    public Map<Object, Object> GetStudentPersonalReporter1(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload) throws Exception {
        // StudentWithBLOBs student =
        // studentDao.selectByPrimaryKey(subjectUserId);
        Map<Object, Object> params = new HashMap<Object, Object>();
        if (isDownload)
            params.put("download", "yes");
        else
            params.put("download", "no");
        String table = "examresult_student";
        Map<?, ?> param = UtilMisc.toMap(Constants.TABLE_PROP, table, Constants.ID_PROP, resultId);
        ExamResult examResult = examResultDao.getStudentExamResult(param);
        subjectUserId = examResult.getUserId();
        if (examResult == null) {
            logger.error("测试结果不存在，请查明原因");
            throw new Exception("测试结果不存在，请查明原因");
        }
        QuestionnaireReportListener listener = (QuestionnaireReportListener) SpringContextHolder
                .getBean("QuestionnaireReportListener", QuestionnaireReportListener.class);
        ExamdoStudent examdoStudent = getExamdoStudentFromExamresult(examResult);
        Student student = getStudentFromExamresult(examResult);
        if (ScaleUtils.isThirdAngleScale(examResult.getScaleId() + "")) {
            listener.setNeedCaclulate(false);
        }

        listener.setSubjectUserInfo(student);
        listener.setObserverUserInfo(obsverUser);
        Questionnaire questionnaire = examResult.toNewQuestionnaire(listener);
        questionnaire.setSubjectUserInfo(student);
        questionnaire.setExamdo(examdoStudent);
        ScaleReportDescriptor reportDescription = (ScaleReportDescriptor) SpringContextHolder
                .getBean("ScaleReportDescriptor", ScaleReportDescriptor.class);
        reportDescription.setQuestionnaire(questionnaire);
        reportDescription.setResultId(resultId);// 用于三角视获取教师、家长测试结果
        reportDescription.setThreeAngleUUID(threeAngleUUID);
        questionnaire.setDescriptor(reportDescription);
        reportDescription.setObserverUserInfo(obsverUser);
        reportDescription.setSubjectUserInfo(student);
        try {
            params.put(QuestionnaireReportListener.CTL_BROW_REPORT, "t");
            params.put(QuestionnaireReportListener.FLT_START_TM, examResult.getStartTime());
            params.put(QuestionnaireReportListener.FLT_END_TM, examResult.getOkTime());
            params.put("gradeorderid", examResult.getUserGradeOrderId());
            params.put("classid", examResult.getUserClassId());

            questionnaire.close(params);
            // params.put("template", "closure");
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Object, Object> GetThreeAngleReporterForStudent(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload) {
        Map<Object, Object> params = new HashMap<Object, Object>();
        if (isDownload)
            params.put("download", "yes");
        else
            params.put("download", "no");
        ExamResult examResult = examResultDao.getExamResultMh(resultId, AcountTypeFlag.student.getId());
        if (examResult == null) {
            logger.error("测试结果不存在，请查明原因");
        }
        QuestionnaireReportListener listener = (QuestionnaireReportListener) SpringContextHolder
                .getBean("QuestionnaireReportListener", QuestionnaireReportListener.class);
        ExamdoStudent examdoStudent = getExamdoStudentFromExamresult(examResult);
        Student student = getStudentFromExamresult(examResult);
        if (ScaleUtils.isThirdAngleScale(examResult.getScaleId() + "")) {
            listener.setNeedCaclulate(false);
        }

        listener.setSubjectUserInfo(student);
        listener.setObserverUserInfo(obsverUser);
        Questionnaire questionnaire = examResult.toNewQuestionnaire(listener);
        questionnaire.setSubjectUserInfo(student);
        questionnaire.setExamdo(examdoStudent);
        ScaleReportDescriptor reportDescription = (ScaleReportDescriptor) SpringContextHolder
                .getBean("ScaleReportDescriptor", ScaleReportDescriptor.class);
        reportDescription.setQuestionnaire(questionnaire);
        reportDescription.setResultId(resultId);// 用于三角视获取教师、家长测试结果
        reportDescription.setThreeAngleUUID(threeAngleUUID);
        questionnaire.setDescriptor(reportDescription);
        reportDescription.setObserverUserInfo(obsverUser);
        reportDescription.setSubjectUserInfo(student);
        try {
            params.put(QuestionnaireReportListener.CTL_BROW_REPORT, "t");
            params.put(QuestionnaireReportListener.FLT_START_TM, examResult.getStartTime());
            params.put(QuestionnaireReportListener.FLT_END_TM, examResult.getOkTime());
            params.put("gradeorderid", examResult.getUserGradeOrderId());
            params.put("classid", examResult.getUserClassId());

            questionnaire.close(params);
            // params.put("template", "closure");
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Object, Object> GetThreeAngleReporterForTeacher(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload) {
        Map<Object, Object> params = new HashMap<Object, Object>();
        if (isDownload)
            params.put("download", "yes");
        else
            params.put("download", "no");
        ExamResult examResult = examResultDao.getExamResultMh(resultId, AcountTypeFlag.teacher.getId());
        if (examResult == null) {
            logger.error("测试结果不存在，请查明原因");
        }
        QuestionnaireReportListener listener = (QuestionnaireReportListener) SpringContextHolder
                .getBean("QuestionnaireReportListener", QuestionnaireReportListener.class);
        ExamdoStudent examdoStudent = getExamdoStudentFromExamresult(examResult);
        Student student = getStudentFromExamresult(examResult);
        if (ScaleUtils.isThirdAngleScale(examResult.getScaleId() + "")) {
            listener.setNeedCaclulate(false);
        }

        listener.setSubjectUserInfo(student);
        listener.setObserverUserInfo(obsverUser);
        Questionnaire questionnaire = examResult.toNewQuestionnaire(listener);
        questionnaire.setSubjectUserInfo(student);
        questionnaire.setExamdo(examdoStudent);
        ScaleReportDescriptor reportDescription = (ScaleReportDescriptor) SpringContextHolder
                .getBean("ScaleReportDescriptor", ScaleReportDescriptor.class);
        reportDescription.setQuestionnaire(questionnaire);
        reportDescription.setResultId(resultId);// 用于三角视获取教师、家长测试结果
        reportDescription.setThreeAngleUUID(threeAngleUUID);
        questionnaire.setDescriptor(reportDescription);
        reportDescription.setObserverUserInfo(obsverUser);
        reportDescription.setSubjectUserInfo(student);
        try {
            params.put(QuestionnaireReportListener.CTL_BROW_REPORT, "t");
            params.put(QuestionnaireReportListener.FLT_START_TM, examResult.getStartTime());
            params.put(QuestionnaireReportListener.FLT_END_TM, examResult.getOkTime());
            params.put("gradeorderid", examResult.getUserGradeOrderId());
            params.put("classid", examResult.getUserClassId());

            questionnaire.close(params);
            // params.put("template", "closure");
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Object, Object> GetThreeAngleReporterForParent(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload) {
        Map<Object, Object> params = new HashMap<Object, Object>();
        if (isDownload)
            params.put("download", "yes");
        else
            params.put("download", "no");
        ExamResult examResult = examResultDao.getExamResultMh(resultId, AcountTypeFlag.parent.getId());
        if (examResult == null) {
            logger.error("测试结果不存在，请查明原因");
        }
        QuestionnaireReportListener listener = (QuestionnaireReportListener) SpringContextHolder
                .getBean("QuestionnaireReportListener", QuestionnaireReportListener.class);
        ExamdoStudent examdoStudent = getExamdoStudentFromExamresult(examResult);
        Student student = getStudentFromExamresult(examResult);
        if (ScaleUtils.isThirdAngleScale(examResult.getScaleId() + "")) {
            listener.setNeedCaclulate(false);
        }

        listener.setSubjectUserInfo(student);
        listener.setObserverUserInfo(obsverUser);
        Questionnaire questionnaire = examResult.toNewQuestionnaire(listener);
        questionnaire.setSubjectUserInfo(student);
        questionnaire.setExamdo(examdoStudent);
        ScaleReportDescriptor reportDescription = (ScaleReportDescriptor) SpringContextHolder
                .getBean("ScaleReportDescriptor", ScaleReportDescriptor.class);
        reportDescription.setQuestionnaire(questionnaire);
        reportDescription.setResultId(resultId);// 用于三角视获取教师、家长测试结果
        reportDescription.setThreeAngleUUID(threeAngleUUID);
        questionnaire.setDescriptor(reportDescription);
        reportDescription.setObserverUserInfo(obsverUser);
        reportDescription.setSubjectUserInfo(student);
        try {
            params.put(QuestionnaireReportListener.CTL_BROW_REPORT, "t");
            params.put(QuestionnaireReportListener.FLT_START_TM, examResult.getStartTime());
            params.put(QuestionnaireReportListener.FLT_END_TM, examResult.getOkTime());
            params.put("gradeorderid", examResult.getUserGradeOrderId());
            params.put("classid", examResult.getUserClassId());

            questionnaire.close(params);
            // params.put("template", "closure");
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Object, Object> getTeacherPersonalReport(long subjectUserId, Object obsverUser, long resultId,
            boolean isDownload) {
        // TeacherWithBLOBs teacher =
        // teacherDao.selectByPrimaryKey(subjectUserId);
        Map<Object, Object> params = new HashMap<Object, Object>();
        if (isDownload)
            params.put("download", "yes");
        else
            params.put("download", "no");
        String table = "examresult_teacher";
        Map<?, ?> param = UtilMisc.toMap(Constants.TABLE_PROP, table, Constants.ID_PROP, resultId);
        ExamResult examResult = examResultDao.getTeacherExamResult(param);
        if (examResult == null) {
            logger.error("测试结果不存在，请查明原因");
        }
        Teacher teacher = getTeacherFromExamresult(examResult);
        ExamdoTeacher examdoTeacher = getExamdoTeacherFromExamresult(examResult);
        QuestionnaireReportListener listener = (QuestionnaireReportListener) SpringContextHolder
                .getBean("QuestionnaireReportListener", QuestionnaireReportListener.class);
        if (ScaleUtils.isThirdAngleScale(examResult.getScaleId() + "")) {
            listener.setNeedCaclulate(false);
        }

        listener.setSubjectUserInfo(teacher);
        listener.setObserverUserInfo(obsverUser);
        Questionnaire questionnaire = examResult.toNewQuestionnaire(listener);
        questionnaire.setSubjectUserInfo(teacher);
        questionnaire.setExamdo(examdoTeacher);
        ScaleReportDescriptor reportDescription = (ScaleReportDescriptor) SpringContextHolder
                .getBean("ScaleReportDescriptor", ScaleReportDescriptor.class);
        reportDescription.setQuestionnaire(questionnaire);
        reportDescription.setResultId(resultId);
        questionnaire.setDescriptor(reportDescription);
        reportDescription.setObserverUserInfo(obsverUser);
        reportDescription.setSubjectUserInfo(teacher);
        try {
            params.put(QuestionnaireReportListener.CTL_BROW_REPORT, "t");
            params.put(QuestionnaireReportListener.FLT_START_TM, examResult.getStartTime());
            params.put(QuestionnaireReportListener.FLT_END_TM, examResult.getOkTime());
            questionnaire.close(params);
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Object, Object> downloadStuPersonalReporter(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID) {
        StudentWithBLOBs student = studentDao.selectByPrimaryKey(subjectUserId);
        Map<Object, Object> params = new HashMap<Object, Object>();
        params.put("download", "yes");
        String table = "examresult_student";
        Map<?, ?> param = UtilMisc.toMap(Constants.TABLE_PROP, table, Constants.ID_PROP, resultId);
        ExamResult examResult = examResultDao.getStudentExamResult(param);
        if (examResult == null) {
            logger.error("测试结果不存在，请查明原因");
        }
        examResult.setObserverUserInfo(obsverUser);
        examResult.setSubjectUserInfo(student);
        QuestionnaireReportListener listener = (QuestionnaireReportListener) SpringContextHolder
                .getBean("QuestionnaireReportListener", QuestionnaireReportListener.class);
        ;
        // QuestionnaireReportListener listener = new
        // QuestionnaireReportListener();
        if (ScaleUtils.isThirdAngleScale(examResult.getScaleId() + "")) {
            listener.setNeedCaclulate(false);
        }

        listener.setSubjectUserInfo(student);

        listener.setObserverUserInfo(obsverUser);
        Questionnaire questionnaire = examResult.toNewQuestionnaire(listener);
        questionnaire.setSubjectUserInfo(student);

        // ScaleReportDescriptor reportDescription = new
        // ScaleReportDescriptor();
        ScaleReportDescriptor reportDescription = (ScaleReportDescriptor) SpringContextHolder
                .getBean("ScaleReportDescriptor", ScaleReportDescriptor.class);
        reportDescription.setQuestionnaire(questionnaire);
        reportDescription.setResultId(resultId);// 用于三角视获取教师、家长测试结果
        reportDescription.setThreeAngleUUID(threeAngleUUID);
        questionnaire.setDescriptor(reportDescription);
        reportDescription.setObserverUserInfo(obsverUser);
        reportDescription.setSubjectUserInfo(student);
        try {
            params.put(QuestionnaireReportListener.CTL_BROW_REPORT, "t");
            params.put(QuestionnaireReportListener.FLT_START_TM, examResult.getStartTime());
            params.put(QuestionnaireReportListener.FLT_END_TM, examResult.getOkTime());
            params.put("gradeorderid", examResult.getUserGradeOrderId());
            params.put("classid", examResult.getUserClassId());

            questionnaire.close(params);
            // params.put("template", "closure");
            return params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> exportStudentSynthesizeReport(long subjectUserId, Object obsverUser) {
        StudentWithBLOBs student = studentDao.selectByPrimaryKey(subjectUserId);
        Map<Object, Object> params = new HashMap<Object, Object>();
        String reportTile = null;
        CompositeReport report = null;
        try {
            report = new CompositeReport(student, obsverUser);
            report.setExamResultMapper(examResultDao);
            reportTile = report.getGroupTitle();

            if (reportTile == null) {
                String message = "<html><head><title>没有复合报告</title><meta http-equiv='Content-type' content='text/html; charset=UTF-8'></head>";
                message += "<body>";
                message += "<table align=center><tr height=100 valign=center><td><p>没有查询符合条件的数据组装复合报告！</p></td></tr></table>";
                message += "</body></html>";
                params.put("message", message);
            }
            report.exportReport(false);
            return report.getDataMap();
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> exportStudentSynthesizeReport(long subjectUserId, Object obsverUser, String starttime,
            String endtime) {
        StudentWithBLOBs student = studentDao.selectByPrimaryKey(subjectUserId);
        Map<Object, Object> params = new HashMap<Object, Object>();
        String reportTile = null;
        CompositeReport report = null;
        try {
            report = new CompositeReport(student, obsverUser, starttime, endtime);
            report.setExamResultMapper(examResultDao);
            reportTile = report.getGroupTitle();

            if (reportTile == null) {
                String message = "<html><head><title>没有复合报告</title><meta http-equiv='Content-type' content='text/html; charset=UTF-8'></head>";
                message += "<body>";
                message += "<table align=center><tr height=100 valign=center><td><p>没有查询符合条件的数据组装复合报告！</p></td></tr></table>";
                message += "</body></html>";
                params.put("message", message);
            }
            report.exportReport(false);
            return report.getDataMap();
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 个人评语报告
     * 
     * @param subjectUserId
     * @param obsverUser
     * @return
     */
    public Map<String, Object> exportStudentRemarkReport(long subjectUserId, Object obsverUser) {
        StudentWithBLOBs student = studentDao.selectByPrimaryKey(subjectUserId);
        Map<Object, Object> params = new HashMap<Object, Object>();
        String reportTile = null;
        RemarkReport report = null;
        try {
            report = new RemarkReport(student, obsverUser);
            report.setExamResultMapper(examResultDao);
            reportTile = report.getGroupTitle();

            if (reportTile == null) {
                String message = "<html><head><title>没有个人评语</title><meta http-equiv='Content-type' content='text/html; charset=UTF-8'></head>";
                message += "<body>";
                message += "<table align=center><tr height=100 valign=center><td><p>没有查询符合条件的数据组装个人评语！</p></td></tr></table>";
                message += "</body></html>";
                params.put("message", message);
            }
            report.exportRemarkReport(false);
            return report.getDataMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> exportTeacherSynthesizeReport(long subjectUserId, Object obsverUser) {
        Teacher teacher = teacherDao.selectByPrimaryKey(subjectUserId);
        Map<Object, Object> params = new HashMap<Object, Object>();
        String reportTile = null;
        CompositeReport report = null;
        try {
            // report =
            // SpringContextHolder.getBean("compositeReport",CompositeReport.class);
            report = new CompositeReport(teacher, obsverUser);
            report.setExamResultMapper(examResultDao);
            reportTile = report.getGroupTitle();

            if (reportTile == null) {
                String message = "<html><head><title>没有复合报告</title><meta http-equiv='Content-type' content='text/html; charset=UTF-8'></head>";
                message += "<body>";
                message += "<table align=center><tr height=100 valign=center><td><p>没有查询符合条件的数据组装复合报告！</p></td></tr></table>";
                message += "</body></html>";
                params.put("message", message);
            }
            // else if(output==null){
            // report.writeReportToRtf(output);
            // return null;
            // }
            // else{
            // report.writeReportToHtml(null);
            // return report.getRootMap();
            report.exportReport(false);
            return report.getDataMap();
            // }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> downloadTeacherSynthesizeReport(long subjectUserId, Object obsverUser) {
        Teacher teacher = teacherDao.selectByPrimaryKey(subjectUserId);
        Map<Object, Object> params = new HashMap<Object, Object>();
        String reportTile = null;
        CompositeReport report = null;
        try {
            // report =
            // SpringContextHolder.getBean("compositeReport",CompositeReport.class);
            report = new CompositeReport(teacher, obsverUser);
            report.setExamResultMapper(examResultDao);
            reportTile = report.getGroupTitle();

            if (reportTile == null) {
                String message = "<html><head><title>没有复合报告</title><meta http-equiv='Content-type' content='text/html; charset=UTF-8'></head>";
                message += "<body>";
                message += "<table align=center><tr height=100 valign=center><td><p>没有查询符合条件的数据组装复合报告！</p></td></tr></table>";
                message += "</body></html>";
                params.put("message", message);
            }
            report.exportReport(true);
            return report.getDataMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> downloadStudentSynthesizeReport(long subjectUserId, Object obsverUser) {
        StudentWithBLOBs student = studentDao.selectByPrimaryKey(subjectUserId);
        Map<Object, Object> params = new HashMap<Object, Object>();
        String reportTile = null;
        CompositeReport report = null;
        try {
            // report =
            // SpringContextHolder.getBean("compositeReport",CompositeReport.class);
            report = new CompositeReport(student, obsverUser);
            report.setExamResultMapper(examResultDao);
            reportTile = report.getGroupTitle();

            if (reportTile == null) {
                String message = "<html><head><title>没有复合报告</title><meta http-equiv='Content-type' content='text/html; charset=UTF-8'></head>";
                message += "<body>";
                message += "<table align=center><tr height=100 valign=center><td><p>没有查询符合条件的数据组装复合报告！</p></td></tr></table>";
                message += "</body></html>";
                params.put("message", message);
            }
            report.exportReport(true);
            return report.getDataMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Object, Object> getStudentGroupReportForSchool(Map paramets) {
        List<ExamResult> list = examResultDao.getStuErForSchool(paramets);
        String gradeId = (String) paramets.get("gradeId"); // 得到分析的所有班级
        String[] classId = (String[]) paramets.get("classids"); // 得到分析的所有班级
        String scaleId = (String) paramets.get("scaleId"); // 得到量表
        String startDate = (String) paramets.get("startDate"); // 开始时间
        String endDate = (String) paramets.get("endDate"); // 结束时间
        // segStatic = SegmentStaticFactory.createSegmentStatic(scaleId, org,
        // gradeId, classId);
        /*
         * GroupReportStatic grStatic = new GroupReportStatic(scaleId, gradeId,
         * classId); for (ExamResult examResult : list) {
         * grStatic.incExmresult(examResult); } Map<Object,Object> page=
         * grStatic.complete(); page.put("startDate", startDate);
         * page.put("endDate", endDate);
         * 
         * return page;
         */
        return null;
    }

    private ExamdoStudent getExamdoStudentFromExamresult(ExamResult examResult) {
        ExamdoStudent examdoStudent = new ExamdoStudent();
        examdoStudent.setBj(examResult.getUserClassId());
        examdoStudent.setBjmc(examResult.getBjmc());
        examdoStudent.setCsrq(examResult.getCsrq());
        examdoStudent.setNj(examResult.getNj());
        examdoStudent.setNjmc(examResult.getNjmc());
        examdoStudent.setXd(examResult.getXd());
        examdoStudent.setAttrs(examResult.getAttrs());
        examdoStudent.setXbm(examResult.getUserGender());
        examdoStudent.setXh(examResult.getXh());
        examdoStudent.setSfzjh(examResult.getSfzjh());
        examdoStudent.setXm(examResult.getUserName());
        examdoStudent.setXmpy(examResult.getXmpy());
        examdoStudent.setXxmc(examResult.getXxmc());
        examdoStudent.setGradeid(examResult.getUserGradeOrderId());
        examdoStudent.setOrgid(examResult.getUserOrgId());
        return examdoStudent;
    }

    private ExamdoTeacher getExamdoTeacherFromExamresult(ExamResult examResult) {
        ExamdoTeacher examdoTeacher = new ExamdoTeacher();
        examdoTeacher.setCsrq(examResult.getCsrq());
        examdoTeacher.setRoleid(examResult.getRoleid());
        examdoTeacher.setAttrs(examResult.getAttrs());
        examdoTeacher.setXbm(examResult.getUserGender());
        examdoTeacher.setSfzjh(examResult.getSfzjh());
        examdoTeacher.setXm(examResult.getUserName());
        examdoTeacher.setXmpy(examResult.getXmpy());
        examdoTeacher.setXxmc(examResult.getXxmc());
        examdoTeacher.setOrgid(examResult.getUserOrgId());
        return examdoTeacher;
    }

    private Student getStudentFromExamresult(ExamResult er) {
        Student stu = new Student();
        stu.setXm(er.getUserName());
        stu.setXbm(er.getUserGender());
        stu.setNjmc(er.getNjmc());
        stu.setBjmch(er.getBjmc());
        stu.setXh(er.getXh());
        stu.setCsrq(er.getCsrq());
        // stu.setMzm(er.getMzm());
        stu.setXmpy(er.getXmpy());
        stu.setXxmc(er.getXxmc());
        stu.setSfzjh(er.getSfzjh());
        stu.setId(er.getUserId());
        return stu;
    }

    private Teacher getTeacherFromExamresult(ExamResult examResult) {
        Teacher t = new Teacher();
        t.setXm(examResult.getUserName());
        t.setXbm(examResult.getUserGender());
        t.setXxmc(examResult.getXxmc());
        t.setXmpy(examResult.getXmpy());
        t.setCsrq(examResult.getCsrq());
        t.setGh(examResult.getGh());
        t.setSfzjh(examResult.getSfzjh());
        t.setId(examResult.getUserId());
        return t;
    }
}
