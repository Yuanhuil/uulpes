package com.njpes.www.service.scaletoollib;

import java.util.List;

import com.njpes.www.entity.scaletoollib.ExamresultHuman;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.entity.scaletoollib.StatColumn;
import com.njpes.www.entity.scaletoollib.StatConfigWithBLOBs;
import com.njpes.www.entity.scaletoollib.StatParams;
import com.njpes.www.entity.scaletoollib.StatResult;
import com.njpes.www.entity.scaletoollib.StatScope;

public interface StatisticsService {

    // 获得适合学生的所有量表
    public List<ScaleInfo> queryAllScalesForStudent();

    // 获得适合老师的所有量表
    public List<ScaleInfo> queryAllScalesForTeacher();

    // 获得符合条件的所有学生的测评结果
    public List<ExamresultHuman> getStudentResult(StatScope scope);

    // 获得符合条件的所有教师的测评结果
    public List<ExamresultHuman> getTeacherResult(StatScope scope);

    // 统一的进行统计分析的接口
    public StatResult doStat(StatParams params) throws Exception;

    // 获得统计配置信息
    public StatConfigWithBLOBs getStatConfig(int StatType);

    // 根据表名获得所有的可统计列
    public List<StatColumn> selectByTable(String tablename);

}
