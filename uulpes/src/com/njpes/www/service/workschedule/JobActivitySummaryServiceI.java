package com.njpes.www.service.workschedule;

import java.util.List;

import com.njpes.www.entity.workschedule.JobActivitySummaryWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface JobActivitySummaryServiceI {
    public static String resourceIdentity = "workschedule:activitysummary";

    /**
     * 根据条件查询活动总结
     * 
     * @param params
     *            查询条件
     * @param page
     *            分页信息
     * @return 返回查询结果列表
     * @author 赵忠诚
     */
    public List<JobActivitySummaryWithBLOBs> findByPage(JobActivitySummaryWithBLOBs params, PageParameter page);

    /**
     * 插入数据到表中
     * 
     * @param record
     * @author 赵忠诚
     */
    public int insert(JobActivitySummaryWithBLOBs record);

    /**
     * 根据实体参数查询实体
     * 
     * @param record
     * @author 赵忠诚
     */
    public JobActivitySummaryWithBLOBs findById(JobActivitySummaryWithBLOBs record);

    /**
     * 删除记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int delete(JobActivitySummaryWithBLOBs record);

    public int delete(long record);

    /**
     * 更新记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int update(JobActivitySummaryWithBLOBs record);

    /**
     * 获得学年
     * 
     * @param depid
     * @author 赵忠诚
     */
    public List<String> getSchoolYearsBySchool(long depid);
}
