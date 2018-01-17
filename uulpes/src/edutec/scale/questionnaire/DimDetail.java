package edutec.scale.questionnaire;

import java.util.List;

import edutec.scale.exception.DimensionException;
import heracles.util.Arith;

public class DimDetail {
    @Override
    public boolean equals(Object obj) {
        DimDetail other = (DimDetail) obj;
        return dimBlk.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return dimBlk.getId().hashCode();
    }

    final private DimensionBlock dimBlk;
    private String dimDescn;// 详细说明
    private String dimDescn1;// 说明的第一句
    private String dimAdvice;
    private Object dimFinalScore;
    private double zScore;
    private double tScore;

    public int getRank() {
        return dimBlk.getRank();
    }

    public void setRank(int rank) {
        dimBlk.setRank(rank);
    }

    public DimDetail(final DimensionBlock dimBlk) {
        super();
        this.dimBlk = dimBlk;
    }

    public String getDimDescn() {
        return dimDescn;
    }

    public void setDimDescn(String dimDesn) {
        this.dimDescn = dimDesn;

    }

    public String getDimDescn1() {
        return dimDescn1;
    }

    public void setDimDescn1(String dimDescn1) {
        this.dimDescn1 = dimDescn1;
    }

    public String getDimAdvice() {
        return dimAdvice;
    }

    public void setDimAdvice(String dimAdvice) {
        this.dimAdvice = dimAdvice;
    }

    /**
     * getDimStdScore取分顺序,如果没有继续取分,直到取到为止 取值过程： dimStdScore-stdscore-rawscore
     * 
     * @return
     */
    public Object getDimFinalScore() {
        if (dimFinalScore == null) {
            return getDimScore();
        }
        if (dimFinalScore != null) {
            if (dimFinalScore instanceof Double)
                dimFinalScore = Arith.round((Double) dimFinalScore, 2);
            else {
            }
        }
        return dimFinalScore;
    }

    public void setDimFinalScore(Object dimValue) {
        if (dimValue == null)
            return;
        this.dimFinalScore = dimValue;
        if (dimValue instanceof Number) {
            dimBlk.setFinalScore((Number) dimValue);
        }
    }

    public double getzScore() {
        return zScore;
    }

    public void setzScore(double zScore) {
        this.zScore = zScore;
        dimBlk.setStdScore(zScore);
    }

    public double gettScore() {
        return tScore;
    }

    public void settScore(double tScore) {
        this.tScore = tScore;
        dimBlk.setTScore(tScore);
    }

    public String getId() {
        return dimBlk.getId();
    }

    public String getTitle() {
        return dimBlk.getTitle();
    }

    public Number getDimScore() {
        Number score;
        try {
            score = dimBlk.getStdScore();
            if (score == null)
                score = dimBlk.getRawScore();
            return score;
        } catch (DimensionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getDescn() {
        return dimBlk.getDescn();
    }

    public Number getRawScore() {
        try {
            return dimBlk.getRawScore();
        } catch (DimensionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Number getStdScore() {
        Number score;
        try {
            score = dimBlk.getStdScore();
            // if (score == null)
            // score = dimBlk.getRawScore();
            return score;
        } catch (DimensionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getConstrain() {
        return dimBlk.getConstrain();
    }

    public List<QuestionBlock> getQuestionBlockList() {
        return dimBlk.getQuestionBlockList();
    }

    public DimensionBlock getDimBlk() {
        return dimBlk;
    }

    public Number getTScore() {
        Number score;
        // try {
        score = dimBlk.getTScore();
        // if (score == null)
        // score = dimBlk.getStdScore();
        // if (score == null)
        // score = dimBlk.getRawScore();
        return score;
        // } //catch (DimensionException e) {
        // throw new RuntimeException(e.getMessage());
        // }
    }

}