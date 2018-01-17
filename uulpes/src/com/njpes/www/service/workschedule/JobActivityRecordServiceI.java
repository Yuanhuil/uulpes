package com.njpes.www.service.workschedule;

import java.util.List;

import com.njpes.www.entity.workschedule.JobActivityRecordPsyactWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsycourseWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordPsyresearchWithBLOBs;
import com.njpes.www.entity.workschedule.JobActivityRecordWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface JobActivityRecordServiceI {

    public static String resourceIdentity = "workschedule:activityrecord";
    public static String resourceIdentity_act = "workschedule:activityrecord:act";
    public static String resourceIdentity_course = "workschedule:activityrecord:course";
    public static String resourceIdentity_research = "workschedule:activityrecord:research";

    public List<JobActivityRecordWithBLOBs> findAllRecordInView(JobActivityRecordWithBLOBs params);

    public List<JobActivityRecordWithBLOBs> findRecordsInViewByPage(JobActivityRecordWithBLOBs params,
            PageParameter page);

    public List<JobActivityRecordWithBLOBs> findSchoolRecordsInViewByPage(JobActivityRecordWithBLOBs params,
            PageParameter page);

    /**
     * 更新工作概览字段为true或者false
     * 
     * @param tablename
     *            表名称
     * @param recordId
     *            记录id
     * @param fieldValue
     *            true或者false值
     * @return 返回更新个数
     * @author 赵忠诚
     */
    public int updateJobOverviewField(String tablename, long recordId, String fieldValue);

    public int updateVipEventField(String tablename, long recordId, String fieldValue);

    /**
     * 根据条件查询活动记录中得心理课
     * 
     * @param params
     *            查询条件
     * @param page
     *            分页信息
     * @return 返回查询结果列表
     * @author 赵忠诚
     */
    public List<JobActivityRecordPsycourseWithBLOBs> findPsycourseByPage(JobActivityRecordPsycourseWithBLOBs params,
            PageParameter page);

    /**
     * 根据条件查询活动记录中得心理活动
     * 
     * @param params
     *            查询条件
     * @param page
     *            分页信息
     * @return 返回查询结果列表
     * @author 赵忠诚
     */
    public List<JobActivityRecordPsyactWithBLOBs> findPsyactByPage(JobActivityRecordPsyactWithBLOBs params,
            PageParameter page);

    /**
     * 根据条件查询活动记录中得心理科研
     * 
     * @param params
     *            查询条件
     * @param page
     *            分页信息
     * @return 返回查询结果列表
     * @author 赵忠诚
     */
    public List<JobActivityRecordPsyresearchWithBLOBs> findPsyresearchByPage(
            JobActivityRecordPsyresearchWithBLOBs params, PageParameter page);

    /**
     * 插入数据到表中
     * 
     * @param record
     * @author 赵忠诚
     */
    public int insertPsycourse(JobActivityRecordPsycourseWithBLOBs record);

    /**
     * 插入数据到表中
     * 
     * @param record
     * @author 赵忠诚
     */
    public int insertPsyact(JobActivityRecordPsyactWithBLOBs record);

    /**
     * 插入数据到表中
     * 
     * @param record
     * @author 赵忠诚
     */
    public int insertPsyresearch(JobActivityRecordPsyresearchWithBLOBs record);

    /**
     * 根据实体参数查询实体
     * 
     * @param record
     * @author 赵忠诚
     */
    public JobActivityRecordPsycourseWithBLOBs findPsycourseById(JobActivityRecordPsycourseWithBLOBs record);

    /**
     * 根据实体参数查询实体
     * 
     * @param record
     * @author 赵忠诚
     */
    public JobActivityRecordPsyactWithBLOBs findPsyactById(JobActivityRecordPsyactWithBLOBs record);

    /**
     * 根据实体参数查询实体
     * 
     * @param record
     * @author 赵忠诚
     */
    public JobActivityRecordPsyresearchWithBLOBs findPsyresearchById(JobActivityRecordPsyresearchWithBLOBs record);

    /**
     * 删除记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int deletePsycourse(JobActivityRecordPsycourseWithBLOBs record);

    public int deletePsycourse(long record);

    /**
     * 删除记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int deletePsyact(JobActivityRecordPsyactWithBLOBs record);

    public int deletePsyact(long record);

    /**
     * 删除记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int deletePsyresearch(JobActivityRecordPsyresearchWithBLOBs record);

    public int deletePsyresearch(long id);

    /**
     * 更新记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int updatePsycourse(JobActivityRecordPsycourseWithBLOBs record);

    /**
     * 更新记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int updatePsyact(JobActivityRecordPsyactWithBLOBs record);

    /**
     * 更新记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int updatePsyresearch(JobActivityRecordPsyresearchWithBLOBs record);

    public JobActivityRecordWithBLOBs selectRecordInView(String id, String tablename);

}
