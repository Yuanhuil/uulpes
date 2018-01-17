package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.WarningInterveneSMapper;
import com.njpes.www.entity.consultcenter.WarningInterveneS;
import com.njpes.www.entity.consultcenter.WarningInterveneSWithBLOBs;
import com.njpes.www.utils.PageParameter;

@Service("warningInterveneSService")
public class WarningInterveneSServiceImpl implements WarningInterveneSServiceI {

    @Autowired
    private WarningInterveneSMapper warningInterveneSMapper;

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningInterveneSServiceI#
     * updateEntity(com.njpes.www.entity.consultcenter.WarningInterveneS)
     */
    @Override
    public int updateEntity(WarningInterveneSWithBLOBs warningInterveneS) {
        // TODO Auto-generated method stub
        return warningInterveneSMapper.updateByPrimaryKeyWithBLOBs(warningInterveneS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.WarningInterveneSServiceI#saveEntity(
     * com.njpes.www.entity.consultcenter.WarningInterveneS)
     */
    @Override
    public int saveEntity(WarningInterveneSWithBLOBs warningInterveneS) {
        // TODO Auto-generated method stub
        return warningInterveneSMapper.insertSelective(warningInterveneS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.WarningInterveneSServiceI#delEntity(
     * com.njpes.www.entity.consultcenter.WarningInterveneS)
     */
    @Override
    public int delEntity(WarningInterveneS warningInterveneS) {
        // TODO Auto-generated method stub
        return warningInterveneSMapper.deleteByPrimaryKey(warningInterveneS.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningInterveneSServiceI#
     * selectByPrimaryKey(long)
     */
    @Override
    public WarningInterveneSWithBLOBs selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return warningInterveneSMapper.selectByPrimaryKey(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.WarningInterveneSServiceI#
     * selectListByEntity(com.njpes.www.entity.consultcenter.WarningInterveneS,
     * com.njpes.www.utils.PageParameter, java.util.Date, java.util.Date)
     */
    @Override
    public List<WarningInterveneS> selectListByEntity(WarningInterveneS warningInterveneS, PageParameter page,
            Date beginDate, Date endDate, int step) {
        // TODO Auto-generated method stub
        return warningInterveneSMapper.selectEntityByPage(warningInterveneS, page, beginDate, endDate, step);
    }

    @Override
    public List<WarningInterveneSWithBLOBs> selectListByEntityWithBLOBs(WarningInterveneSWithBLOBs warningInterveneS,
            PageParameter page, Date beginDate, Date endDate, int step) {
        // TODO Auto-generated method stub
        return warningInterveneSMapper.selectEntityBLOBsByPage(warningInterveneS, page, beginDate, endDate, step);
    }

}
