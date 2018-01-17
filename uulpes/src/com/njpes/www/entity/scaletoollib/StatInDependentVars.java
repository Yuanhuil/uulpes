package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;

import net.sf.json.JSONArray;

public class StatInDependentVars implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String tableName;
    JSONArray cols;
    JSONArray vals;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public JSONArray getCols() {
        return cols;
    }

    public void setCols(JSONArray cols) {
        this.cols = cols;
    }

    public JSONArray getVals() {
        return vals;
    }

    public void setVals(JSONArray vals) {
        this.vals = vals;
    }
}
