package com.njpes.www.service.workschedule;

import java.util.List;

import com.njpes.www.entity.workschedule.JobPlan;
import com.njpes.www.entity.workschedule.JobPlanWithBLOBs;
import com.njpes.www.utils.PageParameter;

/**
 * @author 赵忠诚
 */
public interface JobPlanServiceI {
    public final String resourceIdentity = "workschedule:plan";

    public final String filename = "/jobplan/";

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
    public List<JobPlan> findByPage(JobPlanWithBLOBs params, PageParameter page);

    /**
     * 插入数据到表中
     * 
     * @param record
     * @author 赵忠诚
     */
    public int insert(JobPlanWithBLOBs record);

    /**
     * 根据实体参数查询实体
     * 
     * @param record
     * @author 赵忠诚
     */
    public JobPlanWithBLOBs findById(JobPlanWithBLOBs record);

    /**
     * 查看由sendorgid下发的工作计划
     * 
     * @param record
     * @param sendOrgid
     *            下发通知通告的单位id
     * @author 赵忠诚
     */
    public JobPlanWithBLOBs findById(JobPlanWithBLOBs record, long sendOrgid);

    /**
     * 删除记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int delete(JobPlanWithBLOBs record);

    public int delete(long record);

    /**
     * 更新记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int update(JobPlanWithBLOBs record);

    /**
     * 仅仅是更新状态为，其他字段不更新，这样做的目的是为了防止数据被恶意篡改
     * 
     * @param record
     * @author 赵忠诚
     */
    public int justUpdateState(JobPlanWithBLOBs record);

    /**
     * 保存或者更新下发单位信息
     * 
     * @param noticeId
     *            通知通告id
     * @param shareOrgids
     *            下发单位id
     * @param sendOrgid
     *            谁下发的
     * @return 返回更新的条数
     * @author 赵忠诚
     */
    public int updateOrSaveShareInfo(long noticeId, long[] shareOrgids, long sendOrgid);
}
