package com.njpes.www.service.scaletoollib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.Gson;
import com.njpes.www.dao.assessmentcenter.ExamresultStudentMhMapper;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.ExamresultTeacherMapper;
import com.njpes.www.dao.systeminfo.ExamdoStudentMapper;
import com.njpes.www.dao.systeminfo.ExamdoTeacherMapper;
import com.njpes.www.entity.assessmentcenter.ExamresultStudentMhWithBLOBs;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.ExamresultStudent;
import com.njpes.www.entity.scaletoollib.ExamresultStudentWithBackGround;
import com.njpes.www.entity.scaletoollib.ExamresultTeacher;
import com.njpes.www.entity.scaletoollib.ReportLookStudentFilterParam;
import com.njpes.www.entity.scaletoollib.ReportLookTeacherFilterParam;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.entity.scaletoollib.StudentExamAnswer;
import com.njpes.www.entity.scaletoollib.TeacherExamAnswer;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.utils.PageParameter;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.exam.ExamResult;
import edutec.scale.exception.QuestionnaireException;
import edutec.scale.explain.group.StudentGroupReportForCity;
import edutec.scale.explain.group.StudentGroupReportForCounty;
import edutec.scale.explain.group.StudentGroupReportForSchool;
import edutec.scale.explain.group.StudentGroupReportForSubSchool;
import edutec.scale.explain.group.TeacherGroupReportForCity;
import edutec.scale.explain.group.TeacherGroupReportForCounty;
import edutec.scale.explain.group.TeacherGroupReportForSchool;
import edutec.scale.explain.group.TeacherGroupReportForSubSchool;
import edutec.scale.model.Scale;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.QuestionnaireUtils;
import edutec.scale.util.ScaleUtils;

@Service("ReportLookService")
public class ReportLookServiceImpl implements ReportLookService {

    static Logger log = Logger.getLogger(ReportLookServiceImpl.class.getName());
    @Autowired
    private CachedScaleMgr cachedScaleMgr;
    @Autowired
    private ExamresultStudentMapper examresultStudentMapper;
    @Autowired
    private ExamresultTeacherMapper examresultTeacherMapper;
    @Autowired
    ExamdoStudentMapper examdoStudentMapper;
    @Autowired
    ExamdoTeacherMapper examdoTeacherMapper;
    @Autowired
    private ExamResultMapper examResultDao;
    @Autowired
    ExamResultMapper examresultMapper;
    @Autowired
    DistrictService districtService;

    @Autowired
    private ExamresultStudentMhMapper examresultStudentMhMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    @Override
    public List<ExamresultStudentWithBackGround> getStudentResultByPage(
            ReportLookStudentFilterParam reportLookStudentFilterParam, PageParameter page) {
        // TODO Auto-generated method stub
        if (!StringUtil.isEmpty(reportLookStudentFilterParam.getEndtime()))
            reportLookStudentFilterParam.setEndtime(reportLookStudentFilterParam.getEndtime() + " 24:00:00");
        List<ExamresultStudentWithBackGround> esList = examresultStudentMapper
                .getStudentResultByPage(reportLookStudentFilterParam, page);
        // try{
        // compareTime(esList,reportLookStudentFilterParam);
        // }catch (Exception e) {
        // e.printStackTrace();
        // }
        return esList;
    }

    private void compareTime(List<ExamresultStudentWithBackGround> esList,
            ReportLookStudentFilterParam reportLookStudentFilterParam) throws ParseException {
        if (reportLookStudentFilterParam.getStarttime() != null && reportLookStudentFilterParam.getEndtime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Iterator<ExamresultStudentWithBackGround> it = esList.listIterator();
            while (it.hasNext()) {
                ExamresultStudent esw = it.next();
                Date starttime = sdf.parse(reportLookStudentFilterParam.getStarttime());
                Date endtime = sdf.parse(reportLookStudentFilterParam.getEndtime());
                if (starttime.before(esw.getStartTime()) || endtime.after(esw.getOkTime())) {
                    it.remove();
                }
            }
            return;
        } else if (reportLookStudentFilterParam.getStarttime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Iterator<ExamresultStudentWithBackGround> it = esList.listIterator();
            while (it.hasNext()) {
                ExamresultStudentWithBackGround esw = it.next();
                Date starttime = sdf.parse(reportLookStudentFilterParam.getStarttime());
                if (starttime.after(esw.getStartTime())) {
                    it.remove();
                }
            }
            return;
        } else if (reportLookStudentFilterParam.getEndtime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Iterator<ExamresultStudentWithBackGround> it = esList.listIterator();
            while (it.hasNext()) {
                ExamresultStudentWithBackGround esw = it.next();
                Date endtime = sdf.parse(reportLookStudentFilterParam.getEndtime());
                if (endtime.before(esw.getStartTime())) {
                    it.remove();
                }
            }
            return;
        }
    }

