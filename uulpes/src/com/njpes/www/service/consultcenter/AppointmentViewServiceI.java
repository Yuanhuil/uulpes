/**
 * 
 */
package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.AppointmentView;
import com.njpes.www.utils.PageParameter;

/**
 * @Description:
 * @author zhangchao
 * @Date 2015-6-23 下午10:37:28
 */
public interface AppointmentViewServiceI {

    /**
     * @Description:
     * @param appointmentView
     * @param page
     * @param beginDate
     * @param endDate
     * @return
     */
    List<AppointmentView> selectByEntity(AppointmentView appointmentView, PageParameter page, Date beginDate,
            Date endDate);

}
