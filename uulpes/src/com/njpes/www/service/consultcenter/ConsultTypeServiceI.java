package com.njpes.www.service.consultcenter;

import java.util.List;

import com.njpes.www.entity.consultcenter.ConsultType;

public interface ConsultTypeServiceI {

    public int updateConsultType(ConsultType consultType);

    public int saveConsultType(ConsultType consultType);

    public int delConsultType(ConsultType consultType);

    public List<ConsultType> getListByFid(long id);

    public List<ConsultType> getOpenListByFid(long id);

    public ConsultType selectByPrimaryKey(long idL);

    public int saveOrUpdateConsultType(ConsultType consultType);

}
