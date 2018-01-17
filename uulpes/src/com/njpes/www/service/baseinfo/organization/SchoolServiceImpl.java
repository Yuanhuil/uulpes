package com.njpes.www.service.baseinfo.organization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.baseinfo.ClassSchoolMapper;
import com.njpes.www.dao.baseinfo.GradeMapper;
import com.njpes.www.dao.baseinfo.GraduatingClassMapper;
import com.njpes.www.dao.baseinfo.SchoolMapper;
import com.njpes.www.dao.baseinfo.SchoolPsychicjobMapper;
import com.njpes.www.dao.baseinfo.SchoolPsychicteamMapper;
import com.njpes.www.dao.baseinfo.StudentMapper;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.entity.baseinfo.organization.SchoolPsychicjob;
import com.njpes.www.entity.baseinfo.organization.SchoolPsychicteam;
import com.njpes.www.entity.workschedule.JobAttachment;
import com.njpes.www.service.workschedule.JobAttachmentServiceI;
import com.njpes.www.utils.PageParameter;

/**
 * 学校相关服务
 * 
 * @author 赵忠诚
 */
@Service("schoolService")
public class SchoolServiceImpl implements SchoolServiceI {
    @Autowired
    private OrganizationServiceI organizationService;

    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private SchoolPsychicjobMapper schoolPsychicjobMapper;

    @Autowired
    private SchoolPsychicteamMapper schoolPsychicteamMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ClassSchoolMapper classSchoolMapper;

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private GraduatingClassMapper graduatingMapper;

    @Autowired
    private JobAttachmentServiceI jobAttachmentService;

    @Override
    public HashMap<XueDuan, List<Grade>> getGradesInSchool(long schoolorgid) {
        HashMap<XueDuan, List<Grade>> xd_gradeMap = new HashMap<XueDuan, List<Grade>>();
        School s = schoolMapper.selectSchoolInfoBySchoolOrgId(schoolorgid);
        HashMap<XueDuan, BigDecimal> xz = s.getXZEveryXueDuan();
        Iterator<XueDuan> iterator = xz.keySet().iterator();
        while (iterator.hasNext()) {
            XueDuan x = iterator.next();
            BigDecimal xz_big = xz.get(x);
            List<Grade> grades = gradeMapper.getGradeBySchoolGroupXZ(x.value(), xz_big.intValue());
            xd_gradeMap.put(x, grades);
        }
        return xd_gradeMap;
    }

    @Override
    public List<Grade> getGradeListInSchool(long schoolorgid) {
        List<Grade> gradeList = new ArrayList<Grade>();
        School s = schoolMapper.selectSchoolInfoBySchoolOrgId(schoolorgid);
        HashMap<XueDuan, BigDecimal> xz = s.getXZEveryXueDuan();
        Iterator<XueDuan> iterator = xz.keySet().iterator();
        while (iterator.hasNext()) {
            XueDuan x = iterator.next();
            BigDecimal xz_big = xz.get(x);
            List<Grade> grades = gradeMapper.getGradeBySchoolGroupXZ(x.value(), xz_big.intValue());
            for (int i = 0; i < grades.size(); i++) {
                Grade g = grades.get(i);
                if (g == null)
                    break;
                g.setNjmc(g.getNjmc());
                gradeList.add(g);
            }
        }
        return gradeList;
    }

    @Override
    public List<Grade> getGradesInSchool(long schoolorgid, XueDuan xd) {
        School s = schoolMapper.selectSchoolInfoBySchoolOrgId(schoolorgid);
        HashMap<XueDuan, BigDecimal> xz = s.getXZEveryXueDuan();
        int xd_xz = xz.get(xd).intValue();
        return gradeMapper.getGradeBySchoolGroupXZ(xd.value(), xd_xz);
    }

    @Override
    public List<ClassSchool> getClassByGradeInSchool(long schoolorgid, XueDuan xd, String gradeid, int flozen_flag) {
        return classSchoolMapper.selectClassByGradeInSchool(schoolorgid, xd.getId(), gradeid, flozen_flag);
    }

    public List<ClassSchool> getClassByGradeIdInSchool(long schoolorgid, int gradeid, int flozen_flag) {
        return classSchoolMapper.selectClassByGradeIdInSchool(schoolorgid, gradeid, flozen_flag);
    }

    @Override
    public List<XueDuan> getXueDuanInSchool(long schoolorgid) {
        School s = schoolMapper.selectSchoolInfoBySchoolOrgId(schoolorgid);
        if (s == null)
            return null;
        return s.getXueDuan();
    }

    @Override
    public List<School> getSchoolsAccordingEducommission(int orgid) {
        List<School> sl = schoolMapper.getSchoolsAccordingEducommission(orgid);
        return sl;
    }

    @Override
    public List<School> getSchoolAccordingCountyIds(List<String> ids) {
        /*
         * String instr=""; for(String id:ids){ instr=instr+id+"','"; } instr =
         * instr.substring(0, instr.length()-3);
         */
        if (ids.size() > 0) {
            return schoolMapper.getSchoolsAccordingCountyIds(ids);
        } else {
            return new ArrayList<School>();
        }
    }

    @Override
    public List<School> getSchoolAccordingCityIds(List<String> ids) {
        /*
         * String instr=""; for(String id:ids){ instr=instr+id+","; }
         */
        if (ids.size() > 0) {
            // instr = instr.substring(0, instr.length()-1);
            return schoolMapper.getSchoolAccordingCityIds(ids);

        } else {
            return new ArrayList<School>();
        }
    }

