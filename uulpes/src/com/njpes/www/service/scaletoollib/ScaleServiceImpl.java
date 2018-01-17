package com.njpes.www.service.scaletoollib;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.njpes.www.dao.assessmentcenter.ExamresultStudentMhMapper;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultTeacherMapper;
import com.njpes.www.dao.scaletoollib.NormInfoMapper;
import com.njpes.www.dao.scaletoollib.ScaleInfoDao;
import com.njpes.www.dao.scaletoollib.ScaleMapper;
import com.njpes.www.dao.scaletoollib.ScaleNormLogMapper;
import com.njpes.www.dao.scaletoollib.ScaleOperatorDao;
import com.njpes.www.dao.scaletoollib.ScalenormMapper;
import com.njpes.www.dao.util.DictionaryMapper;
import com.njpes.www.entity.assessmentcenter.ExamresultStudentMhWithBLOBs;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.ExamResultForNorm;
//import com.njpes.www.entity.baseinfo.UserInfo;
import com.njpes.www.entity.scaletoollib.NormInfo;
import com.njpes.www.entity.scaletoollib.Normobject;
import com.njpes.www.entity.scaletoollib.QueryInfo;
import com.njpes.www.entity.scaletoollib.ScaleDetailInfo;
import com.njpes.www.entity.scaletoollib.ScaleFilterParam;
import com.njpes.www.entity.scaletoollib.Scalenorm;
import com.njpes.www.entity.scaletoollib.ScalenormKey;
import com.njpes.www.entity.scaletoollib.Scaletype;
import com.njpes.www.entity.scaletoollib.StudentExamAnswer;
import com.njpes.www.entity.scaletoollib.TeacherExamAnswer;
import com.njpes.www.invoker.DownloadScaleAnswerInvoker;
import com.njpes.www.invoker.DownloadScaleAnswerTmpInvoker;
import com.njpes.www.invoker.ImportScaleInvoker;
import com.njpes.www.invoker.ScaleAnswerInvoker;
import com.njpes.www.invoker.ScaleReportInvoker;
import com.njpes.www.service.baseinfo.DistrictService;

import edutec.admin.ExportUtils;
import edutec.scale.db.CachedScaleMgr;
import edutec.scale.db.ScaleMgr;
import edutec.scale.exam.ExamResult;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.explain.group.StudentGroupReportForCity;
import edutec.scale.explain.group.StudentGroupReportForCounty;
import edutec.scale.explain.group.StudentGroupReportForSchool;
import edutec.scale.explain.group.StudentGroupReportForSubSchool;
import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import edutec.scale.model.ScaleNormObject;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.QuestionnaireUtils;
import edutec.scale.util.ScaleUtils;
import heracles.excel.ExcelException;
import heracles.excel.WorkbookUtils;
import heracles.util.UtilMisc;
import heracles.web.config.ApplicationConfiguration;

@Scope("prototype")
@Service("ScaleService")
public class ScaleServiceImpl implements ScaleService {
    @Autowired
    ScaleInfoDao scaleInfoDao;
    @Autowired
    ScaleMapper scaleMapper;
    @Autowired
    ScaleOperatorDao scaleOperatorDao;
    @Autowired
    DictionaryMapper dictionaryMapper;
    @Autowired
    private ScaleMgr scaleMgrtool;
    private StudentMapper studentDao;
    @Autowired
    ImportScaleInvoker importScaleInvoker;
    @Autowired
    ScaleAnswerInvoker scaleAnswerInvoker;
    @Autowired
    DownloadScaleAnswerTmpInvoker downloadScaleAnswerTmpInvoker;
    @Autowired
    DownloadScaleAnswerInvoker downloadScaleAnswerInvoker;
    @Autowired
    ScaleReportInvoker scaleReportInvoker;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    ScalenormMapper normMapper;
    @Autowired
    ScaleNormLogMapper normLogMapper;
    @Autowired
    NormInfoMapper normInfoMapper;
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
    @Autowired
    private ScaleMgrService scaleMgrService;
    @Autowired
    private PlatformTransactionManager txManager;

    public ScaleInfoDao getScaleInfoDao() {
        return scaleInfoDao;
    }

    public void setScaleInfoDao(ScaleInfoDao scaleInfoDao) {
        this.scaleInfoDao = scaleInfoDao;
    }

    public StudentMapper getStudentDao() {
        return studentDao;
    }

