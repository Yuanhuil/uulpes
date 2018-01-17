package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.AppointmentViewMapper;
import com.njpes.www.entity.consultcenter.AppointmentView;
import com.njpes.www.utils.PageParameter;

@Service("appointmentViewService")
public class AppointmentViewServiceImpl implements AppointmentViewServiceI {
    @Autowired
    private AppointmentViewMapper appointmentViewMapper;

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.AppointmentViewServiceI#
     * selectByEntity(com.njpes.www.entity.consultcenter.AppointmentView,
     * com.njpes.www.utils.PageParameter, java.util.Date, java.util.Date)
     */
    @Override
    public List<AppointmentView> selectByEntity(AppointmentView appointmentView, PageParameter page, Date beginDate,
            Date endDate) {
        // TODO Auto-generated method stub
        return appointmentViewMapper.selectEntityByPage(appointmentView, page, beginDate, endDate);
    }

}
