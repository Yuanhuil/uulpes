package com.njpes.www.service.scaletoollib;

import java.util.List;
import java.util.Map;

import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.ExamresultStudent;
import com.njpes.www.entity.scaletoollib.ExamresultStudentWithBackGround;
import com.njpes.www.entity.scaletoollib.ExamresultTeacher;
import com.njpes.www.entity.scaletoollib.ReportLookStudentFilterParam;
import com.njpes.www.entity.scaletoollib.ReportLookTeacherFilterParam;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.utils.PageParameter;

import edutec.scale.model.Scale;

public interface ReportLookService {

    public List<ExamresultStudentWithBackGround> getStudentResultByPage(
            ReportLookStudentFilterParam reportLookStudentFilterParam, PageParameter page);

    public List<ExamresultStudent> getStuPersonalReportByPage(ReportLookStudentFilterParam reportLookStudentFilterParam,
            PageParameter page);

    public List<ExamresultStudent> getStuPersonalCommentsByPage(
            ReportLookStudentFilterParam reportLookStudentFilterParam, PageParameter page);

    public List<ExamresultStudentWithBackGround> getStuComplianceReportByPage(
            ReportLookStudentFilterParam reportLookStudentFilterParam, PageParameter page);

    public List<ExamresultStudentWithBackGround> getStuTeamReportByPage(
            ReportLookStudentFilterParam reportLookStudentFilterParam, PageParameter page);

    public List<ExamresultTeacher> getTeacherResultByPage(ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            PageParameter page);

    public List<ExamresultTeacher> getTeacherPersonalReportByPage(
            ReportLookTeacherFilterParam reportLookTeacherFilterParam, PageParameter page);

    public List<ExamresultTeacher> getTeaComplianceReportByPage(
            ReportLookTeacherFilterParam reportLookTeacherFilterParam, PageParameter page);

    public List<ExamresultTeacher> getTeaTeamReportsByPage(ReportLookTeacherFilterParam reportLookTeacherFilterParam,
            PageParameter page);

    public List<District> selectStudentDistinctQuxian();

    public List<District> selectTeacherDistinctQuxian();

    public List<Organization> selectTeacherDistinctSubSchool();

    public List<ExamresultStudent> queryDistinctXDFromExamresultStudent(int orgId);

    public String queryDistinctNJFromExamresultStudent(Organization org, int xd, boolean isSonSchool);

    public String queryDistinctNJByCountyidOrSchoolid(Organization org, String id, int xd, boolean isSonSchool);

    public String getEduScaleInfoFromExamdoStudent(Organization org, String countyidOrSchoolid, int xd, int nj,
            boolean isSonSchool);

    public String getEduScaleInfoFromExamdoStudent(Organization org, String countyidOrSchoolid, int xd, int nj,
            int scaletype, int scalesource, boolean isSonSchool);

    // public String getEduScaleInfoByCountyidOrSchoolid(Organization org,String
    // countyidOrSchoolid,int xd,int nj,boolean isSonSchool);
    public String queryDistinctBJFromExamresultStudent(int orgId, int xd, int nj);

    public List<ExamresultStudent> queryDistinctNJFromExamresultStudentAccordingOrgId(int orgId);

    public List<Role> queryDistinctRoleFromExamresultTeacher(Organization org, String countyidOrSchoolid,
            boolean isSonSchool);

    public List<Role> queryDistinctRoleFromExamresultTeacher(Organization org);

    public List<ExamresultTeacher> queryDistinctTeacherFromExamresultTeacher(int orgId);

    public List<Organization> getSchoolsFromExamresultStudent(String countyid);

    public List<Organization> getSchoolsFromExamresultTeacher(String countyid);

    public String queryDistinctBJFromExamresultStudentByNJ(int orgId, String njmc);

    public List<Scale> queryDistinctScales(Organization org);

    public List<Scale> queryDistinctScalesInSchool(Organization org,
            ReportLookStudentFilterParam reportLookStudentFilterParam);

    public List<Scale> queryDistinceScalesInCounty(String countyid,
            ReportLookStudentFilterParam reportLookStudentFilterParam);

    public List<Scale> queryDistinctScalesInCity(String cityid, String xzxs,
            ReportLookStudentFilterParam reportLookStudentFilterParam);

    public List<Scale> queryTeacherDistinctScales(Organization org);

    public List<ScaleInfo> queryTeacherDistinctScales(Organization org, String countyidOrSchoolid, int roleid,
            int scaletype, int scalesource, boolean isSonSchool);

    public int deleteStudentExamResult(long resultid);

    public int deleteSelectedStudentExamResults(String[] resultids);

    public int deleteStudentMhExamResult(long resultid);

    public int deleteSelectedStudentMhExamResults(String[] resultids);

    public int deleteTeacherExamResult(long resultid);

    public int deleteSelectedTeacherExamResults(String[] resultids);

    public int resetStudentExamdo(long resultid);

    public int resetTeacherExamdo(long resultid);

    // 生成学生团体报告
    public Map<Object, Object> getStudentGroupReportForSchool(long orgid, String njmc, String[] bjArray, String scaleId,
            String startDate, String endDate);

    public Map<Object, Object> getStudentGroupReportForCounty(String countyid, String[] schoolnames, String njmc,
            String scaleid, String startDate, String endDate);

    public Map<Object, Object> getStudentGroupReportForCity(String cityname, String[] qxArray, String njmc,
            String scaleid, String startDate, String endDate);

    public Map<Object, Object> getStudentGroupReportForSubSchool(String cityname, String[] schoolnames, String njmc,
            String scaleid, String startDate, String endDate);

    // 生成教师团体报告
    public Map<Object, Object> getTeacherGroupReportForSchool(long orgid, int roleid, String roleName, String scaleId,
            String startDate, String endDate);

    public Map<Object, Object> getTeacherGroupReportForCounty(String countyid, String[] schoolnames, int roleid,
            String roleName, String scaleid, String startDate, String endDate);

    public Map<Object, Object> getTeacherGroupReportForCity(String cityname, String[] qxArray, int roleid,
            String roleName, String scaleid, String startDate, String endDate);

    public Map<Object, Object> getTeacherGroupReportForSubSchool(String cityname, String[] schoolnames, int roleid,
            String roleName, String scaleid, String startDate, String endDate);

    // 答案导出
    public Map<String, Object> getStudentAnswer(long resultid);

    public Map<String, Object> getTeacherAnswer(long resultid);

    public Map<String, Object> getThreeAngleAnswer(long resultid, int typeflag);
}
