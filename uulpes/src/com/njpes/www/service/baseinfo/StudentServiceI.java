package com.njpes.www.service.baseinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.baseinfo.AccountDetail;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface StudentServiceI {

    int deleteByPrimaryKey(Long id);

    int deleteSelected(List<Student> studentList);

    int changeClass(List<Student> studentList, int newclassid);

    int insert(StudentWithBLOBs student);

    int insertSelective(Student record);

    /**
     * 获取学生基本信息
     * 
     * @param id
     *            学生唯一编码
     * @return Student
     * @author 赵忠诚
     */
    Student selectByPrimaryKey(Long id);

    Student getStudentByAccountId(Long id);

    Student getStudentBySfzjh(String sfzjh);

    int updateByPrimaryKeySelective(Student record);

    public int deleteByPrimaryKey(Student student);

    public int update(StudentWithBLOBs student);

    int updateByPrimaryKey(Student record);

    AccountDetail selectByAccountId(int accountId);

    void updateByAccountId(AccountDetail accountDetail);

    StudentWithBLOBs selectStudentInfoById(long id);

    /**
     * 获取班级学生信息
     * 
     * @param stu
     *            学生实体类，查询条件
     * @param school
     *            学校编码
     * @param page
     *            分页参数
     * @param flozenflag
     *            学生是否已毕业：0在校，1已毕业
     * @return List<StudentWithBLOBs>
     * @author 赵忠诚
     */
    List<StudentWithBLOBs> getStudentsByPage(Student stu, long schoolorgid, PageParameter page, int flozenflag);

    /**
     * 获取班级学生信息
     * 
     * @param stu
     *            学生实体类，查询条件
     * @param school
     *            学校编码
     * @param page
     *            分页参数
     * @param exculdes
     *            不包含的学生id
     * @param flozenflag
     *            学生是否已毕业：0在校，1已毕业
     * @return List<StudentWithBLOBs>
     * @author 赵忠诚
     */
    List<StudentWithBLOBs> getStudentsByPage(Student stu, long schoolorgid, PageParameter page, List<Long> excludesids,
            int flozenflag);

    /**
     * 根据查询条件获取班级学生信息
     * 
     * @param queryParam
     *            学生实体类，查询条件
     * @param schoolCode
     *            学校编码
     * @param flozenflag
     *            学生是否已毕业：0在校，1已毕业
     * @return List<StudentWithBLOBs>
     * @author 赵忠诚
     */
    List<StudentWithBLOBs> getAllStudentsInClass(Student queryParam, long schoolorgid, int flozenflag);

    List<StudentWithBLOBs> getAllStudentsInCounty(Student queryParam, String countyid, int flozenflag);

    List<StudentWithBLOBs> getAllStudentsInCity(Student queryParam, String cityid, String xzxs, int flozenflag);

    /**
     * 查询学生信息
     * 
     * @param schoolCode
     *            学校代码
     * @param stu
     *            查询学生信息条件
     * @param isPage
     *            是否需要分页
     * @page 分页类实体，如果isPage 为false ,则此变量为null
     * @param exculdes
     *            不包含的学生id
     * @param flozenflag
     *            学生是否已毕业：0在校，1已毕业
     * @return List<StudentWithBLOBs>
     * @author 赵忠诚
     */
    public List<StudentWithBLOBs> getStudents(long schoolorgid, Student stu, boolean isPage, PageParameter page,
            List<Long> excludesids, int flozenflag);

    /**
     * 根据给定制定的id查询学生信息
     * 
     * @param id
     * @return List<StudentWithBLOBs>
     * @author 赵忠诚
     */
    public List<StudentWithBLOBs> getStudentsByIds(List<Long> ids);

    /**
     * 根据给定的id查询学生信息
     * 
     * @param id
     * @return StudentWithBLOBs
     * @author 赵忠诚
     */
    public StudentWithBLOBs getStudentById(long id);

    public StudentWithBLOBs getStudentWithBlobByAccountId(long id);

    public StudentWithBLOBs selectStudentWithBlobInfoById(long id);

    int updateByPrimaryKeyWithBLOBs(StudentWithBLOBs studentWithBLOBs);

    /**
     * @param orgid
     *            组织机构编码，注意不是学校编码
     * @param gradecodeid
     *            gradecode 表id
     * @return 为常模使用的id
     * @author 赵忠诚
     */
    public int getRealGradeId(Long orgid, int gradecodeid);

    public int updateBjxx(long id, String bjxx);

    public int updateBj(long id, String bjid);

    public boolean getStudentsXjh(long schoolorgid,String xjh);

    public List<Student> getStudentRecordAll(Student stu, long schoolorgid,int flozenflag);

	int getStudentCountByClassid(Long id);
}
