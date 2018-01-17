package com.njpes.www.service.assessmentcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.assessmentcenter.ExamdoMapper;
import com.njpes.www.dao.assessmentcenter.ExamdoMentalHealthMapper;
import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
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

@Service("examdoService")
public class ExamdoServiceImpl implements ExamdoServiceI {

    @Autowired
    private ExamdoMapper examMapper;
    @Autowired
    private ExamdoMentalHealthMapper examMhMapper;
    @Autowired
    ExamresultStudentMapper examresultStudentMapper;

    @Override
    public List<Examdo> getExamdoOfAccount(Account account) {
        List examdos = new ArrayList();
        Map para = new HashMap();
        if (account.isStudent()) {
            para.put("table", "examdo_student");
        } else if (account.isTeacher()) {
            para.put("table", "examdo_teacher");
        } else {
            return null;
        }
        para.put("userId", account.getId());
        examdos = examMapper.selectByUserId(para);
        return examdos;
    }

    @Override
    public List<?> queryStudentExamdo(DataManageFilterParam dmgFilterParam) {
        return examMapper.queryStudentExamdo(dmgFilterParam);
    }

    @Override
    public List<?> queryTeacherExamdo(DataManageFilterParam dmgFilterParam) {
        return examMapper.queryTeacherExamdo(dmgFilterParam);
    }

    @Override
    public List<?> queryStudentExamdo(PageParameter page, DataManageFilterParam dmgFilterParam) {
        return examMapper.queryStudentExamdoByPage(page, dmgFilterParam);

    }

    @Override
    public List<?> queryExamdoMhForTeacher(PageParameter page, DataManageFilterParam dmgFilterParam) {
        return examMhMapper.queryExamdoMhForTeacherByPage(page, dmgFilterParam);

    }

    @Override
    public List<?> queryExamdoMhForParent(PageParameter page, DataManageFilterParam dmgFilterParam) {
        return examMhMapper.queryExamdoMhForParentByPage(page, dmgFilterParam);

    }

    @Override
    public List<?> queryTeacherExamdo(PageParameter page, DataManageFilterParam dmgFilterParam) {
        return examMapper.queryTeacherExamdoByPage(page, dmgFilterParam);

    }

    @Override
    public ExamdoStudent getStudentExamdoByResultid(long resultid) {
        // TODO Auto-generated method stub
        ExamdoStudent examdoStudent = examMapper.selectStudentExamdoByResultId(resultid);
        return examdoStudent;
    }

    @Override
    public ExamdoTeacher getTeacherExamdoByResultid(long resultid) {
        return null;
    }

    @Override
    public List<Scale> queryStudentDistinctScales(Organization org) {
        return examMapper.queryStudentDistinctScales(org);
    }

    @Override
    public List<Scale> queryTeacherDistinctScales(Organization org) {
        return examMapper.queryTeacherDistinctScales(org);
    }

    @Override
    public List<ScaleInfo> selectDistinctScaleAcorStype(Organization org, String gradeid, String bjid, String roleid,
            int typeid) {
        if (roleid == null)
            return examMapper.selectDistinctScaleAcorStype(org, gradeid, bjid, typeid);
        else
            return examMapper.selectTchDistinctScaleAcorStype(org, roleid, typeid);
    }

    @Override
    public List<ScaleInfo> selectDistinctScaleBySource(Organization org, String gradeid, String bjid, String roleid,
            int sourceid) {
        if (roleid == null)
            return examMapper.selectDistinctScaleBySource(org, gradeid, bjid, sourceid);
        else
            return examMapper.selectTchDistinctScaleBySource(org, roleid, sourceid);
    }

    @Override
    public List<ScaleInfo> selectDistinctScales(Organization org, String gradeid, String bjid, String roleid,
            int typeid, int sourceid) {
        if (roleid == null)
            return examMapper.selectDistinctScales(org, gradeid, bjid, typeid, sourceid);
        else
            return examMapper.selectTchDistinctScales(org, roleid, typeid, sourceid);
    }

