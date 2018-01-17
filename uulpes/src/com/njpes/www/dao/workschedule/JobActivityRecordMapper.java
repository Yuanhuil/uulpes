package com.njpes.www.dao.workschedule;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.workschedule.JobActivityRecordWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface JobActivityRecordMapper {

    /**
     * 视图查询，不分页
     * 
     * @author 赵忠诚 *
     */
    public List<JobActivityRecordWithBLOBs> selectAll(@Param("params") JobActivityRecordWithBLOBs params);

    /**
     * 视图查询，分页
     * 
     * @author 赵忠诚 *
     */
    public List<JobActivityRecordWithBLOBs> selectByPage(@Param("params") JobActivityRecordWithBLOBs params,
            @Param("page") PageParameter page);

    public List<JobActivityRecordWithBLOBs> selectBySubOrgidsPage(@Param("params") JobActivityRecordWithBLOBs params,
            @Param("page") PageParameter page, @Param("orgId") Long orgId);

    public List<JobActivityRecordWithBLOBs> selectByOrgidsPage(@Param("params") JobActivityRecordWithBLOBs params,
            @Param("page") PageParameter page, @Param("orgId") Long orgId);

    public JobActivityRecordWithBLOBs selectRecord(@Param("id") String id, @Param("tablename") String tablename);

    public int updateField(@Param("tablename") String tablename, @Param("field") String field,
            @Param("recordId") long recordId, @Param("fieldValue") String fieldValue);
}