package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.njpes.www.entity.consultcenter.Analyze;
import com.njpes.www.utils.PageParameter;

public interface AnalyzeServiceI {

    public List<Analyze> selectListByEntity(Analyze analyze, PageParameter page, Date beginDate, Date endDate);

    public List<Map> select(String string, Analyze analyze, Date beginDate, Date endDate);

    public List<Map> selectInSchool(String string, Analyze analyze, Date beginDate, Date endDate);

    public List<Map> selectInCounty(String string, Analyze analyze, Date beginDate, Date endDate);

    public List<Map> selectInCity(String string, Analyze analyze, String xzxs, Date beginDate, Date endDate);

    public List<Map> select1(String filed, Analyze analyze, Date beginDate, Date endDate);

    public List<Map> select1InSchool(String string, Analyze analyze, Date beginDate, Date endDate);

    public List<Map> select1InCounty(String string, Analyze analyze, Date beginDate, Date endDate);

    public List<Map> select1InCity(String string, Analyze analyze, String xzxs, Date beginDate, Date endDate);

}
