package edutec.scale.model;

import java.io.Serializable;

public class ScoreGrade implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6558898873194476851L;
    String scaleid;
    String wid; // 维度id
    int wlevel; // 维度级别
    int scoreMethod; // 计分方法，z值或原始分
    String score1; // 得分水平的三个数值
    String score2;
    String score3;
    String score4;

    public String getScaleid() {
        return scaleid;
    }

    public void setScaleid(String scaleid) {
        this.scaleid = scaleid;
    }

    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public String getScore3() {
        return score3;
    }

    public void setScore3(String score3) {
        this.score3 = score3;
    }

    public String getScore4() {
        return score4;
    }

    public void setScore4(String score4) {
        this.score4 = score4;
    }

    public int getScoreMethod() {
        return scoreMethod;
    }

    public void setScoreMethod(int scoreMethod) {
        this.scoreMethod = scoreMethod;
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

}