    @Override
    public List<ScaleInfo> selectDistinctScaleByGradeId(Organization org, String gradeid, String bjid) {
        return examMapper.selectDistinctScaleByGradeId(org, gradeid, bjid);
    }

    @Override
    public List<ScaleInfo> selectDistinctScaleByRoleId(Organization org, String roleid) {
        return examMapper.selectDistinctScaleByRoleId(org, roleid);
    }

    @Override
    public List<District> selectStudentDistinctQuxian() {
        return examMapper.selectStudentDistinctQuxian();
    }

    @Override
    public List<Organization> selectStudentDistinctSonSchool() {
        return examMapper.selectStudentDistinctSonSchool();
    }

    @Override
    public List<District> selectTeacherDistinctQuxian() {
        return examMapper.selectTeacherDistinctQuxian();
    }

    @Override
    public List<Organization> selectTeacherDistinctSonSchool() {
        return examMapper.selectTeacherDistinctSonSchool();
    }

    @Override
    public List<Grade> getStudentDistinctGradeInCounty(String countyid) {
        return examMapper.getStudentDistinctGradeInCounty(countyid);
    }

    @Override
    public List<ScaleInfo> getStudentDistinctScaleInCounty(String countyid) {
        return examMapper.getStudentDistinctScaleInCounty(countyid);
    }

    @Override
    public List<Role> getTeacherDistinctRoleInCounty(String countyid) {
        // TODO Auto-generated method stub
        return examMapper.getTeacherDistinctRoleInCounty(countyid);
    }

    @Override
    public List<ScaleInfo> getTeacherDistinctScaleInCounty(String countyid) {
        return examMapper.getTeacherDistinctScaleInCounty(countyid);
    }

    @Override
    public List<Grade> getStudentDistinctGradeInSchool(String schoolid) {
        return examMapper.getStudentDistinctGradeInSchool(schoolid);
    }

    @Override
    public List<ScaleInfo> getStudentDistinctScaleInSchool(String schoolid, String gradeid) {
        return examMapper.getStudentDistinctScaleInSchool(schoolid, gradeid);
    }

    @Override
    public List<Role> getTeacherDistinctRoleInSchool(String schoolid) {
        return examMapper.getTeacherDistinctRoleInSchool(schoolid);
    }

    @Override
    public List<ScaleInfo> getTeacherDistinctScaleInSchool(String schoolid) {
        return examMapper.getTeacherDistinctScaleInSchool(schoolid);
    }

    @Override
    public List<ScaleInfo> getStudentDistinctScaleInCountyByGrade(String countyid, String gradeid) {
        return examMapper.getStudentDistinctScaleInCountyByGrade(countyid, gradeid);
    }

    @Override
    public List queryBJFromExamresultStudentByNj(int orgId, int xd, int nj) {
        return examresultStudentMapper.queryDistinctBJFromExamresultStudent(orgId, xd, nj);
    }

    @Override
    public List queryBJFromExamresultStudentByGrade(int orgId, int xd, int grade) {
        return examresultStudentMapper.queryBJFromExamresultStudentByGrade(orgId, xd, grade);
    }

    @Override
    public List<ScaleInfo> getStudentDistinctScaleInSchool(Map param) {
        return examresultStudentMapper.queryScaleFromExamresultStudentInSchool(param);
    }

    @Override
    public List<String> querySchoolsFromExamresult(String countyid) {
        return examresultStudentMapper.querySchoolsFromExamresult(countyid);
    }

	@Override
	public List<Examdo> getSchoolAllDataById(DataManageFilterParam dmgFilterParam) {
		List<Examdo> examList = examMapper.queryStudentExamdo(dmgFilterParam);
		return examList;
	}

	@Override
	public List<ExamresultHuman> getStudentResult(StatScope scope) {
		List<ExamresultHuman> studentResult = examresultStudentMapper.getStudentResult(scope);
		return studentResult;
	}

}
