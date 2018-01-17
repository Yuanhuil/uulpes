package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.WarningInterveneSWithBLOBs;
import com.njpes.www.entity.consultcenter.WarningStudent;
import com.njpes.www.utils.PageParameter;

public interface WarningStudentServiceI {

    public List<WarningStudent> selectListByEntity(WarningStudent warningStudent, PageParameter page, Date beginDate,
            Date endDate);

    /**
     * @Description:
     * @param warningStudent
     * @return
     */
    public int updateIswarnsure(WarningStudent warningStudent);

    public WarningStudent selectEntityById(Long id);

    WarningInterveneSWithBLOBs WarningStudent2WarningInterveneS(WarningStudent warningStudent);

}
