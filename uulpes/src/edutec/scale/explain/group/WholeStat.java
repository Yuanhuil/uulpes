package edutec.scale.explain.group;

public class WholeStat {
    int nTotal;// 总计
    int nMaleTotal;// 总计男
    int nFemaleTotal;// 总计女
    int nValid;// 有效总计
    int nValidMale;// 有效男
    int nValidFemale;// 有效女
    int numOfInvalid;

    public int getnTotal() {
        return nMaleTotal + nFemaleTotal;
    }

    public void setnTotal(int nTotal) {
        this.nTotal = nTotal;
    }

    public int getnMaleTotal() {
        return nMaleTotal;
    }

    public void setnMaleTotal(int nMaleTotal) {
        this.nMaleTotal = nMaleTotal;
    }

    public int getnFemaleTotal() {
        return nFemaleTotal;
    }

    public void setnFemaleTotal(int nFemaleTotal) {
        this.nFemaleTotal = nFemaleTotal;
    }

    public int getnValid() {
        return nValidMale + nValidFemale;
    }

    public void setnValid(int nValid) {
        this.nValid = nValid;
    }

    public int getnValidMale() {
        return nValidMale;
    }

    public void setnValidMale(int nValidMale) {
        this.nValidMale = nValidMale;
    }

    public int getnValidFemale() {
        return nValidFemale;
    }

    public void setnValidFemale(int nValidFemale) {
        this.nValidFemale = nValidFemale;
    }

    public int getNumOfInvalid() {
        return numOfInvalid;
    }

    public void setNumOfInvalid(int numOfInvalid) {
        this.numOfInvalid = numOfInvalid;
    }

    public void incNValidMale() {
        ++nValidMale;
    }

    public void incNValidFemale() {
        ++nValidFemale;
    }

    public void incNInvalid() {
        ++numOfInvalid;
    }
}
