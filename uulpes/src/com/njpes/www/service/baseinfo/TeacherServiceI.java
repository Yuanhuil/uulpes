package com.njpes.www.service.baseinfo;

import java.util.List;

import com.njpes.www.entity.baseinfo.AccountDetail;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherQueryParam;
import com.njpes.www.entity.baseinfo.TeacherWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface TeacherServiceI {

    int deleteByPrimaryKey(Teacher teacher);

    int deleteSelected(List<Teacher> teacherList);

    /**
     * 插入前端传入参数到数据库 需要操作多张表 account teacher teacher_ext auth表 需要使用事物处理
     *
     * @param teacher
     *            前端传入的参数
     * @param orgid
     *            组织机构编码
     * @param roleids
     *            改用户的角色列表
     * @author 赵忠诚
     */
    int insert(TeacherQueryParam teacher, long orgid, long[] roleids);

    /**
     * 更新前端传入参数到数据库 需要操作多张表 account teacher teacher_ext auth表 需要使用事物处理
     *
     * @param teacher
     *            前端传入的参数
     * @param roleids
     *            改用户的角色列表
     * @author 赵忠诚
     */
    int update(TeacherQueryParam teacher, long[] roleids);

    TeacherQueryParam selectTeacherInfoById(long id);

    /**
     * 插入教师信息
     *
     * @author 赵忠诚
     */
    int insert(TeacherWithBLOBs teacher);

    /**
     * 更新教师信息
     *
     * @author 赵忠诚
     */
    int update(TeacherWithBLOBs teacher);

    public int updateBjxx(long id, String bjxx);

    Teacher selectByPrimaryKey(Long id);

    Teacher getTeacherByAccountId(Long id);

    int updateByPrimaryKey(Teacher record);

    AccountDetail selectByAccountId(int accountId);

    void updateByAccountId(AccountDetail accountDetail);

    /**
     * 分页获取老师信息
     *
     * @param
     * @author 赵忠诚
     */
    public List<Teacher> getTeachersByPage(TeacherQueryParam teacher, long schoolorgid, PageParameter page);

    public List<Teacher> getAdminTeachersByPage(TeacherQueryParam teacher, long schoolorgid, long parentorgid,
            PageParameter page);

    /**
     * 分页获取老师信息
     *
     * @param teacher
     *            查询条件
     * @param schoolCode
     *            学校代码
     * @param isPage
     *            是否分页
     * @param page
     *            分页实体，ispage为false时，page写null
     * @param excludes
     *            除外的id列表
     * @author 赵忠诚
     */
    public List<Teacher> getTeachers(TeacherQueryParam teacher, long schoolorgid, boolean isPage, PageParameter page,
            List<Long> excludes);

    /**
     * 根据学校代码以及类型获得老师信息
     *
     * @param orgid
     *            学校long值的唯一编码，为数据库的自增字段
     * @param roleFlag
     *            0为查询全部,>0的值为按照需求查询
     * @author 赵忠诚
     */
    public List<Teacher> getTeacherInSchool(long orgid, int roleFlag);

    // 获得心理老师或心理咨询师
    public List<Teacher> getPsychologyTeacherInSchool(long orgid);

    /**
     * 根据给定制定的id查询老师信息
     *
     * @param ids
     * @return List<StudentWithBLOBs>
     * @author 赵忠诚
     */
    public List<Teacher> getTeachersByIds(List<Long> ids);

}
