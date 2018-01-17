package com.njpes.www.dao.scaletoollib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import edutec.scale.db.ScaleDto;
import edutec.scale.model.PreWarn;
import edutec.scale.model.Scale;
import edutec.scale.model.ScaleExplainObject;
import edutec.scale.model.ScaleNormObject;
import edutec.scale.model.ScoreGrade;
import heracles.domain.model.ModelEntity;
import heracles.util.ArrayRow;
import com.njpes.www.entity.scaletoollib.ScaleFilterParam;

public interface ScaleOperatorDao {

    public int selectScale(int value);

    public List<Map> getAllGradeCode();

    public void insertScale_(ScaleDto scaleDto);

    public void insertScaledimexplain_(ScaleExplainObject scaleExplainObject);

    public void insertScaleNorm_(ScaleNormObject scaleNormObject);

    public void insertScaleNormCustom_(ScaleNormObject scaleNormObject);

    public void insertScaleScoregrade_(ScoreGrade scoreGrade);

    public void insertScalePrewarn_(PreWarn preWarn);

    public void updateScaleTitle(ModelEntity modelEntity);

    public boolean updateScaleShowTitle(String code, String showtitle);

    public void updateScaleFlag(Scale Scale);

    public void updateScaleTypeId(HashMap map);

    public List<ModelEntity> selectAllScaleBlur_(String keyword);

    public List<Scale> selectAllScales_();

    public List<ArrayRow> selectScaleTypes_();

    public String selectXmlStr(int id);

    public void updateXmlStr(Map<String, Object> map);

    public void deleteScale(String value);

    public void deleteStudentExamdo(String value);

    public void deleteTeacherExamdo(String value);

    public void deleteStudentDimExam(String value);

    public void deleteTeacherDimExam(String value);

    public void deleteIndividuaExam(String value);

    public void deleteStudentExam(String value);

    public void deleteTeacherExam(String value);

    public void deleteScaleNorm(String value);

    public void deleteScaleScoreGrade(String value);

    public void deleteScaleWarning(String value);

    public void deleteScaleExplain(String value);

    public void deleteScaleGrades(String value);

    public List<Map> getScaleCfgsByOrg(long orgId);

    public void insertScaleCfgOrg(Map<?, ?> parmap);

    public void updateScaleCfgOrg(Map<?, ?> parmap);

    public void updateTitleAlias(Map<?, ?> parmap);

    public Scale getScaleByCode(String code);
}