    @Override
    public List<ExamresultTeacher> getTeacherResultByPage(ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            PageParameter page) {
        // TODO Auto-generated method stub
        if (!StringUtil.isEmpty(reportLookTeacherFilterParam.getEndtime()))
            reportLookTeacherFilterParam.setEndtime(reportLookTeacherFilterParam.getEndtime() + " 24:00:00");
        return examresultTeacherMapper.getTeacherResultByPage(reportLookTeacherFilterParam, page);
    }

    @Override
    public List<ExamresultStudent> getStuPersonalReportByPage(ReportLookStudentFilterParam reportLookStudentFilterParam,
            PageParameter page) {
        // TODO Auto-generated method stub
        if (!StringUtil.isEmpty(reportLookStudentFilterParam.getEndtime()))
            reportLookStudentFilterParam.setEndtime(reportLookStudentFilterParam.getEndtime() + " 24:00:00");
        return examresultStudentMapper.getStuPersonalReportByPage(page, reportLookStudentFilterParam);
    }

    @Override
    public List<ExamresultStudentWithBackGround> getStuComplianceReportByPage(
            ReportLookStudentFilterParam reportLookStudentFilterParam, PageParameter page) {
        // TODO Auto-generated method stub
        return examresultStudentMapper.getStuComplianceReportByPage(page, reportLookStudentFilterParam);
    }

    @Override
    public List<ExamresultStudent> getStuPersonalCommentsByPage(
            ReportLookStudentFilterParam reportLookStudentFilterParam, PageParameter page) {
        // TODO Auto-generated method stub
        return examresultStudentMapper.getStuPersonalCommentsByPage(page, reportLookStudentFilterParam);
    }

    @Override
    public List<ExamresultStudentWithBackGround> getStuTeamReportByPage(
            ReportLookStudentFilterParam reportLookStudentFilterParam, PageParameter page) {
        // TODO Auto-generated method stub
        // return examresultStudentMapper.getStuTeamReportByPage(page,
        // reportLookStudentFilterParam);
        return null;
    }

    @Override
    public List<ExamresultTeacher> getTeacherPersonalReportByPage(
            ReportLookTeacherFilterParam reportLookTeacherFilterParam, PageParameter page) {
        // TODO Auto-generated method stub
        return examresultTeacherMapper.getTeacherPersonalReportByPage(reportLookTeacherFilterParam, page);
    }

    @Override
    public List<ExamresultTeacher> getTeaComplianceReportByPage(
            ReportLookTeacherFilterParam reportLookTeacherFilterParam, PageParameter page) {
        // TODO Auto-generated method stub
        return examresultTeacherMapper.getTeaComplianceReportByPage(reportLookTeacherFilterParam, page);
    }

    @Override
    public List<ExamresultTeacher> getTeaTeamReportsByPage(ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            PageParameter page) {
        // TODO Auto-generated method stub
        return examresultTeacherMapper.getTeaTeamReportsByPage(reportLookTeacherFilterParam, page);
    }

    @Override
    public List<ExamresultStudent> queryDistinctXDFromExamresultStudent(int orgId) {
        // TODO Auto-generated method stub
        List<ExamresultStudent> les = examresultStudentMapper.queryDistinctXDFromExamresultStudent(orgId);
        for (ExamresultStudent es : les) {
            String xdmc = XueDuan.valueOf(es.getXd()).getInfo();
            es.setXdmc(xdmc);
        }
        // MDC.put("orglevelid", 1);
        // MDC.put("orgid", 2);
        // log.info("ok");
        return les;
    }

