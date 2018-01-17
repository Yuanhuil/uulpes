package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.Scheduling;
import com.njpes.www.utils.PageParameter;

public interface SchedulingServiceI {

    public int updateScheduling(Scheduling scheduling);

    public int saveScheduling(Scheduling scheduling);

    public int delScheduling(Scheduling scheduling);

    public Scheduling selectByPrimaryKey(long id);

    public List<Scheduling> selectListByEntity(Scheduling scheduling, PageParameter page, Date beginDate, Date endDate);

    /**
     * @Description:
     * @param scheduling
     * @param startTimeId
     * @param endTimeId
     * @param startDate
     * @return
     */
    public String saveScheduling(Scheduling scheduling, int startTimeId, int endTimeId, Date startDate,
            String dateType);

    /**
     * @Description:
     * @param scheduling
     * @return
     */
    public int delByEntity(Scheduling scheduling);

    /**
     * @Description:
     * @param scheduling
     * @param endDate
     * @param dateType
     * @return
     */
    public String delByEntity(Scheduling scheduling, Date endDate, String dateType);

    public long getNextId(long id);

}
