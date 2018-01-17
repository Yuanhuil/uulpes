package edutec.scale.model;

import java.io.Serializable;

public class ScaleNormObject implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4528927304897212712L;
    private String scale_id;
    private String w_id;
    private int grade_id;
    // 特殊量表中需要年龄阶段
    private String ages = null;
    private int agemin;
    private int agemax;
    private int gender;
    private int type; // 量表类型，1-系统常模，2-自定义常模
    private double m;
    private double sd;

    private int orglevel;
    private int areaid;
    private long orgid;
    private long userid;

    public String getScale_id() {
        return scale_id;
    }

    public void setScale_id(String scale_id) {
        this.scale_id = scale_id;
    }

    public String getW_id() {
        return w_id;
    }

    public void setW_id(String w_id) {
        this.w_id = w_id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getSd() {
        return sd;
    }

    public void setSd(double sd) {
        this.sd = sd;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getAges() {
        return ages;
    }

    public void setAges(String ages) {
        this.ages = ages;
    }

    public int getAgemin() {
        return agemin;
    }

    public void setAgemin(int agemin) {
        this.agemin = agemin;
    }

    public int getAgemax() {
        return agemax;
    }

    public void setAgemax(int agemax) {
        this.agemax = agemax;
    }

    public int getOrglevel() {
        return orglevel;
    }

    public void setOrglevel(int orglevel) {
        this.orglevel = orglevel;
    }

    public int getAreaid() {
        return areaid;
    }

    public void setAreaid(int areaid) {
        this.areaid = areaid;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

}
