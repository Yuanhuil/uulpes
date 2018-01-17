package edutec.scale.descriptor;

public class Score {
    private Number rawScore;
    private Number stdScore;
    private Number tScore;

    public Number getRawScore() {
        return rawScore;
    }

    public void setRawScore(Number rawScore) {
        this.rawScore = rawScore;
    }

    public Number getStdScore() {
        return stdScore;
    }

    public void setStdScore(Number stdScore) {
        this.stdScore = stdScore;
    }

    public Number gettScore() {
        return tScore;
    }

    public void settScore(Number tScore) {
        this.tScore = tScore;
    }

}
