package edutec.scale.model;

import java.io.Serializable;

/**
 * @author huangc 结果解释，包括学生、家长、老师
 */
public class ScaleExplainObject implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8092132090893405716L;
    // 量表id
    private String scaleid;
    // 维度id
    private String dimensionId;
    // 得分等级
    private int dgrade;
    // 维度级别
    private int wlevel;
    // 解释中的第一行
    private String firstStr;
    // 解释中的第二行，也就是其他
    private String otherStr;
    // 该量表的建议
    private String advice;
    // 标志位用来判断是学生，家长，还是老师
    private int typeFlag;

    public String getScaleid() {
        return scaleid;
    }

    public void setScaleid(String scaleid) {
        this.scaleid = scaleid;
    }

    public String getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
    }

    public int getDgrade() {
        return dgrade;
    }

    public void setDgrade(int dgrade) {
        this.dgrade = dgrade;
    }

    public String getFirstStr() {
        return firstStr;
    }

    public void setFirstStr(String firstStr) {
        this.firstStr = firstStr;
    }

    public String getOtherStr() {
        return otherStr;
    }

    public void setOtherStr(String otherStr) {
        this.otherStr = otherStr;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public int getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(int typeFlag) {
        this.typeFlag = typeFlag;
    }

    public int getWlevel() {
        return wlevel;
    }

    public void setWlevel(int wlevel) {
        this.wlevel = wlevel;
    }
}
