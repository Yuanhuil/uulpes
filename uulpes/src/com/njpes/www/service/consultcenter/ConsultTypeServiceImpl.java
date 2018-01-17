package com.njpes.www.service.consultcenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.ConsultTypeMapper;
import com.njpes.www.entity.consultcenter.ConsultType;

@Service("consultTypeService")
public class ConsultTypeServiceImpl implements ConsultTypeServiceI {

    @Autowired
    private ConsultTypeMapper ConsultTypeMapper;

    @Override
    public int updateConsultType(ConsultType consultType) {
        // TODO Auto-generated method stub
        return this.ConsultTypeMapper.updateByPrimaryKey(consultType);
    }

    @Override
    public int saveConsultType(ConsultType consultType) {
        // TODO Auto-generated method stub
        return this.ConsultTypeMapper.insert(consultType);
    }

    @Override
    public int delConsultType(ConsultType consultType) {
        // TODO Auto-generated method stub
        return this.ConsultTypeMapper.deleteByPrimaryKey(consultType.getId());
    }

    @Override
    public List<ConsultType> getListByFid(long id) {
        // TODO Auto-generated method stub
        return this.ConsultTypeMapper.getListByFid(id);
    }

    @Override
    public ConsultType selectByPrimaryKey(long idL) {
        // TODO Auto-generated method stub
        return this.ConsultTypeMapper.selectByPrimaryKey(idL);
    }

    @Override
    public int saveOrUpdateConsultType(ConsultType consultType) {

        int a = 0;
        if (consultType.getId() == null) {
            a = this.ConsultTypeMapper.insert(consultType);
        } else {
            a = this.ConsultTypeMapper.updateByPrimaryKey(consultType);
        }
        return a;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.ConsultTypeServiceI#getOpenListByFid(
     * long)
     */
    @Override
    public List<ConsultType> getOpenListByFid(long id) {
        // TODO Auto-generated method stub
        return this.ConsultTypeMapper.getOpenListByFid(id);
    }

}
