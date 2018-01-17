package com.njpes.www.service.workschedule;

import java.util.List;

import com.njpes.www.entity.workschedule.JobActivityPlanWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface JobActivityPlanServiceI {

    public static String resourceIdentity = "workschedule:activityplan";

    /**
     * 根据条件查询工作计划
     * 
     * @param params
     *            查询条件
     * @param page
     *            分页信息
     * @return 返回查询结果列表
     * @author 赵忠诚
     */
    public List<JobActivityPlanWithBLOBs> findByPage(JobActivityPlanWithBLOBs params, PageParameter page);

    /**
     * 插入数据到表中
     * 
     * @param record
     * @author 赵忠诚
     */
    public int insert(JobActivityPlanWithBLOBs record);

    /**
     * 根据实体参数查询实体
     * 
     * @param record
     * @author 赵忠诚
     */
    public JobActivityPlanWithBLOBs findById(JobActivityPlanWithBLOBs record);

    /**
     * 删除记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int delete(JobActivityPlanWithBLOBs record);

    public int delete(Long id);

    /**
     * 更新记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int update(JobActivityPlanWithBLOBs record);

    /**
     * 仅仅是更新状态为，其他字段不更新，这样做的目的是为了防止数据被恶意篡改
     * 
     * @param record
     * @author 赵忠诚
     */
    public int justUpdateState(JobActivityPlanWithBLOBs record);

    /**
     * 根据登陆组织机构获取该组织机构添加的所有学年的信息
     * 
     * @param orgid
     *            组织机构(学校)唯一id
     * @return 获取学年的列表
     * @author 赵忠诚
     */
    public List<String> getSchoolYearsBySchool(long orgid);

}
