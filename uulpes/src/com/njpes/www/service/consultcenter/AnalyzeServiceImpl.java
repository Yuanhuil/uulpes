package com.njpes.www.service.consultcenter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.consultcenter.AnalyzeMapper;
import com.njpes.www.entity.consultcenter.Analyze;
import com.njpes.www.utils.PageParameter;

@Service("analyzeService")
public class AnalyzeServiceImpl implements AnalyzeServiceI {
    @Autowired
    private AnalyzeMapper analyzeMapper;

    /*
     * (non-Javadoc)
     * 
     * @see com.njpes.www.service.consultcenter.AppointmentServiceI#
     * selectListByEntity(com.njpes.www.entity.consultcenter.Appointment,
     * com.njpes.www.utils.PageParameter, java.util.Date, java.util.Date)
     */
    @Override
    public List<Analyze> selectListByEntity(Analyze analyze, PageParameter page, Date beginDate, Date endDate) {
        // TODO Auto-generated method stub
        return analyzeMapper.selectEntityByPage(analyze, page, beginDate, endDate);
    }

    @Override
    public List<Map> select(String string, Analyze analyze, Date beginDate, Date endDate) {
        // TODO Auto-generated method stub
        return analyzeMapper.count(string, analyze, beginDate, endDate);
    }

    @Override
    public List<Map> select1(String filed, Analyze analyze, Date beginDate, Date endDate) {
        // TODO Auto-generated method stub
        return analyzeMapper.count1(filed, analyze, beginDate, endDate);
    }

    @Override
    public List<Map> selectInSchool(String string, Analyze analyze, Date beginDate, Date endDate) {
        return analyzeMapper.countInSchool(string, analyze, beginDate, endDate);
    }

    @Override
    public List<Map> selectInCounty(String string, Analyze analyze, Date beginDate, Date endDate) {
        return analyzeMapper.countInCounty(string, analyze, beginDate, endDate);
    }

    @Override
    public List<Map> selectInCity(String string, Analyze analyze, String xzxs, Date beginDate, Date endDate) {
        return analyzeMapper.countInCity(string, analyze, xzxs, beginDate, endDate);
    }

    @Override
    public List<Map> select1InSchool(String string, Analyze analyze, Date beginDate, Date endDate) {
        return analyzeMapper.count1InSchool(string, analyze, beginDate, endDate);
    }

    @Override
    public List<Map> select1InCounty(String string, Analyze analyze, Date beginDate, Date endDate) {
        return analyzeMapper.count1InCounty(string, analyze, beginDate, endDate);
    }

    @Override
    public List<Map> select1InCity(String string, Analyze analyze, String xzxs, Date beginDate, Date endDate) {
        return analyzeMapper.count1InCity(string, analyze, xzxs, beginDate, endDate);
    }

}
