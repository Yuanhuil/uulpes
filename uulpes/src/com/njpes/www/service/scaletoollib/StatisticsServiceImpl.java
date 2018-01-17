package com.njpes.www.service.scaletoollib;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.scaletoollib.ExamresultStudentMapper;
import com.njpes.www.dao.scaletoollib.ScaleInfoDao;
import com.njpes.www.dao.scaletoollib.StatColumnMapper;
import com.njpes.www.dao.scaletoollib.StatConfigMapper;
import com.njpes.www.entity.scaletoollib.ExamresultHuman;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.entity.scaletoollib.StatColumn;
import com.njpes.www.entity.scaletoollib.StatConfigWithBLOBs;
import com.njpes.www.entity.scaletoollib.StatMethod;
import com.njpes.www.entity.scaletoollib.StatParams;
import com.njpes.www.entity.scaletoollib.StatResult;
import com.njpes.www.entity.scaletoollib.StatScope;
import com.njpes.www.utils.SpringContextHolder;

@Service("StatisticsService")
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    ExamresultStudentMapper examResultStuMapper;

    @Autowired
    ScaleInfoDao scaleInfoDao;

    @Autowired
    StatConfigMapper statConfigMapper;

    @Autowired
    StatColumnMapper columnMapper;

    @Override
    public List<ExamresultHuman> getStudentResult(StatScope scope) {
        // TODO Auto-generated method stub
        return examResultStuMapper.getStudentResult(scope);
    }

    @Override
    public List<ExamresultHuman> getTeacherResult(StatScope scope) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ScaleInfo> queryAllScalesForStudent() {
        // TODO Auto-generated method stub
        return scaleInfoDao.queryAllScalesForStudent();
    }

    @Override
    public List<ScaleInfo> queryAllScalesForTeacher() {
        // TODO Auto-generated method stub
        return scaleInfoDao.queryAllScalesForTeacher();
    }

    @Override
    public StatResult doStat(StatParams params) throws Exception {
        // TODO Auto-generated method stub
        int method = params.getMethod();
        String methodClass = StatMethod.ANALYSIS_CLASS[method];
        Class statServiceClass;
        statServiceClass = Class.forName("com.njpes.www.service.scaletoollib." + methodClass + "ServiceImpl");
        Object sm = SpringContextHolder.getBean(methodClass + "Service", statServiceClass);
        Method stat = statServiceClass.getMethod("stat", StatParams.class);
        return (StatResult) stat.invoke(sm, params);
    }

    public StatConfigWithBLOBs getStatConfig(int statType) {
        return statConfigMapper.selectByType(statType);
    }

    @Override
    public List<StatColumn> selectByTable(String tablename) {
        // TODO Auto-generated method stub
        return columnMapper.selectByTable(tablename);
    }

}
