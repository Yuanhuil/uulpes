package com.njpes.www.dao.workschedule;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.workschedule.JobActivityRecordPsyactWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface JobActivityRecordPsyactMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job_activityrecord_psyact
     * 
     * @mbggenerated Tue Jul 21 00:22:22 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job_activityrecord_psyact
     * 
     * @mbggenerated Tue Jul 21 00:22:22 CST 2015
     */
    int insert(JobActivityRecordPsyactWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job_activityrecord_psyact
     * 
     * @mbggenerated Tue Jul 21 00:22:22 CST 2015
     */
    int insertSelective(JobActivityRecordPsyactWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job_activityrecord_psyact
     * 
     * @mbggenerated Tue Jul 21 00:22:22 CST 2015
     */
    JobActivityRecordPsyactWithBLOBs selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job_activityrecord_psyact
     * 
     * @mbggenerated Tue Jul 21 00:22:22 CST 2015
     */
    int updateByPrimaryKeySelective(JobActivityRecordPsyactWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table job_activityrecord_psyact
     * 
     * @mbggenerated Tue Jul 21 00:22:22 CST 2015
     */
    int updateByPrimaryKeyWithBLOBs(JobActivityRecordPsyactWithBLOBs record);

    /**
     * @author 赵忠�?
     */
    List<JobActivityRecordPsyactWithBLOBs> findByPage(@Param("entity") JobActivityRecordPsyactWithBLOBs params,
            @Param("page") PageParameter page);

}