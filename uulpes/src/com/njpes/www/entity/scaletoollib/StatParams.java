package com.njpes.www.entity.scaletoollib;

import java.io.Serializable;
import java.util.List;

/*******************************************************************************
 * 此类解析来自客户所定义的人群查询条件 <br>
 * 
 * @author Shibin
 */

public class StatParams implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public int statObj; // 统计对象，学生、老师
    public StatScope scope; // 统计人群
    public int method; // 统计方法
    public List<StatInDependentVars> indpVars; // 自变量
    public List<StatDependentVars> depVars; // 因变量

    public List<StatInDependentVars> getIndpVars() {
        return indpVars;
    }

    public void setIndpVars(List<StatInDependentVars> indpVars) {
        this.indpVars = indpVars;
    }

    public List<StatDependentVars> getDepVars() {
        return depVars;
    }

    public void setDepVars(List<StatDependentVars> depVars) {
        this.depVars = depVars;
    }

    /**
     * 获得统计对象
     * 
     * @return
     */
    public int getStatObj() {
        return statObj;
    }

    public void setStatObj(int statObj) {
        this.statObj = statObj;
    }

    /**
     * 获得统计人群
     * 
     * @return
     */
    public StatScope getScope() {
        return scope;
    }

    public void setScope(StatScope scope) {
        this.scope = scope;
    }

    /**
     * 获得统计方法
     * 
     * @return
     */
    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

}
