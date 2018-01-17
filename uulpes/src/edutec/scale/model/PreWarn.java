package edutec.scale.model;

import java.io.Serializable;

public class PreWarn implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6083792090291744445L;
    String scaleid;
    String wid; // 维度id
    int wlevel; // 维度级别
    int scoreMethod; // 计分方法，z值或原始分
    String w1; // 预警三个值
    String w2;
    String w3;

    public String getScaleid() {
        return scaleid;
    }

    public void setScaleid(String scaleid) {
        this.scaleid = scaleid;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public int getWlevel() {
        return wlevel;
    }

    public void setWlevel(int wlevel) {
        this.wlevel = wlevel;
    }

    public int getScoreMethod() {
        return scoreMethod;
    }

    public void setScoreMethod(int scoreMethod) {
        this.scoreMethod = scoreMethod;
    }

    public String getW1() {
        return w1;
    }

    public void setW1(String w1) {
        this.w1 = w1;
    }

    public String getW2() {
        return w2;
    }

    public void setW2(String w2) {
        this.w2 = w2;
    }

    public String getW3() {
        return w3;
    }

    public void setW3(String w3) {
        this.w3 = w3;
    }
}