    @Override
    public List<School> getSchoolAccordingTownIds(List<String> ids) {
        /*
         * String instr=""; for(String id:ids){ instr=instr+id+","; } instr =
         * instr.substring(0, instr.length()-1);
         */
        if (ids.size() > 0) {
            return schoolMapper.getSchoolAccordingTownIds(ids);
        } else {
            return new ArrayList<School>();
        }
    }

    @Override
    public List<School> getSchoolsAccordingOrgId(String orgId) {
        return this.schoolMapper.getSchoolsAccordingOrgId(Integer.parseInt(orgId));
    }

    @Override
    public List<ClassSchool> getClassBySchool(long schoolorgid) {
        // TODO Auto-generated method stub
        return classSchoolMapper.selectClassBySchool(schoolorgid);
    }

    @Override
    public School getSchoolInfoByKeyid(long id) {
        if (id <= 0)
            return null;
        return schoolMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SchoolPsychicteam> getTeamsByOrgid(Long orgid) {
        return schoolPsychicteamMapper.getTeamsByOrgid(orgid);
    }

    @Override
    public List<SchoolPsychicjob> getJobsByOrgid(Long orgid) {
        return schoolPsychicjobMapper.getJobsByOrgid(orgid);
    }

    @Override
    public int update(School school) {
        Organization org = school.getOrg();
        org.setId(school.getOrgid());
        org.setName(school.getXxmc());
        org.setCode(school.getXxdm());
        String attachment_uuid = org.getImageurl();
        if (StringUtils.isNotBlank(attachment_uuid)) {
            JobAttachment att = jobAttachmentService.selectByUUid(attachment_uuid);
            if (att != null) {
                String imageurl = att.getSavePath();
                org.setImageurl(imageurl);
            }
        }
        organizationService.updateOrganization(org);
        // return schoolMapper.updateByPrimaryKeyWithBLOBs(school);
        return schoolMapper.updateByPrimaryKeySelective(school);
    }

    @Override
    public List<ClassSchool> getClassByGradeidInSchoolByPage(ClassSchool entity, PageParameter page, int flozen_flag) {
        return classSchoolMapper.selectClassesByPage(entity, page, flozen_flag);
    }

    @Override
    public List<ClassSchool> getClassByGradeidInSchool(ClassSchool entity, int flozen_flag) {
        return classSchoolMapper.selectAllClasses(entity, flozen_flag);
    }

    @Override
    public School getSchoolInfoByOrgId(long id) {
        return schoolMapper.selectSchoolInfoByOrgId(id);
    }

    @Override
    public School selectByPrimaryKey(long id) {
        return schoolMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(School school) {
        Organization org = school.getOrg();
        org.setCode(school.getXxdm());
        org.setName(school.getXxmc());
        org = organizationService.insertOrganization(org);
        school.setOrgid(org.getId());
        return schoolMapper.insertSelective(school);
    }

    @Override
    public int delByKeyid(Long keyid) {
        return schoolMapper.deleteByPrimaryKey(keyid);
    }

    @Override
    public int delByOrgid(Long orgid) {
        return schoolMapper.deleteByOrgid(orgid);
    }

    @Override
    public List<School> selectSubSchoolsInWebQueryByPage(School school, PageParameter page) {
        return schoolMapper.selectSubSchoolsInWebQueryByPage(school, page);
    }

    @Override
    public int delByEntity(School school) {
        organizationService.delOrg(school.getOrg());
        return delByKeyid(school.getId());
    }

    @Override
    public List<Map> getDirectTrainForSchools(String code) {
        return schoolMapper.getDirectTrainForSchools(code);
    }

    @Override
    public List<Grade> getGradeinSchools(List<String> schools) {
        // TODO Auto-generated method stub
        if (schools != null && schools.size() > 0) {
            Map<String, BigDecimal> xzs = gradeMapper.getMaxXZinSchools(schools);
            String xxxz = null;
            String czxz = null;
            String gzxz = null;
            if (xzs.containsKey("xxxz")) {
                String xz = Integer.toString(((BigDecimal) xzs.get("xxxz")).intValue());
                if (xz.equals("6"))
                    xxxz = "9";
                else if (xz.equals("5"))
                    xxxz = "7";
                else
                    xxxz = Integer.toString(((BigDecimal) xzs.get("xxxz")).intValue());
            }
            if (xzs.containsKey("czxz"))
                czxz = Integer.toString(((BigDecimal) xzs.get("czxz")).intValue());
            if (xzs.containsKey("gzxz"))
                gzxz = Integer.toString(((BigDecimal) xzs.get("gzxz")).intValue());
            if (StringUtils.isEmpty(xxxz) && StringUtils.isEmpty(czxz) && StringUtils.isEmpty(gzxz))
                return new ArrayList<Grade>();
            else
                return gradeMapper.getGradeinSchools(xxxz, czxz, gzxz);
        } else {
            return new ArrayList<Grade>();
        }
    }

    @Override
    public List<String> getGraduateYear(long orgid) {
        return graduatingMapper.getGraduateYear(orgid);
    }

    @Override
    public List<ClassSchool> getGraduatedClassByYear(long orgid, String graduateyear) {
        return graduatingMapper.getGraduatedClass(orgid, graduateyear);
    }

}
