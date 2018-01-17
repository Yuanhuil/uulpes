package com.njpes.www.service.assessmentcenter;

import java.util.List;
import java.util.Map;

import com.njpes.www.entity.assessmentcenter.DataManageFilterParam;
import com.njpes.www.entity.assessmentcenter.Examdo;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.scaletoollib.ExamdoStudent;
import com.njpes.www.entity.scaletoollib.ExamdoTeacher;
import com.njpes.www.entity.scaletoollib.ExamresultHuman;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.entity.scaletoollib.StatScope;
import com.njpes.www.utils.PageParameter;

import edutec.scale.model.Scale;

public interface ExamdoServiceI {

    public List<Examdo> getExamdoOfAccount(Account account);

    List<?> queryStudentExamdo(DataManageFilterParam dmgFilterParam);

    List<?> queryTeacherExamdo(DataManageFilterParam dmgFilterParam);

    List<?> queryStudentExamdo(PageParameter page, DataManageFilterParam dmgFilterParam);

    List<?> queryTeacherExamdo(PageParameter page, DataManageFilterParam dmgFilterParam);

    public List<?> queryExamdoMhForTeacher(PageParameter page, DataManageFilterParam dmgFilterParam);

    public List<?> queryExamdoMhForParent(PageParameter page, DataManageFilterParam dmgFilterParam);

    ExamdoStudent getStudentExamdoByResultid(long resultid);

    ExamdoTeacher getTeacherExamdoByResultid(long resultid);

    public List<Scale> queryStudentDistinctScales(Organization org);

    public List<Scale> queryTeacherDistinctScales(Organization org);

    public List<ScaleInfo> selectDistinctScaleAcorStype(Organization org, String gradeid, String bjids, String roleid,
            int typeid);

    public List<ScaleInfo> selectDistinctScaleBySource(Organization org, String gradeid, String bjid, String roleid,
            int sourceid);

    public List<ScaleInfo> selectDistinctScales(Organization org, String gradeid, String bjid, String roleid,
            int typeid, int sourceid);

    public List<ScaleInfo> selectDistinctScaleByGradeId(Organization org, String gradeid, String bjid);

    public List<ScaleInfo> selectDistinctScaleByRoleId(Organization org, String roleid);

    public List<District> selectStudentDistinctQuxian();

    public List<Organization> selectStudentDistinctSonSchool();

    public List<District> selectTeacherDistinctQuxian();

    public List<Organization> selectTeacherDistinctSonSchool();

    // 以下是数据导出页面所用的
    public List<Grade> getStudentDistinctGradeInCounty(String countyid);

    public List<Grade> getStudentDistinctGradeInSchool(String schoolid);

    public List<ScaleInfo> getStudentDistinctScaleInCounty(String countyid);

    public List<ScaleInfo> getStudentDistinctScaleInCountyByGrade(String countyid, String gradeid);

    public List<ScaleInfo> getStudentDistinctScaleInSchool(String schoolid, String gradeid);

    public List<ScaleInfo> getStudentDistinctScaleInSchool(Map param);

    public List<Role> getTeacherDistinctRoleInCounty(String countyid);

    public List<Role> getTeacherDistinctRoleInSchool(String schoolid);

    public List<ScaleInfo> getTeacherDistinctScaleInCounty(String countyid);

    public List<ScaleInfo> getTeacherDistinctScaleInSchool(String schoolid);

    public List queryBJFromExamresultStudentByNj(int orgId, int xd, int nj);// 根据学段、年级查询班级

    public List queryBJFromExamresultStudentByGrade(int orgId, int xd, int grade);// 根据学段、年级查询班级

    public List querySchoolsFromExamresult(String countyid);
    // public List queryCountyidsFromExamresult(String cityid,int typeflag);

	public List<Examdo> getSchoolAllDataById(DataManageFilterParam dmgFilterParam);

	public List<ExamresultHuman> getStudentResult(StatScope scope);

}
