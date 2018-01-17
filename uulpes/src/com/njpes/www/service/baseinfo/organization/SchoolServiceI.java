package com.njpes.www.service.baseinfo.organization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.ClassSchool;
import com.njpes.www.entity.baseinfo.organization.Grade;
import com.njpes.www.entity.baseinfo.organization.School;
import com.njpes.www.entity.baseinfo.organization.SchoolPsychicjob;
import com.njpes.www.entity.baseinfo.organization.SchoolPsychicteam;
import com.njpes.www.utils.PageParameter;

/**
 * 学校相关服务
 * 
 * @author 赵忠诚
 */
public interface SchoolServiceI {

    /**
     * 根据学校编码获取学段信息
     * 
     * @param schoolorgid
     *            组织机构表id
     * @return 学段列表
     * @author 赵忠诚
     */
    public List<XueDuan> getXueDuanInSchool(long schoolorgid);

    /**
     * 根据学校编码取得学校年级
     * 
     * @param schoolorgid
     *            组织机构表id
     * @return 返回年级实体的列表
     * @author 赵忠诚
     */
    public HashMap<XueDuan, List<Grade>> getGradesInSchool(long schoolorgid);

    /**
     * 返回指定学段的年级列表 针对第二个参数使用如果传入参数为int类型的1 2 3 ，请使用XueDuan.valueOf(xd)作为参数即可
     * 
     * @param schoolCode
     *            学校编码
     * @param xd
     *            学段 {@link com.njpes.www.entity.baseinfo.enums.XueDuan}
     * @return 返回年级实体的列表
     * @author 赵忠诚
     */
    public List<Grade> getGradeListInSchool(long schoolorgid);

    public List<Grade> getGradesInSchool(long schoolorgid, XueDuan xd);

    /**
     * 根据年级和学校信息取得班级信息 针对第二个参数使用如果传入参数为int类型的1 2 3 ，请使用XueDuan.valueOf(xd)作为参数即可
     * 
     * @param schoolCode
     *            学校编码 可为空
     * @param xd
     *            学段 {@link com.njpes.www.entity.baseinfo.enums.XueDuan} 可为空
     * @param flozen_flag,表示是否已经毕业，1为已经毕业，0为在校
     * @param grade
     *            gradecode表id编码 不可为空
     * @return 返回班级实体的列表
     * @author 赵忠诚
     */
    public List<ClassSchool> getClassByGradeInSchool(long schoolorgid, XueDuan xd, String gradeid, int flozen_flag);

    public List<ClassSchool> getClassByGradeIdInSchool(long schoolorgid, int gradeid, int flozen_flag);

    /**
     * 根据年级和学校信息取得班级信息
     * 
     * @param entity
     *            班级实体,flozen_flag是否已毕业
     * @return 返回班级实体的列表
     * @author 赵忠诚
     */
    public List<ClassSchool> getClassByGradeidInSchoolByPage(ClassSchool entity, PageParameter page, int flozen_flag);

    public List<ClassSchool> getClassByGradeidInSchool(ClassSchool entity, int flozen_flag);

    /**
     * 根据主键id获得学校的基本信息
     * 
     * @param id
     * @return School
     * @author 赵忠诚
     */
    public School getSchoolInfoByKeyid(long id);

    /**
     * 根据学校编号获得所有的班级
     * 
     * @param schoolCode
     * @author s
     * @return
     */
    public List<ClassSchool> getClassBySchool(long schoolorgid);

    /**
     * 根据组织机构id获取心理工作队伍信息
     * 
     * @param orgid
     *            组织机构代码 为计算机识别码，即为Organization的主键id
     * @return {@link com.njpes.www.entity.baseinfo.organization.SchoolPsychicteam}
     * @author 赵忠诚
     */
    public List<SchoolPsychicteam> getTeamsByOrgid(Long orgid);

    /**
     * 根据组织机构id获取心理工作内容
     * 
     * @param orgid
     *            组织机构代码 为计算机识别码，即为Organization的主键id
     * @return {@link com.njpes.www.entity.baseinfo.organization.SchoolPsychicjob}
     * @author 赵忠诚
     */
    public List<SchoolPsychicjob> getJobsByOrgid(Long orgid);

    /**
     * 更新
     */
    public int update(School school);

    public int insert(School school);

    // 根据教委获得所有直属学校
    public List<School> getSchoolsAccordingEducommission(int orgid);

    // 根据组织机构的id获取组织机构下的所有学校
    public List<School> getSchoolsAccordingOrgId(String orgId);

    // 根据乡镇获得所有的学校
    public List<School> getSchoolAccordingTownIds(List<String> ids);

    // 根据区县获得所有的学校
    public List<School> getSchoolAccordingCountyIds(List<String> ids);

    // 根据城市获得所有的学校
    public List<School> getSchoolAccordingCityIds(List<String> ids);

    /**
     * 根据id获得一个学校里的所有年级
     * 
     * @author s
     * @param id
     * @return
     */
    public School getSchoolInfoByOrgId(long id);

    public School selectByPrimaryKey(long id);

    public int delByKeyid(Long keyid);

    public int delByOrgid(Long orgid);

    public int delByEntity(School school);

    public List<School> selectSubSchoolsInWebQueryByPage(School school, PageParameter page);

    // 获得区县下的所有学校（首页直通车使用)
    public List<Map> getDirectTrainForSchools(String code);

    public List<Grade> getGradeinSchools(List<String> schools);

    public List<String> getGraduateYear(long orgid);

    // 根据毕业年份获取毕业班列表
    public List<ClassSchool> getGraduatedClassByYear(long orgid, String graduateyear);
}
