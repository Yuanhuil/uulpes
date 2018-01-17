package com.njpes.www.service.scaletoollib;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.KeyValue;

import edutec.exception.QueryException;
import edutec.scale.db.ScaleDto;
import edutec.scale.model.PreWarn;
import edutec.scale.model.Scale;
import edutec.scale.model.ScaleExplainObject;
import edutec.scale.model.ScaleNormObject;
import edutec.scale.model.ScoreGrade;
import heracles.domain.model.ModelEntity;

public interface ScaleMgrService {
    public List<Scale> getSimpleScales() throws QueryException;

    public List<KeyValue> getScaleTypes();

    public void updateScaleFlag(Scale scale);

    public List<ModelEntity> getBlurScales(String keyword) throws QueryException;

    public void appendScale(ScaleDto scale);

    // 添加得分水平
    public void insertPreWarnList(List<PreWarn> preWarns, long nextId);

    // 添加计分
    public void insertScoreGradeList(List<ScoreGrade> scoreGrades, long nextId);

    // huangc
    // 插入结果解释和指导建议
    public void appendDimExplainAndInstr(List<ScaleExplainObject> scaleExplainObjectList, long nextId);

    // huangc
    // 插入常模
    public void appendNormList(List<ScaleNormObject> normObjList, long nextId, int type, long orgid, long userid);

    public int appendNormInfo(String scaleid, String normname, int type, Date createtime, long createuserid,
            String description, int areaid, long orgid, int orglevel, String editer, String eidttime);

    public void appendNormCustomList(List<ScaleNormObject> normObjList, int scaleId, int orglevel, int areaid,
            long edituserid);

    /**
     * 插入得分水平
     * 
     * @author shibin
     * @param scoreGrade
     */
    public void insertScoreGrade(ScoreGrade scoreGrade);

    /**
     * 插入预警级别
     * 
     * @author shibin
     * @param preWarn
     */
    public void insertPreWarn(PreWarn preWarn);

    public void deleteScale(String scaleId);

    public String getXmlStr(String scaleId);

    // 判断scaleid是否存在
    public int getScale(String scaleId);

    public void updateXmlStr(String xmlStrl, String scaleId);

    @SuppressWarnings("unchecked")
    public List<Map> getScaleCfgsByOrg(long orgId);

    public void insertScaleCfgOrg(Map<?, ?> parmap);

    public void updateScaleCfgOrg(Map<?, ?> parmap);

    public void updateTitleAlias(Map<?, ?> parmap);

    public Scale getScaleByCode(String code);
}
