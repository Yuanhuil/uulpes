package com.njpes.www.entity.baseinfo;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.attr.PropObject;

public abstract class SchoolUser extends PropObject {

    protected String xm;
    protected String xbm;
    protected String csrq;
    protected String bjxx;

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXbm() {
        return xbm;
    }

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getBjxx() {
        return bjxx;
    }

    public void setBjxx(String bjxx) {
        this.bjxx = bjxx;
        // setBackGrandByStr();
    }

    public void setBackGrandByStr() {
        setBackGrand(this.bjxx);
    }

    @Override
    public Set<String> getExculdeAttr() {
        return DEAFAULT_EXCULDEATTRSET;
    }

    @Override
    protected int[] getCateIds() {
        return new int[] { 1 };
    }

    @Override
    protected void saveExcludeAttrs() throws SQLException {
        List<String> params = getExculudeParamVals();
        if (StringUtils.isEmpty(params.get(0)) | StringUtils.isEmpty(params.get(1))) {
            return;
        }
        Object paramObjs[] = new Object[params.size()];
        params.toArray(paramObjs);
        paramObjs = ArrayUtils.add(paramObjs, getId());
    }

    @Override
    protected void loadProps(String bjxx) throws SQLException {
        // TODO Auto-generated method stub
        setPropsByStr(bjxx);
        this.setProp(Constants.NAME_KEY, getXm());
        this.setProp(Constants.GENDER_KEY, getXbm());
        this.setProp(Constants.BIRTHDAY_KEY, getCsrq());
    }
}
