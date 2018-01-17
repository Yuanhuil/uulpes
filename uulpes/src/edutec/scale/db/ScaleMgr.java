package edutec.scale.db;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.service.scaletoollib.ScaleMgrService;

import edutec.exception.QueryException;
import edutec.scale.model.PreWarn;
import edutec.scale.model.Scale;
import edutec.scale.model.ScaleExplainObject;
import edutec.scale.model.ScaleNormObject;
import edutec.scale.model.ScoreGrade;
import heracles.db.ibatis.PersistableException;
import heracles.domain.model.ModelEntity;

@Service("ScaleMgr")
public class ScaleMgr {
    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ScaleMgrService scaleMgrService;

    public List<Scale> getSimpleScales() throws QueryException {
        try {
            return scaleMgrService.getSimpleScales();
        } catch (PersistableException e) {
            logger.error(e.getMessage(), e);
            throw new QueryException("查询数据库getSimpleScales时，出现问题，请与系统管理员联系");
        }
    }

    public List<KeyValue> getScaleTypes() {
        try {
            return scaleMgrService.getScaleTypes();
        } catch (PersistableException e) {
            logger.error(e.getMessage(), e);
            throw new QueryException("查询数据库selectScaleTypes_时，出现问题，请与系统管理员联系");
        }
    }

    public void updateScaleFlag(Scale scale) {
        try {
            scaleMgrService.updateScaleFlag(scale);
        } catch (PersistableException e) {
            throw new RuntimeException("更新量表数据库，出现异常");

        }
    }

    public List<ModelEntity> getBlurScales(String keyword) throws QueryException {
        try {
            return scaleMgrService.getBlurScales(keyword);
        } catch (PersistableException e) {
            logger.error(e.getMessage(), e);
            throw new QueryException("查询数据库时，出现问题，请与系统管理员联系");
        }
    }

    public void appendScale(ScaleDto scale) {
        try {
            scaleMgrService.appendScale(scale);
        } finally {
        }
    }

    // 添加得分水平
    public void insertPreWarnList(List<PreWarn> preWarns, long nextId) {
        scaleMgrService.insertPreWarnList(preWarns, nextId);
    }

    // 添加计分
    public void insertScoreGradeList(List<ScoreGrade> scoreGrades, long nextId) {
        scaleMgrService.insertScoreGradeList(scoreGrades, nextId);
    }

    // huangc
    // 插入结果解释和指导建议
    public void appendDimExplainAndInstr(List<ScaleExplainObject> scaleExplainObjectList, long nextId) {
        scaleMgrService.appendDimExplainAndInstr(scaleExplainObjectList, nextId);

    }

    // huangc
    // 插入常模
    public void appendNormList(List<ScaleNormObject> normObjList, long nextId, int type, long orgid, long userid) {
        scaleMgrService.appendNormList(normObjList, nextId, type, orgid, userid);
    }

    public int appendNormInfo(String scaleid, String normname, int type, Date createtime, long createuserid,
            String description, int areaid, long orgid, int orglevel, String editer, String edittime) {
        return scaleMgrService.appendNormInfo(scaleid, normname, type, createtime, createuserid, description, areaid,
                orgid, orglevel, editer, edittime);
    }

    public void appendNormCustomList(List<ScaleNormObject> normObjList, int scaleId, int orglevel, int areaid,
            long edituserid) {
        scaleMgrService.appendNormCustomList(normObjList, scaleId, orglevel, areaid, edituserid);

    }

    /**
     * 插入得分水平
     * 
     * @author shibin
     * @param scoreGrade
     */
    public void insertScoreGrade(ScoreGrade scoreGrade) {
        scaleMgrService.insertScoreGrade(scoreGrade);
    }

    /**
     * 插入预警级别
     * 
     * @author shibin
     * @param preWarn
     */
    public void insertPreWarn(PreWarn preWarn) {
        scaleMgrService.insertPreWarn(preWarn);
    }

    public void deleteScale(String scaleId) {
        scaleMgrService.deleteScale(scaleId);
    }

    public String getXmlStr(String scaleId) {
        return scaleMgrService.getXmlStr(scaleId);
    }

    // 判断scaleid是否存在
    public int getScale(String scaleId) {
        return scaleMgrService.getScale(scaleId);
    }

    public void updateXmlStr(String xmlStrl, String scaleId) {
        scaleMgrService.updateXmlStr(xmlStrl, scaleId);
    }

    @SuppressWarnings("unchecked")
    public List<Map> getScaleCfgsByOrg(long orgId) {
        return scaleMgrService.getScaleCfgsByOrg(orgId);
    }

    public void insertScaleCfgOrg(Map<?, ?> parmap) {
        scaleMgrService.insertScaleCfgOrg(parmap);
    }

    public void updateScaleCfgOrg(Map<?, ?> parmap) {
        scaleMgrService.updateScaleCfgOrg(parmap);
    }

    public void updateTitleAlias(Map<?, ?> parmap) {
        scaleMgrService.updateTitleAlias(parmap);
    }

    private boolean on = true;
    private boolean isStart = false;

    public boolean isOn() {
        return on;
    }

    public void on() {
        this.on = true;
    }

    public void off() {
        this.on = false;
    }
}
