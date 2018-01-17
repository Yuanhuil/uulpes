package edutec.group.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 产权量表的常模
 * 
 * @author 王文
 */
public class PropNorm {
    private int scaleId;
    private String wid;
    private int gradeOrderId;
    // private int age;
    private int gender;

    private float mean;
    private float sdv;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public float getSdv() {
        return sdv;
    }

    public void setSdv(float sdv) {
        this.sdv = sdv;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public int getScaleId() {
        return scaleId;
    }

    public void setScaleId(int scaleId) {
        this.scaleId = scaleId;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }
    // public int getAge() {
    // return age;
    // }
    // public void setAge(int age) {
    // this.age = age;
    // }

    public int getGradeOrderId() {
        return gradeOrderId;
    }

    public void setGradeOrderId(int gradeOrderId) {
        this.gradeOrderId = gradeOrderId;
    }
    //
}