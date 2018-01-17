package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import com.njpes.www.entity.consultcenter.WarningInterveneS;
import com.njpes.www.entity.consultcenter.WarningInterveneSWithBLOBs;
import com.njpes.www.utils.PageParameter;

public interface WarningInterveneSServiceI {

    public int updateEntity(WarningInterveneSWithBLOBs warningInterveneS);

    public int saveEntity(WarningInterveneSWithBLOBs warningInterveneS);

    public int delEntity(WarningInterveneS warningInterveneS);

    public WarningInterveneSWithBLOBs selectByPrimaryKey(long id);

    public List<WarningInterveneS> selectListByEntity(WarningInterveneS warningInterveneS, PageParameter page,
            Date beginDate, Date endDate, int step);

    public List<WarningInterveneSWithBLOBs> selectListByEntityWithBLOBs(WarningInterveneSWithBLOBs warningInterveneS,
            PageParameter page, Date beginDate, Date endDate, int step);

}
