package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.AppointmentMapper;
import com.njpes.www.entity.consultcenter.Appointment;
import com.njpes.www.utils.PageParameter;

@Service("appointmentService")
public class AppointmentServiceImpl implements AppointmentServiceI {
    @Autowired
    private AppointmentMapper appointmentMapper;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.AppointmentServiceI#updateAppointment
     * (com.njpes.www.entity.consultcenter.Appointment)
     */
    @Override
    public int updateAppointment(Appointment appointment) {
        // TODO Auto-generated method stub
        return appointmentMapper.updateByPrimaryKeyWithBLOBs(appointment);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.AppointmentServiceI#saveAppointment(
     * com.njpes.www.entity.consultcenter.Appointment)
     */
    @Override
    public int saveAppointment(Appointment appointment) {
        // TODO Auto-generated method stub
        return appointmentMapper.insert(appointment);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.AppointmentServiceI#delAppointment(
     * com.njpes.www.entity.consultcenter.Appointment)
     */
    @Override
    public int delAppointment(Appointment appointment) {
        // TODO Auto-generated method stub
        return appointmentMapper.deleteByPrimaryKeyWithNext(appointment.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.AppointmentServiceI#
     * selectByPrimaryKey(long)
     */
    @Override
    public Appointment selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return appointmentMapper.selectByPrimaryKey(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.AppointmentServiceI#
     * selectListByEntity(com.njpes.www.entity.consultcenter.Appointment,
     * com.njpes.www.utils.PageParameter, java.util.Date, java.util.Date)
     */
    @Override
    public List<Appointment> selectListByEntity(Appointment appointment, PageParameter page, Date beginDate,
            Date endDate) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.AppointmentServiceI#selectListByIds(
     * java.lang.String)
     */
    @Override
    public List<Appointment> selectListByIds(Long[] ids) {
        // TODO Auto-generated method stub
        if (ids.length == 0) {
            ids = new Long[] { null };
        }
        return appointmentMapper.selectListByIds(ids);
    }

}
