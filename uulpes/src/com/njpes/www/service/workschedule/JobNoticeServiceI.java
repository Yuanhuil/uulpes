package com.njpes.www.service.workschedule;

import java.util.List;

import com.njpes.www.entity.workschedule.JobNoticeWebQueryParam;
import com.njpes.www.entity.workschedule.JobNoticeWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface JobNoticeServiceI {

    public final String resourceIdentity = "workschedule:notice";

    public final String filename = "/jobnotice/";

    /**
     * 根据条件查询notice信息返回给页面
     * 
     * @param noticeParams
     *            查询条件
     * @param page
     *            分页信息
     * @return 返回查询结果列表
     * @author 赵忠诚
     */
    public List<JobNoticeWithBLOBs> findNoticeByPage(JobNoticeWebQueryParam noticeParams, PageParameter page);

    public List<JobNoticeWithBLOBs> findAllNoticeByPage(JobNoticeWebQueryParam noticeParams, PageParameter page);

    /**
     * 插入数据到表中
     * 
     * @param record
     * @author 赵忠诚
     */
    public int insert(JobNoticeWithBLOBs record);

    /**
     * 根据实体参数查询实体
     * 
     * @param record
     * @author 赵忠诚
     */
    public JobNoticeWithBLOBs findById(JobNoticeWithBLOBs record);

    public JobNoticeWithBLOBs findById(long id);

    public int del(long id);

    /**
     * 查看由sendorgid下发的通知通告信息
     * 
     * @param record
     * @param sendOrgid
     *            下发通知通告的单位id
     * @author 赵忠诚
     */
    public JobNoticeWithBLOBs findById(JobNoticeWithBLOBs record, long sendOrgid);

    /**
     * 删除记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int delete(JobNoticeWithBLOBs record);

    /**
     * 更新记录
     * 
     * @param record
     * @author 赵忠诚
     */
    public int update(JobNoticeWithBLOBs record);

    /**
     * 仅仅是更新状态为，其他字段不更新，这样做的目的是为了防止数据被恶意篡改
     * 
     * @param record
     * @author 赵忠诚
     */
    public int justUpdateState(JobNoticeWithBLOBs record);

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