    public void setStudentDao(StudentMapper studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public List getScaleTypes() {
        // TODO Auto-generated method stub
        // 基础信息操作，通过学校、年级、班级查询所有的学生
        // List<Student> stus = studentDao.selectByOrgId(1l);
        // List<Student> stus1 = studentDao.selectByGradeId(1l);
        // List<Student> stus2 = studentDao.selectByClassId(1l);
        return scaleInfoDao.getAllScaleType();
    }

    @Override
    public List getScaleTypesByGroupid(int groupid) {
        return scaleInfoDao.getScaleTypeByGroup(groupid);
    }

    @Override
    public Boolean addScaleType(Scaletype scaleType) {
        // TODO Auto-generated method stub
        try {
            int res = scaleInfoDao.insertScaleType(scaleType);
            if (res > 0) {
                return true;
            }
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public Boolean delScaleType(int scaleTypeId) {
        // TODO Auto-generated method stub
        try {
            int res = scaleInfoDao.deleteScaleType(scaleTypeId);
            if (res > 0) {
                return true;
            }
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public List QueryScaleList(QueryInfo para) {
        // TODO Auto-generated method stub
        return scaleInfoDao.queryScaleList(para);
    }

    @Override
    public ScaleDetailInfo getScaleInfo(int scaleId) {
        // TODO Auto-generated method stub
        return scaleInfoDao.getScaleDetailById(scaleId);
    }

    @Override
    public String ImportScale(InputStream stream, String excelUrl, long userid, Organization org) throws Exception {
        return importScaleInvoker.importExcel(stream, excelUrl, userid, org, scaleMgrtool);
    }

    @Override
    public String ImportScaleQuestion(InputStream stream, String excelUrl, Organization org) throws Exception {
        return importScaleInvoker.importScaleQuestionExcel(stream, excelUrl);
    }

    @Override
    public FileInputStream downloadScale(String scaletitle, Map saveInfo) throws Exception {
        // 直接返回流
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        String path = servletContext.getRealPath("/");
        // File tmpDir = new
        // File(ApplicationConfiguration.getInstance().getWorkDir() + "/" +
        // ExportUtils.SCALE_TMP_DIR + "/" + scaletitle+".xlsx");
        File tmpDir = new File(ApplicationConfiguration.getInstance().getWorkDir() + "/" + ExportUtils.SCALE_TMP_DIR
                + "/" + scaletitle + ".xlsx");
        saveInfo.put("extendname", "xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(tmpDir);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            // tmpDir = new
            // File(ApplicationConfiguration.getInstance().getWorkDir() + "/" +
            // ExportUtils.SCALE_TMP_DIR + "/" + scaletitle+".zip");
            tmpDir = new File(ApplicationConfiguration.getInstance().getWorkDir() + "/" + ExportUtils.SCALE_TMP_DIR
                    + "/" + scaletitle + ".xlsx");
            try {
                in = new FileInputStream(tmpDir);
                // saveInfo.put("extendname", "zip");
                saveInfo.put("extendname", "xlsx");
            } catch (FileNotFoundException ex) {
                throw new Exception("没有可供下载的量表题本模板!");
            }
            // e.printStackTrace();

        }

        return in;
    }

    @Override
    public List DeleteScales(List scaleids) {
        // TODO Auto-generated method stub
        List fails = new ArrayList<String>();
        for (Object scaleid : scaleids) {
            try {
                scaleMgrtool.deleteScale((String) scaleid);
            } catch (Exception e) {
                fails.add(scaleid);
            }
        }
        return fails;
    }

    @Override
    public Integer ImportIndividualAnswers(String url) {

        return null;
    }

    @Override
    public Integer ImportGroupAnswers(InputStream inputStream) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FileInputStream personalReportExport(int scaleId, String type, String userid) {
        // TODO Auto-generated method stub
        // scaleReportInvoker.GetStudentPersonalReporter(subjectUserInfo,
        // obsverUserInfo, resultId, threeAngleUUID);
        return null;
    }

    @Override
    public FileInputStream personalReportExport(long resultId, String subjectUserId, String obsverUserId,
            String threeAngleUUID) {
        // TODO Auto-generated method stub
        // scaleReportInvoker.GetStudentPersonalReporter1(subjectUserId,
        // obsverUserId, resultId, threeAngleUUID);
        return null;
    }

    @Override
    public Map<Object, Object> getStudentPersonalReporter(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload) throws Exception {
        return scaleReportInvoker.GetStudentPersonalReporter1(subjectUserId, obsverUser, resultId, threeAngleUUID,
                isDownload);
    }

    @Override
    public Map<Object, Object> GetThreeAngleReporterForStudent(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload) {
        return scaleReportInvoker.GetThreeAngleReporterForStudent(subjectUserId, obsverUser, resultId, threeAngleUUID,
                isDownload);
    }

    @Override
    public Map<Object, Object> GetThreeAngleReporterForTeacher(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload) {
        return scaleReportInvoker.GetThreeAngleReporterForTeacher(subjectUserId, obsverUser, resultId, threeAngleUUID,
                isDownload);
    }

    @Override
    public Map<Object, Object> GetThreeAngleReporterForParent(long subjectUserId, Object obsverUser, long resultId,
            String threeAngleUUID, boolean isDownload) {
        return scaleReportInvoker.GetThreeAngleReporterForParent(subjectUserId, obsverUser, resultId, threeAngleUUID,
                isDownload);

    }

    @Override
    public FileInputStream personalCompositeReportExport(List scaleIds, String type, String userid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FileInputStream groupReportExport(int scaleId, String type, int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Scalenorm> getNorm(int scaleId) {
        // TODO Auto-generated method stub
        ScalenormKey normKey = new ScalenormKey();
        normKey.setScaleId(scaleId);
        return normMapper.selectByPrimaryKey(normKey);
    }

    @Override
    public List<NormInfo> getNormInfo(int scaleId) {
        return normInfoMapper.selectByScaleId(scaleId);
    }

    @Override
    public Boolean addNorm(int scaleId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String downloadScaleModel() {
        // TODO Auto-generated method stub
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String path = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getServletPath()))
                + "/files/scale-tpl.xls";
        return path;
    }

    @Override
    public FileInputStream downloadAnswerModel(String scaleid, String type, int recordId) {
        // TODO Auto-generated method stub
        // downloadScaleAnswerTmpInvoker.downloadAnswerTemp(userList,scaleid,1,
        // os);
        return null;
    }

    @Override
    public String downloadAnswerModelForStu(String scaleid, long orgid, long nj, long bj, OutputStream outputStream) {
        Map<String, Object> params = new HashMap<String, Object>();
        // List<StudentWithBLOBs> stuList = studentDao.getStudents(params);

        downloadScaleAnswerTmpInvoker.downloadStudentAnswerTemp(orgid, nj, bj, scaleid, 1, outputStream);
        return null;
    }

    @Override
    public String downloadAnswerModelForTeach(String scaleid, long orgid, String roleName, long teacherRole,
            OutputStream outputStream) {
        Map<String, Object> params = new HashMap<String, Object>();
        downloadScaleAnswerTmpInvoker.downloadTeacherAnswerTemp(orgid, roleName, teacherRole, scaleid, outputStream);
        return null;
    }

    @Override
    public List getScaleSource() {
        // TODO Auto-generated method stub
        return this.dictionaryMapper.selectAllDic("dic_scale_source");
        // return this.scaleInfoDao.getAllScaleSource();
    }

    @Override
    public Map<Object, Object> getTeacherPersonalReporter(long subjectUserId, Object obsverUser, long resultId,
            boolean isDownload) {
        // TODO Auto-generated method stub
        return scaleReportInvoker.getTeacherPersonalReport(subjectUserId, obsverUser, resultId, isDownload);
    }

    @Override
    public Map<String, Object> getStudentCompositeReport(long subjectUserId, Object obsverUser) {
        // TODO Auto-generated method stub
        return scaleReportInvoker.exportStudentSynthesizeReport(subjectUserId, obsverUser);
    }

    @Override
    public Map<String, Object> getStudentCompositeReport(long subjectUserId, Object obsverUser, String starttime,
            String endtime) {
        return scaleReportInvoker.exportStudentSynthesizeReport(subjectUserId, obsverUser, starttime, endtime);
    }

    @Override
    public Map<String, Object> getStudentRemarkReport(long subjectUserId, Object obsverUser) {
        // TODO Auto-generated method stub
        return scaleReportInvoker.exportStudentRemarkReport(subjectUserId, obsverUser);
    }

    @Override
    public Map<String, Object> downloadStudentCompositeReport(long subjectUserId, Object obsverUser) {
        // TODO Auto-generated method stub
        return scaleReportInvoker.downloadStudentSynthesizeReport(subjectUserId, obsverUser);
    }

    @Override
    public Map<String, Object> getTeacherCompositeReport(long subjectUserId, Object observerUser) {
        return scaleReportInvoker.exportTeacherSynthesizeReport(subjectUserId, observerUser);
    }

    @Override
    public Map<String, Object> downloadTeacherCompositeReport(long subjectUserId, Object observerUser) {
        return scaleReportInvoker.downloadTeacherSynthesizeReport(subjectUserId, observerUser);
    }

    @Override
    public Map<Object, Object> getStudentGroupReportForSchool(long orgid, String njmc, String[] bjArray, String scaleId,
            String startDate, String endDate) {
        Map<String, Object> parmeters = new HashMap<String, Object>();
        parmeters.put("orgid", orgid);
        parmeters.put("njmc", njmc);
        parmeters.put("bjArray", bjArray);
        parmeters.put("scaleid", scaleId);
        parmeters.put("starttime", startDate);
        parmeters.put("endtime", endDate);

        List<ExamResult> list = examResultDao.getStuErForSchool(parmeters);
        StudentGroupReportForSchool grStatic = new StudentGroupReportForSchool(orgid, scaleId, njmc, bjArray, startDate,
                endDate);
        for (ExamResult examResult : list) {
            grStatic.incExmresult(examResult);
        }
        Map<Object, Object> page = grStatic.complete();
        page.put("startDate", startDate);
        page.put("endDate", endDate);

        return page;

        // return scaleReportInvoker.getStudentGroupReportForSchool(parmeters);
    }

    @Override
    public Map<Object, Object> getStudentGroupReportForCounty(String countyid, String[] schoolnames, String njmc,
            String scaleid, String startDate, String endDate) {
        Map<String, Object> parmeters = new HashMap<String, Object>();
        parmeters.put("countyid", countyid);
        parmeters.put("xxmcArray", schoolnames);
        parmeters.put("njmc", njmc);
        parmeters.put("schoolnames", schoolnames);
        parmeters.put("scaleid", scaleid);
        parmeters.put("starttime", startDate);
        parmeters.put("endtime", endDate);
        List<ExamResult> list = examResultDao.getStuErForCounty(parmeters);
        District district = districtService.selectByCode(countyid);
        StudentGroupReportForCounty grStatic = new StudentGroupReportForCounty(district.getName(), countyid, scaleid,
                njmc, schoolnames, startDate, endDate);
        for (ExamResult examResult : list) {
            grStatic.incExmresult(examResult);
        }
        Map<Object, Object> page = grStatic.complete();
        page.put("startDate", startDate);
        page.put("endDate", endDate);

        return page;
    }

    @Override
    public Map<Object, Object> getStudentGroupReportForCity(String cityname, String[] qxArray, String njmc,
            String scaleid, String startDate, String endDate) {
        Map<String, Object> parmeters = new HashMap<String, Object>();
        parmeters.put("qxArray", qxArray);
        parmeters.put("njmc", njmc);
        parmeters.put("scaleid", scaleid);
        parmeters.put("starttime", startDate);
        parmeters.put("endtime", endDate);
        // districtService.getCounties(cityid);
        List<ExamResult> list = examResultDao.getStuErForCity(parmeters);
        StudentGroupReportForCity grStatic = new StudentGroupReportForCity(cityname, qxArray, scaleid, njmc, startDate,
                endDate);
        for (ExamResult examResult : list) {
            grStatic.incExmresult(examResult);
        }
        Map<Object, Object> page = grStatic.complete();
        page.put("startDate", startDate);
        page.put("endDate", endDate);

        return page;

    }

    @Override
    public Map<Object, Object> getStudentGroupReportForSubSchool(String cityname, String[] zsxxorgids, String njmc,
            String scaleid, String startDate, String endDate) {
        Map<String, Object> parmeters = new HashMap<String, Object>();
        parmeters.put("zzxxorgidArray", zsxxorgids);
        parmeters.put("njmc", njmc);
        parmeters.put("scaleid", scaleid);
        parmeters.put("starttime", startDate);
        parmeters.put("endtime", endDate);
        List<ExamResult> list = examResultDao.getStuErForCitySubSchool(parmeters);
        StudentGroupReportForSubSchool grStatic = new StudentGroupReportForSubSchool(cityname, zsxxorgids, scaleid,
                njmc, startDate, endDate);
        for (ExamResult examResult : list) {
            grStatic.incExmresult(examResult);
        }
        Map<Object, Object> page = grStatic.complete();
        page.put("startDate", startDate);
        page.put("endDate", endDate);

        return page;

    }

    @Override
    public List queryScaleByGroupId(int groupid) {
        return scaleInfoDao.getScaleTypeByGroup(groupid);

    }

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
    public void downloadTchAnswerForEdu(int orglevel, String roleid, String rolename, List countyIdList, String scaleId,
            String[] attrnames, String[] attrfields, String[] attrIds, String startTime, String endTime,
            OutputStream outputStream) {
        // downloadScaleAnswerInvoker.downloadTchAnswerForEdu(orglevel,sb.toString(),roleid,rolename,
        // scaleId,attrnames,attrIds,attrfields,startTime,endTime,
        // outputStream);
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
    public List<NormInfo> getScaleNorminfo(NormInfo norminfo) {
        return normInfoMapper.selectByNormType(norminfo);
    }

    @Override
    public String ImportScaleNorm(InputStream stream, long orgid, int orglevel, String scaleId, int areaid, long userid,
            String description, String editer, String edittime, String excelUrl) throws Exception {

        return importScaleInvoker.importScaleNormExcel(stream, scaleId, orgid, orglevel, areaid, userid, description,
                editer, edittime, excelUrl, scaleMgrtool);
    }

    @Override
    public List<Scalenorm> getNormById(String scaleid, int normid) {
        return normMapper.selectByNormid(scaleid, normid);
    }

    @Override
    public String createNorm(String scaleid, Organization org, long userid, String normname, String description,
            String starttime, String endtime) {
        if (ScaleUtils.isThirdAngleScale(scaleid))
            return gernarateThreeAngleNorm(scaleid, org, userid, normname, description, starttime, endtime);
        else
            return gernarateNorm(scaleid, org, userid, normname, description, starttime, endtime);
    }

    public String gernarateNorm(String scaleid, Organization org, long userid, String normname, String description,
            String starttime, String endtime) {
        int orglevel = org.getOrgLevel();
        List<ExamResultForNorm> examResultList = null;
        int areaid = 0;
        if (orglevel == 4) {
            areaid = Integer.parseInt(org.getCountyid());
            Map<?, ?> param = UtilMisc.toMap("scaleid", scaleid, "countyid", org.getCountyid(), "orglevel", orglevel,
                    "starttime", starttime, "endtime", endtime);
            examResultList = examResultDao.getExamResultForNorm(param);
        }
        if (orglevel == 3) {
            areaid = Integer.parseInt(org.getCityid());
            Map<?, ?> param = UtilMisc.toMap("scaleid", scaleid, "cityid", org.getCityid(), "orglevel", orglevel,
                    "starttime", starttime, "endtime", endtime);
            examResultList = examResultDao.getExamResultForNorm(param);
        }
        if (orglevel == 6) {
            Map<?, ?> param = UtilMisc.toMap("scaleid", scaleid, "orgid", org.getId(), "orglevel", orglevel,
                    "starttime", starttime, "endtime", endtime);
            examResultList = examResultDao.getExamResultForNorm(param);
        }
        // 遍历一次测量结果数据，求出每个维度平均分
        int recordSize = examResultList.size();
        Map<String, Map<String, NormUnit>> normMap = new HashMap<String, Map<String, NormUnit>>();
        for (int i = 0; i < examResultList.size(); i++) {
            ExamResultForNorm er = examResultList.get(i);
            String csrq = er.getCsrq();
            String dimscoreStr = er.getDim_score();
            int gender = er.getGender();
            int gradeid = er.getRealgradeid();
            String key = gradeid + "_" + gender;
            String[] dimscoreArray = dimscoreStr.split("#");

            if (normMap.get(key) == null) {
                Map<String, NormUnit> dimscoreMap = new HashMap<String, NormUnit>();
                for (int j = 0; j < dimscoreArray.length; j++) {
                    String dimscore = dimscoreArray[j];
                    String[] diminfoArray = dimscore.split(",");
                    String dimTitle = diminfoArray[0];
                    String rawScore = diminfoArray[1];
                    NormUnit nu = new NormUnit();
                    nu.setM(0);
                    nu.setSd(0);
                    nu.setM(Double.valueOf(rawScore) / recordSize);
                    dimscoreMap.put(dimTitle, nu);
                }
                normMap.put(key, dimscoreMap);
            } else {
                Map<String, NormUnit> dimscoreMap = normMap.get(key);
                for (int j = 0; j < dimscoreArray.length; j++) {
                    String dimscore = dimscoreArray[j];
                    String[] diminfoArray = dimscore.split(",");
                    String dimTitle = diminfoArray[0];
                    String rawScore = diminfoArray[1];
                    NormUnit nu = dimscoreMap.get(dimTitle);
                    // Double raw = dimscoreMap.get(dimTitle);
                    if (nu == null) {
                        nu = new NormUnit();
                        nu.setM(Double.valueOf(rawScore));
                        dimscoreMap.put(dimTitle, nu);
                    } else {
                        double sum = nu.getM() + Double.parseDouble(rawScore) / recordSize;
                        nu.setM(sum);
                    }
                }
            }
        }

        // 再遍历一次数据，求出标准差
        for (int i = 0; i < examResultList.size(); i++) {
            ExamResultForNorm er = examResultList.get(i);
            String csrq = er.getCsrq();
            String dimscoreStr = er.getDim_score();
            int gender = er.getGender();
            int gradeid = er.getRealgradeid();
            String key = gradeid + "_" + gender;
            String[] dimscoreArray = dimscoreStr.split("#");

            Map<String, NormUnit> dimscoreMap = normMap.get(key);
            if (dimscoreMap != null) {
                for (int j = 0; j < dimscoreArray.length; j++) {
                    String dimscore = dimscoreArray[j];
                    String[] diminfoArray = dimscore.split(",");
                    String dimTitle = diminfoArray[0];
                    String rawScore = diminfoArray[1];
                    NormUnit nu = dimscoreMap.get(dimTitle);

                    if (nu != null) {
                        double m = nu.getM();
                        double raw = Double.parseDouble(rawScore);
                        double sd = nu.getSd() + ((raw - m) * (raw - m)) / recordSize;
                        nu.setSd(sd);
                    }
                }
            }
        }
        // 计算维度平均分和标准差，并存入数据库
        List<ScaleNormObject> normObjList = new ArrayList<ScaleNormObject>();
        for (Map.Entry<String, Map<String, NormUnit>> entry : normMap.entrySet()) {
            String grade_genderStr = entry.getKey();
            Map<String, NormUnit> dimScoreInfoMap = entry.getValue();
            String[] grade_gender = grade_genderStr.split("_");
            String grade = grade_gender[0];
            String gender = grade_gender[1];

            for (Map.Entry<String, NormUnit> dimScoreInfo : dimScoreInfoMap.entrySet()) {
                String dimTitle = dimScoreInfo.getKey();
                NormUnit nu = dimScoreInfo.getValue();
                double m = nu.getM();
                double sd = Math.sqrt(nu.getSd());
                ScaleNormObject sno = new ScaleNormObject();
                sno.setScale_id(scaleid);
                sno.setW_id(dimTitle);
                sno.setType(2);
                sno.setGender(Integer.parseInt(gender));
                sno.setGrade_id(Integer.parseInt(grade));
                dimScoreInfoMap.get(dimTitle);
                sno.setM(m);
                sno.setSd(sd);
                normObjList.add(sno);
            }

        }
        Scale scale = cachedScaleMgr.get(scaleid);

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            // 向数据库中添加常模
            // scaleMgrtool.appendNormCustomList(normObjList,Integer.parseInt(scaleId),orglevel,Integer.parseInt(areaid),userid);
            scaleMgrtool.appendNormList(normObjList, Integer.parseInt(scaleid), 2, org.getId(), userid);

            scaleMgrtool.appendNormInfo(scaleid, normname, 2, new Date(), userid, description, areaid, org.getId(),
                    orglevel, null, null);
            // 向数据库中的添加结果解释和指导建议
            txManager.commit(status); // 提交事务
        } catch (Exception e) {
            // 否则回滚
            e.printStackTrace();
            txManager.rollback(status);
            return "fail";
        }
        return "success";
    }

    public String gernarateThreeAngleNorm(String scaleId, Organization org, long userid, String normname,
            String description, String starttime, String endtime) {
        String[] thirdAngleScaleIDs = null;
        if (ScaleUtils.isThirdAngleScaleP(scaleId)) {
            thirdAngleScaleIDs = new String[] { "111000001", "111000002", "111000003" };
        }
        if (ScaleUtils.isThirdAngleScaleM(scaleId)) {
            thirdAngleScaleIDs = new String[] { "110100001", "110100002", "110100003" };
        }
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        for (int k = 0; k < 3; k++) {
            String scaleid = thirdAngleScaleIDs[k];
            int orglevel = org.getOrgLevel();
            List<ExamResultForNorm> examResultList = null;
            int areaid = 0;
            if (orglevel == 4) {
                areaid = Integer.parseInt(org.getCountyid());
                Map<?, ?> param = UtilMisc.toMap("scaleid", scaleid, "countyid", org.getCountyid(), "orglevel",
                        orglevel, "starttime", starttime, "endtime", endtime);
                examResultList = examResultDao.getExamResultForNorm(param);
            }
            if (orglevel == 3) {
                areaid = Integer.parseInt(org.getCityid());
                Map<?, ?> param = UtilMisc.toMap("scaleid", scaleid, "cityid", org.getCityid(), "orglevel", orglevel,
                        "starttime", starttime, "endtime", endtime);
                examResultList = examResultDao.getExamResultForNorm(param);
            }
            if (orglevel == 6) {
                Map<?, ?> param = UtilMisc.toMap("scaleid", scaleid, "orgid", org.getId(), "orglevel", orglevel,
                        "starttime", starttime, "endtime", endtime);
                examResultList = examResultDao.getExamResultForNorm(param);
            }
            // 遍历一次测量结果数据，求出每个维度平均分
            int recordSize = examResultList.size();
            Map<String, Map<String, NormUnit>> normMap = new HashMap<String, Map<String, NormUnit>>();
            for (int i = 0; i < examResultList.size(); i++) {
                ExamResultForNorm er = examResultList.get(i);
                String csrq = er.getCsrq();
                String dimscoreStr = er.getDim_score();
                int gender = er.getGender();
                int gradeid = er.getRealgradeid();
                String key = gradeid + "_" + gender;
                String[] dimscoreArray = dimscoreStr.split("#");

                if (normMap.get(key) == null) {
                    Map<String, NormUnit> dimscoreMap = new HashMap<String, NormUnit>();
                    for (int j = 0; j < dimscoreArray.length; j++) {
                        String dimscore = dimscoreArray[j];
                        String[] diminfoArray = dimscore.split(",");
                        String dimTitle = diminfoArray[0];
                        String rawScore = diminfoArray[1];
                        NormUnit nu = new NormUnit();
                        nu.setM(0);
                        nu.setSd(0);
                        nu.setM(Double.valueOf(rawScore) / recordSize);
                        dimscoreMap.put(dimTitle, nu);
                    }
                    normMap.put(key, dimscoreMap);
                } else {
                    Map<String, NormUnit> dimscoreMap = normMap.get(key);
                    for (int j = 0; j < dimscoreArray.length; j++) {
                        String dimscore = dimscoreArray[j];
                        String[] diminfoArray = dimscore.split(",");
                        String dimTitle = diminfoArray[0];
                        String rawScore = diminfoArray[1];
                        NormUnit nu = dimscoreMap.get(dimTitle);
                        // Double raw = dimscoreMap.get(dimTitle);
                        if (nu == null) {
                            nu = new NormUnit();
                            nu.setM(Double.valueOf(rawScore));
                            dimscoreMap.put(dimTitle, nu);
                        } else {
                            double sum = nu.getM() + Double.parseDouble(rawScore) / recordSize;
                            nu.setM(sum);
                        }
                    }
                }
            }

            // 再遍历一次数据，求出标准差
            for (int i = 0; i < examResultList.size(); i++) {
                ExamResultForNorm er = examResultList.get(i);
                String csrq = er.getCsrq();
                String dimscoreStr = er.getDim_score();
                int gender = er.getGender();
                int gradeid = er.getRealgradeid();
                String key = gradeid + "_" + gender;
                String[] dimscoreArray = dimscoreStr.split("#");

                Map<String, NormUnit> dimscoreMap = normMap.get(key);
                if (dimscoreMap != null) {
                    for (int j = 0; j < dimscoreArray.length; j++) {
                        String dimscore = dimscoreArray[j];
                        String[] diminfoArray = dimscore.split(",");
                        String dimTitle = diminfoArray[0];
                        String rawScore = diminfoArray[1];
                        NormUnit nu = dimscoreMap.get(dimTitle);

                        if (nu != null) {
                            double m = nu.getM();
                            double raw = Double.parseDouble(rawScore);
                            double sd = nu.getSd() + ((raw - m) * (raw - m)) / recordSize;
                            nu.setSd(sd);
                        }
                    }
                }
            }
            // 计算维度平均分和标准差，并存入数据库
            List<ScaleNormObject> normObjList = new ArrayList<ScaleNormObject>();
            for (Map.Entry<String, Map<String, NormUnit>> entry : normMap.entrySet()) {
                String grade_genderStr = entry.getKey();
                Map<String, NormUnit> dimScoreInfoMap = entry.getValue();
                String[] grade_gender = grade_genderStr.split("_");
                String grade = grade_gender[0];
                String gender = grade_gender[1];

                for (Map.Entry<String, NormUnit> dimScoreInfo : dimScoreInfoMap.entrySet()) {
                    String dimTitle = dimScoreInfo.getKey();
                    NormUnit nu = dimScoreInfo.getValue();
                    double m = nu.getM();
                    double sd = Math.sqrt(nu.getSd());
                    ScaleNormObject sno = new ScaleNormObject();
                    sno.setScale_id(scaleid);
                    sno.setW_id(dimTitle);
                    sno.setType(2);
                    sno.setGender(Integer.parseInt(gender));
                    sno.setGrade_id(Integer.parseInt(grade));
                    dimScoreInfoMap.get(dimTitle);
                    sno.setM(m);
                    sno.setSd(sd);
                    normObjList.add(sno);
                }
            }
            // Scale scale = cachedScaleMgr.get(scaleid);

            try {
                // 向数据库中添加常模
                scaleMgrtool.appendNormList(normObjList, Integer.parseInt(scaleid), 2, org.getId(), userid);
                if (k == 3)// 三级视量表只需要注册学生版，教师版和家长版 的id号与学生版相同。
                    scaleMgrtool.appendNormInfo(scaleid, normname, 2, new Date(), userid, description, areaid,
                            org.getId(), orglevel, null, null);
                // 向数据库中的添加结果解释和指导建议

            } catch (Exception e) {
                // 否则回滚
                e.printStackTrace();
                txManager.rollback(status);
                return "fail";
            }
        }
        txManager.commit(status); // 提交事务
        return "success";
    }

    public class NormUnit {
        private double m;
        private double sd;

        public double getM() {
            return m;
        }

        public void setM(double m) {
            this.m = m;
        }

        public double getSd() {
            return sd;
        }

        public void setSd(double sd) {
            this.sd = sd;
        }

    }

    @Override
    public String downloadscalenormtemp(Scale scale, OutputStream outputStream) throws Exception {
        InputStream in = null;
        ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
        String fileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/scalenormtemp.xls");
        in = new BufferedInputStream(new FileInputStream(fileName));
        // XSSFWorkbook workbook = null;

        // workbook = new XSSFWorkbook(in);
        HSSFWorkbook workbook = null;
        workbook = WorkbookUtils.openWorkbook(in);
        HSSFSheet sheet = null;
        sheet = workbook.getSheetAt(0);

        String scaleId = scale.getId();
        HSSFCellStyle cellStyle = workbook.createCellStyle();// 建立单元格格式
        HSSFFont font = workbook.createFont();
        HSSFRow titlerow = sheet.getRow(2);
        // 填写维度名称行
        List<Dimension> dimList = scale.getDimensions();
        int idx = 2;
        int idx1 = 2;
        for (int i = 0; i < dimList.size(); i++) {
            Dimension dim = dimList.get(i);
            String dimId = dim.getId();
            if (dimId.equals("W00"))
                continue;
            if (dimId.equals("W0")) {
                if (ScaleUtils.isMentalHealthScale(scaleId))
                    continue;
            }
            String dimTitle = dim.getTitle();

            WorkbookUtils.createMergedCell2003(sheet, titlerow, 2, idx, dimTitle, 2, idx1, 2, idx1 + 1, cellStyle, font,
                    0);

            // WorkbookUtils.setCellValue(sheet, 2, idx,dimTitle);
            WorkbookUtils.setCellValue(sheet, 3, idx, "均值");
            WorkbookUtils.setCellValue(sheet, 3, idx + 1, "标准差");
            idx = idx + 2;
            idx1 = idx1 + 2;
        }

        int rindex = 4;
        idx = 0;
        String gradegroups = scale.getApplicablePerson();
        if (StringUtils.isNotEmpty(gradegroups)) {
            String[] groups = gradegroups.split("、");
            for (String group : groups) {
                if (group.equals("小学")) {

                    for (int j = 1; j < 7; j++) {
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "男");
                        rindex++;
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "女");
                        rindex++;
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "");
                        rindex++;
                    }
                    //
                }
                if (group.equals("初中")) {
                    for (int j = 7; j < 10; j++) {
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "男");
                        rindex++;
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "女");
                        rindex++;
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "");
                        rindex++;
                    }

                }
                if (group.equals("高中")) {
                    for (int j = 10; j < 13; j++) {
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "男");
                        rindex++;
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "女");
                        rindex++;
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "");
                        rindex++;
                    }

                }
                if (group.equals("成人")) {
                    for (int j = 14; j < 15; j++) {
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "男");
                        rindex++;
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "女");
                        rindex++;
                        WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "");
                        rindex++;
                    }

                }
            }
            WorkbookUtils.setCellValue(sheet, rindex, 1, "男");
            rindex++;
            WorkbookUtils.setCellValue(sheet, rindex, 1, "女");
            rindex++;
            WorkbookUtils.setCellValue(sheet, rindex, 1, "");

        }

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.copy(inputStream, outputStream);
            IOUtils.closeQuietly(output);
        } catch (IOException e) {
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);

        }
        return null;
    }

    @Override
    public int deleteScaleNorm(String scaleid, int normid) {
        // TransactionDefinition def = new DefaultTransactionDefinition();
        // TransactionStatus status = txManager.getTransaction(def);
        try {
            // 删除常模表记录
            int n = normMapper.deleteByNormid(normid, scaleid);
            // 删除常模注册表
            n = normInfoMapper.deleteByPrimaryKey(normid);
            // txManager.commit(status); //提交事务
            return n;
        } catch (Exception e) {
            // 否则回滚
            e.printStackTrace();
            // txManager.rollback(status);
        }
        return 0;
    }

    @Override
    public void exportScaleNorm(Scale scale, int normid, OutputStream outputStream) throws Exception {

        List<Scalenorm> normlist = getNormById(scale.getCode(), normid);
        Map<String, Map<String, Normobject>> normMap = new HashMap<String, Map<String, Normobject>>();
        Map<String, Normobject> normobjMap = null;
        for (int i = 0; i < normlist.size(); i++) {
            Scalenorm norm = normlist.get(i);
            int grade = norm.getGradeOrderId();
            int gender = norm.getGender();
            String wid = norm.getwId();
            float m = norm.getM();
            float sd = norm.getSd();
            Dimension dim = scale.findDimension(wid);
            String dimid = dim.getId();
            String dimtitle = dim.getTitle();
            Normobject no = new Normobject();
            no.setDimtitle(dimtitle);

            no.setM(String.valueOf(m));
            no.setSd(String.valueOf(sd));
            String key = grade + "_" + gender;
            if (normMap.get(key) == null) {
                normobjMap = new HashMap();
                normMap.put(key, normobjMap);
            } else {
                normobjMap = (Map) normMap.get(key);
            }
            normobjMap.put(dimid, no);

        }

        InputStream in = null;
        ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
        String fileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/scalenormtemp.xls");
        in = new BufferedInputStream(new FileInputStream(fileName));
        HSSFWorkbook workbook = null;

        // workbook = new XSSFWorkbook(in);
        workbook = WorkbookUtils.openWorkbook(in);

        HSSFSheet sheet = null;
        sheet = workbook.getSheetAt(0);

        String scaleId = scale.getId();
        HSSFCellStyle cellStyle = workbook.createCellStyle();// 建立单元格格式
        HSSFFont font = workbook.createFont();
        HSSFRow titlerow = sheet.getRow(2);
        {
            // 填写维度名称行
            List<Dimension> dimList = scale.getDimensions();
            int idx = 2;
            int idx1 = 2;
            for (int i = 0; i < dimList.size(); i++) {
                Dimension dim = dimList.get(i);
                String dimId = dim.getId();
                if (dimId.equals("W00"))
                    continue;
                if (dimId.equals("W0")) {
                    if (ScaleUtils.isMentalHealthScale(scaleId))
                        continue;
                }
                String dimTitle = dim.getTitle();

                WorkbookUtils.createMergedCell2003(sheet, titlerow, 2, idx, dimTitle, 2, idx1, 2, idx1 + 1, cellStyle,
                        font, 0);

                // WorkbookUtils.setCellValue(sheet, 2, idx,dimTitle);
                WorkbookUtils.setCellValue(sheet, 3, idx, "均值");
                WorkbookUtils.setCellValue(sheet, 3, idx + 1, "标准差");
                idx = idx + 2;
                idx1 = idx1 + 2;
            }

            int rindex = 4;
            idx = 0;
            String key = null;
            String gradegroups = scale.getApplicablePerson();
            if (StringUtils.isNotEmpty(gradegroups)) {
                String[] groups = gradegroups.split("、");
                for (String group : groups) {
                    if (group.equals("小学")) {
                        for (int j = 1; j < 7; j++) {
                            key = j + "_1";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "男");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }

                            key = j + "_2";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "女");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }
                            key = j + "_0";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }
                        }
                        //
                    }
                    if (group.equals("初中")) {
                        for (int j = 7; j < 10; j++) {

                            key = j + "_1";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "男");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }

                            key = j + "_2";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "女");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }
                            key = j + "_0";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }
                        }

                    }
                    if (group.equals("高中")) {
                        for (int j = 10; j < 13; j++) {

                            key = j + "_1";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "男");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }

                            key = j + "_2";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "女");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }
                            key = j + "_0";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }
                        }

                    }
                    if (group.equals("成人")) {
                        for (int j = 14; j < 15; j++) {

                            key = "14_1";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "男");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }

                            key = "14_2";
                            {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "女");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }
                            key = "14_0";
                            if (normMap.get(key) != null) {
                                WorkbookUtils.setCellValue(sheet, rindex, 0, j + "");
                                WorkbookUtils.setCellValue(sheet, rindex, 1, "");
                                writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                                rindex++;
                            }
                        }

                    }
                    key = "0_0";
                    if (normMap.get(key) != null) {
                        WorkbookUtils.setCellValue(sheet, rindex, 0, "");
                        WorkbookUtils.setCellValue(sheet, rindex, 1, "");
                        writeNormItem(sheet, scale.getCode(), dimList, normMap, key, rindex, 2);
                        rindex++;
                    }

                }

            }

        }
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            WorkbookUtils.saveWorkbook(workbook, output);
            output.flush();
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            IOUtils.copy(inputStream, outputStream);
            IOUtils.closeQuietly(output);
        } catch (IOException e) {
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);

        }

    }

    private void writeNormItem(HSSFSheet sheet, String scaleid, List<Dimension> dimList,
            Map<String, Map<String, Normobject>> normMap, String key, int row, int column) {
        Map<String, Normobject> normobjMap = normMap.get(key);
        int idx = 0;
        for (int i = 0; i < dimList.size(); i++) {
            Dimension dim = dimList.get(i);
            String dimId = dim.getId();
            if (dimId.equals("W00"))
                continue;
            if (ScaleUtils.isMentalHealthScale(scaleid)) {
                if (dimId.equals("W0"))
                    continue;
            }
            Normobject no = normobjMap.get(dimId);
            if (no == null)
                continue;
            WorkbookUtils.setCellValue(sheet, row, column + idx, no.getM());
            idx++;
            WorkbookUtils.setCellValue(sheet, row, column + idx, no.getSd());
            idx++;
        }
    }

    public boolean updateShowTitle(String code, String showtitle){
       boolean update = scaleOperatorDao.updateScaleShowTitle(code, showtitle);
       return update;
    }

	@Override
	public List<Scale> getAllScale() {
		List<Scale> scales = scaleMapper.getAllScale();
		return scales;
	};

}
