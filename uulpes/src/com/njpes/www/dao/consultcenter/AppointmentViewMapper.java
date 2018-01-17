package com.njpes.www.dao.consultcenter;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.njpes.www.entity.consultcenter.AppointmentView;
import com.njpes.www.utils.PageParameter;

public interface AppointmentViewMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table view_appointment
     * 
     * @mbggenerated Wed Jun 24 23:26:04 CST 2015
     */
    int insert(AppointmentView record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds
     * to the database table view_appointment
     * 
     * @mbggenerated Wed Jun 24 23:26:04 CST 2015
     */
    int insertSelective(AppointmentView record);

    /**
     * @Description:
     * @param appointmentView
     * @param page
     * @param beginDate
     * @param endDate
     * @return
     */
    List<AppointmentView> selectEntityByPage(@Param("appointmentView") AppointmentView appointmentView,
            @Param("page") PageParameter page, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);
}