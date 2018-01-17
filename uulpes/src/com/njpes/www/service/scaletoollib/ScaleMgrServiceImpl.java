package com.njpes.www.service.scaletoollib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.scaletoollib.NormInfoMapper;
import com.njpes.www.dao.scaletoollib.ScaleNormLogMapper;
import com.njpes.www.dao.scaletoollib.ScaleOperatorDao;
import com.njpes.www.dao.scaletoollib.ScalenormMapper;
import com.njpes.www.entity.scaletoollib.NormInfo;
import com.njpes.www.entity.scaletoollib.ScaleNormLog;

import edutec.exception.QueryException;
import edutec.scale.db.ScaleDto;
import edutec.scale.model.PreWarn;
import edutec.scale.model.Scale;
import edutec.scale.model.ScaleExplainObject;
import edutec.scale.model.ScaleNormObject;
import edutec.scale.model.ScoreGrade;
import edutec.scale.util.ScaleUtils;
import heracles.db.ibatis.PersistableException;
import heracles.domain.model.ModelEntity;
import heracles.util.ArrayRow;
import heracles.util.UtilMisc;

@Service("ScaleMgrService")
public class ScaleMgrServiceImpl implements ScaleMgrService, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ScaleOperatorDao scaleOperatorDao;
    @Autowired
    private ScaleNormLogMapper scaleNormLogMapper;
    @Autowired
    private NormInfoMapper normInfoMapper;
    @Autowired
    private ScalenormMapper scalenormMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    public ScaleOperatorDao getScaleOperatorDao() {
        return scaleOperatorDao;
    }

    public void setScaleOperatorDao(ScaleOperatorDao scaleOperatorDao) {
        this.scaleOperatorDao = scaleOperatorDao;
    }

    @Override
    public List<Scale> getSimpleScales() throws QueryException {
        // TODO Auto-generated method stub
        return this.scaleOperatorDao.selectAllScales_();
    }

    @Override
    public List<KeyValue> getScaleTypes() {
        try {
            List<ArrayRow> list = this.scaleOperatorDao.selectScaleTypes_();
            List<KeyValue> result = new ArrayList<KeyValue>(list.size());
            Iterator<ArrayRow> iter = list.iterator();
            while (iter.hasNext()) {
                ArrayRow arrayRow = iter.next();
                Object[] values = arrayRow.getValues();
                result.add(new DefaultKeyValue(values[0], values[1]));
            }
            return result;
        } catch (PersistableException e) {
            logger.error(e.getMessage(), e);
            throw new QueryException("查询数据库selectScaleTypes_时，出现问题，请与系统管理员联系");
        }
    }

    @Override
    public void updateScaleFlag(Scale scale) {
        // TODO Auto-generated method stub
        try {
            this.scaleOperatorDao.updateScaleFlag(scale);
        } catch (PersistableException e) {
            throw new RuntimeException("更新量表数据库，出现异常");

        }
    }

    @Override
    public List<ModelEntity> getBlurScales(String keyword) throws QueryException {
        try {
            return this.scaleOperatorDao.selectAllScaleBlur_(keyword);
        } catch (PersistableException e) {
            logger.error(e.getMessage(), e);
            throw new QueryException("查询数据库时，出现问题，请与系统管理员联系");
        }
    }

    @Override
    public void appendScale(ScaleDto scale) {
        // TODO Auto-generated method stub
        try {
            this.scaleOperatorDao.insertScale_(scale);
        } finally {
        }
    }

    @Override
    public void insertPreWarnList(List<PreWarn> preWarns, long nextId) {
        // TODO Auto-generated method stub
        if (null != preWarns) {
            // 向数据库中的添加这些记录
            for (PreWarn preWarn : preWarns) {
                preWarn.setScaleid(nextId + "");
                insertPreWarn(preWarn);
            }
        }
    }

    @Override
    public void insertScoreGradeList(List<ScoreGrade> scoreGrades, long nextId) {
        // TODO Auto-generated method stub
        if (null != scoreGrades) {
            // 向数据库中的添加这些记录
            for (ScoreGrade scoreGrade : scoreGrades) {
                scoreGrade.setScaleid(nextId + "");
                insertScoreGrade(scoreGrade);
            }
        }
    }

    @Override
    public void appendDimExplainAndInstr(List<ScaleExplainObject> scaleExplainObjectList, long nextId) {
        // TODO Auto-generated method stub
        if (scaleExplainObjectList != null) {
            for (ScaleExplainObject seo : scaleExplainObjectList) {
                try {
                    seo.setScaleid(nextId + "");
                    this.scaleOperatorDao.insertScaledimexplain_(seo);
                } finally {
                }
            }
        }
    }

    @Override
    public void appendNormList(List<ScaleNormObject> normObjList, long nextId, int type, long orgid, long userid) {
        // TODO Auto-generated method stub
        if (normObjList != null) {
            for (ScaleNormObject sno : normObjList) {
                try {
                    sno.setScale_id(nextId + "");
                    sno.setType(type);
                    sno.setOrgid(orgid);
                    sno.setUserid(userid);
                    this.scaleOperatorDao.insertScaleNorm_(sno);
                } finally {
                }
            }
        }
    }

    @Override
    public int appendNormInfo(String scaleid, String normname, int type, Date createtime, long createuserid,
            String description, int areaid, long orgid, int orglevel, String editer, String edittime) {
        NormInfo norminfo = new NormInfo();
        norminfo.setAreaid(areaid);
        norminfo.setCreatetime(createtime);
        norminfo.setCreateuserid(createuserid);
        norminfo.setDescription(description);
        norminfo.setName(normname);
        norminfo.setOrgid(orgid);
        norminfo.setOrglevel(orglevel);
        if (ScaleUtils.isThirdAngleScaleP(scaleid) && type == 2) {// 自定义常模，三角视量表的只注册学生版
            scaleid = "111000001";
        }
        if (ScaleUtils.isThirdAngleScaleM(scaleid) && type == 2) {
            scaleid = "110100001";
        }
        norminfo.setScaleId(Integer.parseInt(scaleid));
        norminfo.setType(type);
        norminfo.setEditer(editer);
        norminfo.setEdittime(edittime);
        normInfoMapper.insert(norminfo);
        int id = norminfo.getId();
        if (type == 1)
            scalenormMapper.updateSystemScaleNormID(id, scaleid, type);
        else {
            if (ScaleUtils.isThirdAngleScaleP(scaleid)) {
                scalenormMapper.updateCustomThreeAngleScaleNormID(id, "111000001", "111000002", "111000003", type,
                        createuserid);
            } else if (ScaleUtils.isThirdAngleScaleM(scaleid)) {
                scalenormMapper.updateCustomThreeAngleScaleNormID(id, "110100001", "110100002", "110100003", type,
                        createuserid);
            } else
                scalenormMapper.updateCustomScaleNormID(id, scaleid, type, createuserid);
        }
        return id;

    }

    @Override
    public void appendNormCustomList(List<ScaleNormObject> normObjList, int scaleId, int orglevel, int areaid,
            long edituserid) {
        if (normObjList != null) {
            ScaleNormLog scaleNormLog = new ScaleNormLog();
            scaleNormLog.setScaleid(scaleId);
            scaleNormLog.setAreaid(areaid);
            scaleNormLog.setEditTime(new Date());
            scaleNormLog.setEdituserid((int) edituserid);
            scaleNormLogMapper.insert(scaleNormLog);
            for (ScaleNormObject sno : normObjList) {
                try {
                    sno.setScale_id(scaleId + "");
                    sno.setOrglevel(orglevel);
                    sno.setAreaid(areaid);
                    this.scaleOperatorDao.insertScaleNormCustom_(sno);
                } finally {
                }
            }
        }

    }

    @Override
    public void insertScoreGrade(ScoreGrade scoreGrade) {
        // TODO Auto-generated method stub
        this.scaleOperatorDao.insertScaleScoregrade_(scoreGrade);
    }

    @Override
    public void insertPreWarn(PreWarn preWarn) {
        // TODO Auto-generated method stub
        this.scaleOperatorDao.insertScalePrewarn_(preWarn);
    }

    @Override
    public void deleteScale(String scaleId) {
        // TODO Auto-generated method stub
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {
            this.scaleOperatorDao.deleteScale(scaleId);
            // this.scaleOperatorDao.deleteStudentExamdo(scaleId);
            // this.scaleOperatorDao.deleteTeacherExamdo(scaleId);
            // this.scaleOperatorDao.deleteStudentDimExam(scaleId);
            // this.scaleOperatorDao.deleteTeacherDimExam(scaleId);
            // this.scaleOperatorDao.deleteIndividuaExam(scaleId);
            // this.scaleOperatorDao.deleteStudentExam(scaleId);
            // this.scaleOperatorDao.deleteTeacherExam(scaleId);
            // 删除常模
            this.scaleOperatorDao.deleteScaleNorm(scaleId);

            this.normInfoMapper.deleteNormInfo(scaleId, 1);

            // 删除得分水平
            this.scaleOperatorDao.deleteScaleScoreGrade(scaleId);
            // 删除预警
            this.scaleOperatorDao.deleteScaleWarning(scaleId);
            // 删除解释
            this.scaleOperatorDao.deleteScaleExplain(scaleId);
            this.scaleOperatorDao.deleteScaleGrades(scaleId);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
        } finally {

        }
    }

    @Override
    public String getXmlStr(String scaleId) {
        return scaleOperatorDao.selectXmlStr(Integer.valueOf(scaleId));
    }

    @Override
    public int getScale(String scaleId) {
        return scaleOperatorDao.selectScale(Integer.valueOf(scaleId));
    }

    @Override
    public void updateXmlStr(String xmlStrl, String scaleId) {
        this.scaleOperatorDao.updateXmlStr(UtilMisc.toMap("xmlStr", xmlStrl, "scaleId", scaleId));
    }

    @Override
    public List<Map> getScaleCfgsByOrg(long orgId) {
        // TODO Auto-generated method stub
        return this.scaleOperatorDao.getScaleCfgsByOrg(orgId);
    }

    @Override
    public void insertScaleCfgOrg(Map<?, ?> parmap) {
        // TODO Auto-generated method stub
        this.scaleOperatorDao.insertScaleCfgOrg(parmap);
    }

    @Override
    public void updateScaleCfgOrg(Map<?, ?> parmap) {
        // TODO Auto-generated method stub
        this.scaleOperatorDao.updateScaleCfgOrg(parmap);
    }

    @Override
    public void updateTitleAlias(Map<?, ?> parmap) {
        // TODO Auto-generated method stub
        this.scaleOperatorDao.updateTitleAlias(parmap);
    }

    public Scale getScaleByCode(String code) {
        return scaleOperatorDao.getScaleByCode(code);
    };

}
