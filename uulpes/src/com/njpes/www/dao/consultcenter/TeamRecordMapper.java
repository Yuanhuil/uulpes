package com.njpes.www.dao.consultcenter;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.consultcenter.TeamRecord;
import com.njpes.www.entity.consultcenter.TeamRecordWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface TeamRecordMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_team_record
     * 
     * @mbggenerated Wed May 27 20:28:42 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_team_record
     * 
     * @mbggenerated Wed May 27 20:28:42 CST 2015
     */
    int insert(TeamRecordWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_team_record
     * 
     * @mbggenerated Wed May 27 20:28:42 CST 2015
     */
    int insertSelective(TeamRecordWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_team_record
     * 
     * @mbggenerated Wed May 27 20:28:42 CST 2015
     */
    TeamRecordWithBLOBs selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_team_record
     * 
     * @mbggenerated Wed May 27 20:28:42 CST 2015
     */
    int updateByPrimaryKeySelective(TeamRecordWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_team_record
     * 
     * @mbggenerated Wed May 27 20:28:42 CST 2015
     */
    int updateByPrimaryKeyWithBLOBs(TeamRecordWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table coach_team_record
     * 
     * @mbggenerated Wed May 27 20:28:42 CST 2015
     */
    int updateByPrimaryKey(TeamRecord record);

    /**
     * @Description:
     * @param recordWithBLOBs
     * @param page
     * @return
     */
    List<TeamRecordWithBLOBs> selectEntityByPage(@Param("recordWithBLOBs") TeamRecordWithBLOBs recordWithBLOBs,
            @Param("page") PageParameter page, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);
}