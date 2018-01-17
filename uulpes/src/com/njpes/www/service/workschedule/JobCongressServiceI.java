package com.njpes.www.service.workschedule;

import java.util.List;

import com.njpes.www.entity.workschedule.JobCongressWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface JobCongressServiceI {
    public static String resourceIdentity = "workschedule:congress";

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
    public List<JobCongressWithBLOBs> findByPage(JobCongressWithBLOBs params, PageParameter page);

    /**
     * 插入数据到表中
     * 
     * @param record
     * @author 赵忠诚
     */
    public int insert(JobCongressWithBLOBs record);

    /**
     * 根据实体参数查询实体
     * 
     * @param record
     * @author 赵忠诚
     */
    public JobCongressWithBLOBs findById(JobCongressWithBLOBs record);

    /**
     * 删除记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int delete(JobCongressWithBLOBs record);

    public int delete(long record);

    /**
     * 更新记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int update(JobCongressWithBLOBs record);

}
