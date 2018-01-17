package edutec.scale.explain.group;

public class WarningStat {
    // private String wLevel;//预警级别
    // private String gLevel;//得分水平
    // private String xb;//性别
    private int amount;// 检出数
    private int maleNum;
    private int femaleNum;

    public WarningStat() {
        amount = 0;
        maleNum = 0;
        femaleNum = 0;
    }

    // public String getwLevel() {
    // return wLevel;
    // }
    // public void setwLevel(String wLevel) {
    // this.wLevel = wLevel;
    // }
    // public String getXb() {
    // return xb;
    // }
    // public void setXb(String xb) {
    // this.xb = xb;
    // }
    public int getAmount() {
        return maleNum + femaleNum;
    }

    // public String getgLevel() {
    // return gLevel;
    // }
    // public void setgLevel(String gLevel) {
    // this.gLevel = gLevel;
    // }
    // public void incWarningStat(){
    // amount++;
    // }
    public void incMaleNum() {
        maleNum++;
    }

    public void incFemaleNum() {
        femaleNum++;
    }

    public int getMaleNum() {
        return maleNum;
    }

    public int getFemaleNum() {
        return femaleNum;
    }

}