    // 根据组织机构，学段查找年级
    @Override
    public String queryDistinctNJFromExamresultStudent(Organization org, int xd, boolean isSonSchool) {
        // TODO Auto-generated method stub
        List<ExamresultStudent> les = examresultStudentMapper.queryDistinctNJFromExamresultStudent(org, xd,
                isSonSchool);
        Gson gson = new Gson();
        String result = gson.toJson(les);
        return result;
    }

    @Override
    public String queryDistinctNJByCountyidOrSchoolid(Organization org, String id, int xd, boolean isSonSchool) {
        // TODO Auto-generated method stub
        List<ExamresultStudent> les = examresultStudentMapper.queryDistinctNJByCountyidOrSchoolid(org, id, xd,
                isSonSchool);
        Gson gson = new Gson();
        String result = gson.toJson(les);
        return result;
    }

    @Override
    public String getEduScaleInfoFromExamdoStudent(Organization org, String countyidOrSchoolid, int xd, int nj,
            boolean isSonSchool) {
        List<ScaleInfo> scaleList = null;
        if (StringUtils.isEmpty(countyidOrSchoolid))
            scaleList = examresultStudentMapper.queryEduScaleFromExamresultStudent(org, xd, nj, -1, -1, isSonSchool);
        else
            scaleList = examresultStudentMapper.queryEduScaleByCountyidOrSchoolid(org, countyidOrSchoolid, xd, nj, -1,
                    -1, isSonSchool);
        Map resultMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        List scaleTypeList = new ArrayList();
        List scaleSourceList = new ArrayList();
        Map scaleSourceMap = new HashMap<String, String>();
        Map scaleTypeMap = new HashMap<String, String>();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                ScaleInfo scaleInfo = scaleList.get(i);
                String scaleid = scaleInfo.getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleInfo.getTitle());
                scaleinfoList.add(scaleinfo);

                String scalesource = scaleInfo.getScalesource() + "";
                String sourcename = ScaleUtils.SCALE_SOURCE_DESC.get(scalesource);
                scaleSourceMap.put(scalesource, sourcename);

