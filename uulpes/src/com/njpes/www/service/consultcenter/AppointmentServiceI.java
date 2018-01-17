package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.Appointment;
import com.njpes.www.utils.PageParameter;

public interface AppointmentServiceI {

    public int updateAppointment(Appointment appointment);

    public int saveAppointment(Appointment appointment);

    public int delAppointment(Appointment appointment);

    public Appointment selectByPrimaryKey(long id);

    public List<Appointment> selectListByEntity(Appointment appointment, PageParameter page, Date beginDate,
            Date endDate);

    /**
     * @Description:
     * @param ids
     * @return
     */
    public List<Appointment> selectListByIds(Long[] ids);

}
