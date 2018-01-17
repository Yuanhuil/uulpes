package com.njpes.www.dao.scaletoollib;

import java.util.List;

import com.njpes.www.entity.scaletoollib.QueryInfo;
import com.njpes.www.entity.scaletoollib.ScaleDetailInfo;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.entity.scaletoollib.Scaletype;

import edutec.scale.model.Scale;

public interface ScaleInfoDao {

    public ScaleDetailInfo getScaleDetailById(int id);

    public int insertScaleInfo(ScaleInfo scaleInfo);

    public int updateScaleInfo(ScaleInfo scaleInfo);

    public int deleteScale(Integer id);

    public List<?> querySuitable(int gradeId);

    public List<?> getAllScaleType();

    public List<?> getScaleTypeByGroup(int groupid);

    public List<?> getAllScaleSource();

    public List<?> getAllSuitable();

    public int insertScaleType(Scaletype stype);

    public List<?> queryScaleList(QueryInfo info);

    public List<?> queryScaleForStudent(int gradeid);

    public List<?> queryScaleByGroupId(int groupid);

    public List<?> queryScaleForTeacher();

    public int deleteScaleType(int id);

    public List<ScaleInfo> queryAllScalesForStudent();

    public List<ScaleInfo> queryAllScalesForTeacher();

}
