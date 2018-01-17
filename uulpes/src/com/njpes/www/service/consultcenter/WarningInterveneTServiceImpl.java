package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.WarningInterveneTMapper;
import com.njpes.www.entity.consultcenter.WarningInterveneT;
import com.njpes.www.entity.consultcenter.WarningInterveneTWithBLOBs;
import com.njpes.www.utils.PageParameter;

@Service("warningInterveneTService")
public class WarningInterveneTServiceImpl implements WarningInterveneTServiceI {

    @Autowired
    private WarningInterveneTMapper warningInterveneTMapper;

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningInterveneTServiceI#
     * updateEntity(com.njpes.www.entity.consultcenter.WarningInterveneT)
     */
    @Override
    public int updateEntity(WarningInterveneTWithBLOBs warningInterveneT) {
        // TODO Auto-generated method stub
        return warningInterveneTMapper.updateByPrimaryKeyWithBLOBs(warningInterveneT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.WarningInterveneTServiceI#saveEntity(
     * com.njpes.www.entity.consultcenter.WarningInterveneT)
     */
    @Override
    public int saveEntity(WarningInterveneTWithBLOBs warningInterveneT) {
        // TODO Auto-generated method stub
        return warningInterveneTMapper.insertSelective(warningInterveneT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.WarningInterveneTServiceI#delEntity(
     * com.njpes.www.entity.consultcenter.WarningInterveneT)
     */
    @Override
    public int delEntity(WarningInterveneT warningInterveneT) {
        // TODO Auto-generated method stub
        return warningInterveneTMapper.deleteByPrimaryKey(warningInterveneT.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningInterveneTServiceI#
     * selectByPrimaryKey(long)
     */
    @Override
    public WarningInterveneTWithBLOBs selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return warningInterveneTMapper.selectByPrimaryKey(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningInterveneTServiceI#
     * selectListByEntity(com.njpes.www.entity.consultcenter.WarningInterveneT,
     * com.njpes.www.utils.PageParameter, java.util.Date, java.util.Date)
     */
    @Override
    public List<WarningInterveneT> selectListByEntity(WarningInterveneT warningInterveneT, PageParameter page,
            Date beginDate, Date endDate, int step) {
        // TODO Auto-generated method stub
        return warningInterveneTMapper.selectEntityByPage(warningInterveneT, page, beginDate, endDate, step);
    }

    @Override
    public List<WarningInterveneTWithBLOBs> selectListByEntityWithBLOBs(WarningInterveneTWithBLOBs warningInterveneT,
            PageParameter pageParameter, Date beginDate, Date endDate, int step) {
        // TODO Auto-generated method stub
        return warningInterveneTMapper.selectEntityBLOBsByPage(warningInterveneT, pageParameter, beginDate, endDate,
                step);
    }

}
