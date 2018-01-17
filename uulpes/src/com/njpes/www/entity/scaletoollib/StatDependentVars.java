package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;
import java.util.List;

import edutec.scale.model.Scale;
import net.sf.json.JSONArray;

public class StatDependentVars implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Scale scale;
    JSONArray dims;
    Integer normId;
    List<Scalenorm> norm;
    Integer taskid;

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public Scale getScaleCode() {
        return scale;
    }

    public void setScaleCode(Scale scale) {
        this.scale = scale;
    }

    public JSONArray getDims() {
        return dims;
    }

    public void setDims(JSONArray dims) {
        this.dims = dims;
    }

    public List<Scalenorm> getNorm() {
        return norm;
    }

    public void setNorm(List<Scalenorm> norm) {
        this.norm = norm;
    }

    public void setNormId(Integer normId) {
        this.normId = normId;
    }

    public Integer getNormId() {
        return this.normId;
    }
}