                String scaletype = scaleInfo.getScaletype();
                String typename = ScaleUtils.SCALE_TYPE_DESC.get(scaletype);
                scaleTypeMap.put(scaletype, typename);
            }
        }
        resultMap.put("scaleList", scaleinfoList);
        resultMap.put("scaleTypeMap", scaleTypeMap);
        resultMap.put("scaleSourceMap", scaleSourceMap);

        Gson gson = new Gson();
        String classStr = gson.toJson(resultMap);
        return classStr;
    }

    @Override
    public String getEduScaleInfoFromExamdoStudent(Organization org, String countyidOrSchoolid, int xd, int nj,
            int scaletype, int scalesource, boolean isSonSchool) {
        List<ScaleInfo> scaleList = null;
        if (StringUtils.isEmpty(countyidOrSchoolid))
            scaleList = examresultStudentMapper.queryEduScaleFromExamresultStudent(org, xd, nj, scaletype, scalesource,
                    isSonSchool);
        else
            scaleList = examresultStudentMapper.queryEduScaleByCountyidOrSchoolid(org, countyidOrSchoolid, xd, nj,
                    scaletype, scalesource, isSonSchool);
        Map resultMap = new HashMap<String, Object>();
        List scaleinfoList = new ArrayList();
        List scaleTypeList = new ArrayList();
        List scaleSourceList = new ArrayList();
        Map scaleSourceMap = new HashMap<String, String>();
        Map scaleTypeMap = new HashMap<String, String>();
        if (scaleList != null) {
            for (int i = 0; i < scaleList.size(); i++) {

                Map scaleinfo = new HashMap();
                ScaleInfo scaleInfo = scaleList.get(i);
                String scaleid = scaleInfo.getCode();
                if (ScaleUtils.isThirdAngleScaleM1(scaleid) || ScaleUtils.isThirdAngleScaleP1(scaleid))
                    continue;
                scaleinfo.put("code", scaleid);
                scaleinfo.put("title", scaleInfo.getTitle());
                scaleinfoList.add(scaleinfo);
            }
        }
        resultMap.put("scaleList", scaleinfoList);
        resultMap.put("scaleTypeMap", scaleTypeMap);
        resultMap.put("scaleSourceMap", scaleSourceMap);

        Gson gson = new Gson();
        String classStr = gson.toJson(resultMap);
        return classStr;
    }

    // 根据组织机构，查找年级
    @Override
    public List<ExamresultStudent> queryDistinctNJFromExamresultStudentAccordingOrgId(int orgId) {
        // TODO Auto-generated method stub
        List<ExamresultStudent> les = examresultStudentMapper.queryDistinctNJFromExamresultStudentAccordingOrgId(orgId);
        return les;
    }

    @Override
    public String queryDistinctBJFromExamresultStudent(int orgId, int xd, int nj) {
        // TODO Auto-generated method stub
        List<ExamresultStudent> les = examresultStudentMapper.queryDistinctBJFromExamresultStudent(orgId, xd, nj);
        Gson gson = new Gson();
        String result = gson.toJson(les);
        return result;
    }

    @Override
    public String queryDistinctBJFromExamresultStudentByNJ(int orgId, String njmc) {
        List<ExamresultStudent> les = examresultStudentMapper.queryDistinctBJFromExamresultStudentByNJ(orgId, njmc);
        Gson gson = new Gson();
        String result = gson.toJson(les);
        return result;
    }

    @Override
    public List<Scale> queryDistinctScales(Organization org) {
        // TODO Auto-generated method stub
        return examresultStudentMapper.queryDistinctScales(org);
    }

    @Override
    public List<Scale> queryDistinctScalesInSchool(Organization org,
            ReportLookStudentFilterParam reportLookStudentFilterParam) {
        return examresultStudentMapper.queryDistinctScalesInSchool(org, reportLookStudentFilterParam);
    }

    @Override
    public List<Scale> queryDistinceScalesInCounty(String countyid,
            ReportLookStudentFilterParam reportLookStudentFilterParam) {
        return examresultStudentMapper.queryDistinctScalesInCounty(countyid, reportLookStudentFilterParam);
    }

    @Override
    public List<Scale> queryDistinctScalesInCity(String cityid, String xzxs,
            ReportLookStudentFilterParam reportLookStudentFilterParam) {
        return examresultStudentMapper.queryDistinctScalesInCity(cityid, xzxs, reportLookStudentFilterParam);
    }

    @Override
    public List<Scale> queryTeacherDistinctScales(Organization org) {
        return examresultTeacherMapper.queryTeacherDistinctScales(org);
    }

    @Override
    public List<ScaleInfo> queryTeacherDistinctScales(Organization org, String countyidOrSchoolid, int roleid,
            int scaletype, int scalesource, boolean isSonSchool) {
        return examresultTeacherMapper.getTeacherDistinctScales(org, countyidOrSchoolid, roleid, scaletype, scalesource,
                isSonSchool);
    }

    @Override
    public int deleteStudentExamResult(long resultid) {
        int result;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            result = examdoStudentMapper.resetExamdo(resultid);
            result = examresultStudentMapper.deleteByPrimaryKey(resultid);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int deleteSelectedStudentExamResults(String[] resultids) {
        // TODO Auto-generated method stub
        int result;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            result = examdoStudentMapper.resetExamdoBatch(resultids);
            result = examresultStudentMapper.deleteBatch(resultids);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int deleteStudentMhExamResult(long resultid) {
        // TODO Auto-generated method stub
        int result;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            result = examdoStudentMapper.resetExamdoMh(resultid);
            result = examdoStudentMapper.resetExamdo(resultid);
            result = examresultMapper.deleteExamresultMh(resultid);
            result = examresultStudentMapper.deleteByPrimaryKey(resultid);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int deleteSelectedStudentMhExamResults(String[] resultids) {
        // TODO Auto-generated method stub
        int result;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            result = examdoStudentMapper.resetExamdoMhBatch(resultids);
            result = examdoStudentMapper.resetExamdoBatch(resultids);
            result = examresultMapper.deleteExamresultMhBatch(resultids);
            result = examresultStudentMapper.deleteBatch(resultids);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int resetStudentExamdo(long resultid) {
        // TODO Auto-generated method stub
        return examdoStudentMapper.resetExamdo(resultid);

    }

    @Override
    public int resetTeacherExamdo(long resultid) {
        // TODO Auto-generated method stub
        return examdoTeacherMapper.resetExamdo(resultid);
    }

    @Override
    public List<ExamresultTeacher> queryDistinctTeacherFromExamresultTeacher(int orgId) {
        // TODO Auto-generated method stub
        return examresultTeacherMapper.queryDistinctTeacherFromExamresultTeacher(orgId);
    }

    @Override
    public int deleteTeacherExamResult(long resultid) {
        int result;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            result = examdoTeacherMapper.resetExamdo(resultid);
            result = examresultTeacherMapper.deleteByPrimaryKey(resultid);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public int deleteSelectedTeacherExamResults(String[] resultids) {
        int result;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            result = examdoTeacherMapper.resetExamdoBatch(resultids);
            result = examresultTeacherMapper.deleteBatch(resultids);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            return -1;
        }
        return 1;
    }

    @Override
    public List<Organization> getSchoolsFromExamresultStudent(String countyid) {
        // TODO Auto-generated method stub
        return examresultStudentMapper.getSchoolsFromExamresultStudent(countyid);
    }

    @Override
    public List<District> selectStudentDistinctQuxian() {
        // TODO Auto-generated method stub
        return examresultStudentMapper.selectDistinctQuxianFromExamresultStudent();
    }

    @Override
    public List<District> selectTeacherDistinctQuxian() {
        // TODO Auto-generated method stub
        return examresultTeacherMapper.selectDistinctQuxianFromExamresultTeacher();
    }

    @Override
    public List<Organization> selectTeacherDistinctSubSchool() {
        // TODO Auto-generated method stub
        return examresultTeacherMapper.selectDistinctSubSchoolFromExamresultTeacher();
    }

    @Override
    public List<Role> queryDistinctRoleFromExamresultTeacher(Organization org) {
        // TODO Auto-generated method stub
        return examresultTeacherMapper.selectDistinctRoleFromExamresultTeacher(org);
    }

    @Override
    public List<Organization> getSchoolsFromExamresultTeacher(String countyid) {
        return examresultTeacherMapper.getSchoolsFromExamresultTeacher(countyid);
    }

    // 生成团体报告
    @Override
    public Map<Object, Object> getStudentGroupReportForSchool(long orgid, String njmc, String[] bjArray, String scaleId,
            String startDate, String endDate) {
        Map<String, Object> parmeters = new HashMap<String, Object>();
        parmeters.put("orgid", orgid);
        parmeters.put("njmc", njmc);
        parmeters.put("bjArray", bjArray);
        parmeters.put("scaleid", scaleId);
        parmeters.put("starttime", startDate);
        parmeters.put("endtime", endDate + " 23:59:59");

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
        parmeters.put("endtime", endDate + " 23:59:59");
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
        parmeters.put("endtime", endDate + " 23:59:59");
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
        parmeters.put("endtime", endDate + " 23:59:59");
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
    public Map<Object, Object> getTeacherGroupReportForSchool(long orgid, int roleid, String roleName, String scaleId,
            String startDate, String endDate) {
        Map<String, Object> parmeters = new HashMap<String, Object>();
        parmeters.put("orgid", orgid);
        parmeters.put("roleid", roleid);
        parmeters.put("rolename", roleName);
        parmeters.put("scaleid", scaleId);
        parmeters.put("starttime", startDate);
        parmeters.put("endtime", endDate + " 23:59:59");

        List<ExamResult> list = examResultDao.getTeaErForSchool(parmeters);
        TeacherGroupReportForSchool grStatic = new TeacherGroupReportForSchool(orgid, scaleId, roleid, roleName,
                startDate, endDate);
        for (ExamResult examResult : list) {
            grStatic.incExmresult(examResult);
        }
        Map<Object, Object> page = grStatic.complete(roleName);
        page.put("startDate", startDate);
        page.put("endDate", endDate);

        return page;
    }

    @Override
    public Map<Object, Object> getTeacherGroupReportForCounty(String countyid, String[] schoolnames, int roleid,
            String roleName, String scaleid, String startDate, String endDate) {
        Map<String, Object> parmeters = new HashMap<String, Object>();
        parmeters.put("countyid", countyid);
        parmeters.put("xxmcArray", schoolnames);
        parmeters.put("roleid", roleid);
        parmeters.put("rolename", roleName);
        parmeters.put("schoolnames", schoolnames);
        parmeters.put("scaleid", scaleid);
        parmeters.put("starttime", startDate);
        parmeters.put("endtime", endDate + " 23:59:59");
        List<ExamResult> list = examResultDao.getTeaErForCounty(parmeters);
        District district = districtService.selectByCode(countyid);
        TeacherGroupReportForCounty grStatic = new TeacherGroupReportForCounty(district.getName(), countyid, scaleid,
                roleid, roleName, schoolnames, startDate, endDate);
        for (ExamResult examResult : list) {
            grStatic.incExmresult(examResult);
        }
        Map<Object, Object> page = grStatic.complete();
        page.put("startDate", startDate);
        page.put("endDate", endDate);

        return page;
    }

    @Override
    public Map<Object, Object> getTeacherGroupReportForCity(String cityname, String[] qxArray, int roleid,
            String roleName, String scaleid, String startDate, String endDate) {
        Map<String, Object> parmeters = new HashMap<String, Object>();
        parmeters.put("qxArray", qxArray);
        parmeters.put("cityname", cityname);
        parmeters.put("roleid", roleid);
        parmeters.put("rolename", roleName);
        parmeters.put("scaleid", scaleid);
        parmeters.put("starttime", startDate);
        parmeters.put("endtime", endDate + " 23:59:59");
        List<ExamResult> list = examResultDao.getTeaErForCity(parmeters);
        // District district = districtService.selectByCode(countyid);
        TeacherGroupReportForCity grStatic = new TeacherGroupReportForCity(cityname, qxArray, scaleid, roleid, roleName,
                startDate, endDate);
        for (ExamResult examResult : list) {
            grStatic.incExmresult(examResult);
        }
        Map<Object, Object> page = grStatic.complete();
        page.put("startDate", startDate);
        page.put("endDate", endDate);

        return page;
    }

    @Override
    public Map<Object, Object> getTeacherGroupReportForSubSchool(String cityname, String[] zsxxorgids, int roleid,
            String roleName, String scaleid, String startDate, String endDate) {
        Map<String, Object> parmeters = new HashMap<String, Object>();
        parmeters.put("zzxxorgidArray", zsxxorgids);
        parmeters.put("roleid", roleid);
        parmeters.put("rolename", roleName);
        parmeters.put("scaleid", scaleid);
        parmeters.put("starttime", startDate);
        parmeters.put("endtime", endDate + " 23:59:59");

        List<ExamResult> list = examResultDao.getTeaErForCitySubSchool(parmeters);
        TeacherGroupReportForSubSchool grStatic = new TeacherGroupReportForSubSchool(cityname, zsxxorgids, scaleid,
                roleid, roleName, startDate, endDate);
        for (ExamResult examResult : list) {
            grStatic.incExmresult(examResult);
        }
        Map<Object, Object> page = grStatic.complete();
        page.put("startDate", startDate);
        page.put("endDate", endDate);

        return page;

    }

    @Override
    public List<Role> queryDistinctRoleFromExamresultTeacher(Organization org, String countyidOrSchoolid,
            boolean isSonSchool) {
        return examresultTeacherMapper.getDistinctRoleFromExamresultTeacher(org, countyidOrSchoolid, isSonSchool);
    }

    // 以下为答案导出功能
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
}
