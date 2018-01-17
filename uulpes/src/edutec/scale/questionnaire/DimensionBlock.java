package edutec.scale.questionnaire;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edutec.scale.exception.CalcException;
import edutec.scale.exception.DimensionException;
import edutec.scale.model.Dimension;
import edutec.scale.model.Formula;
import edutec.scale.model.Question;

public class DimensionBlock {
    /**
     * 所代理的维度
     */
    private final Dimension dimension;
    /**
     * 所属的问卷
     */
    private final Questionnaire questionnaire;
    /**
     * 父亲维度段
     */
    private DimensionBlock parenDimensionClock;
    /**
     * 子维度
     */
    private List<DimensionBlock> subdimensionBlockList = null;
    /**
     * 如果没有子维度，则包含问题
     */
    private List<QuestionBlock> questionBlockList = null;

    /**
     * 原始分
     */
    private Number rawScore;
    /**
     * 标准分即Z分数
     */
    private Number stdScore;

    private int questionNum;// 本维度题目总数（报告下属各子维度的题目总数之和）
    private Number sumScore;// 总分，用于计算非子维度的平均分

    /**
     * 最终维度分数,及T分数；由于维度分有可能经过多次运算,而每次的计算都可能被将来的时候使用<br>
     * 一次粗分,一次标准分,或再一个计算 最终的分数放在这里
     */
    private Number finalScore;// T分数

    private Number tScore;// T分数

    private int warningGrade;// 告警等级

    private int scoreGrade;// 得分等级
    /**
     * 此维度是否已经完成计算
     */

    private short completeFlag;

    private int rank;

    public final short COMPLETE_RAW = 1 << 0;
    public final short COMPLETE_STD = 1 << 1;

    public DimensionBlock(final Dimension dimension, final Questionnaire questionnaire) {
        this.dimension = dimension;
        this.questionnaire = questionnaire;
    }

    public void mount() {
        if (!dimension.isLeaf()) {
            mountDimensions();
        } else {
            mountQuestions();
        }
    }

    /**
     * 组装具有子维度的维度
     */
    private void mountDimensions() {
        if (subdimensionBlockList == null) {
            subdimensionBlockList = new ArrayList<DimensionBlock>();
        }
        subdimensionBlockList.clear();
        DimensionBlock dimBlk = null;
        List<Dimension> dims = dimension.getSubdimensions();
        if (dims != null) {
            for (Dimension dim : dims) {
                dimBlk = questionnaire.findDimensionBlock(dim.getId());
                dimBlk.setParenDimensionBlock(this);
                subdimensionBlockList.add(dimBlk);
            }
        }
    }

    /**
     * 组装具有问题的维度，并建立问题和维度的关系
     */
    private void mountQuestions() {
        if (questionBlockList == null) {
            questionBlockList = new ArrayList<QuestionBlock>();
        }
        questionBlockList.clear();
        QuestionBlock quesBlk;
        List<Question> questions = dimension.getQuestionList();
        if (questions != null) {
            for (Question q : questions) {
                quesBlk = questionnaire.findQuestionBlock(q.getId());
                quesBlk.setDimensionBlock(this);
                questionBlockList.add(quesBlk);
            }
        }
    }

    public Dimension getDimension() {
        return dimension;
    }

    public DimensionBlock getParenDimensionBlock() {
        return parenDimensionClock;
    }

    public void setParenDimensionBlock(DimensionBlock parenDimensionSegment) {
        this.parenDimensionClock = parenDimensionSegment;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public List<QuestionBlock> getQuestionBlockList() {
        return questionBlockList;
    }

    public List<DimensionBlock> getSubdimensionBlkList() {
        return subdimensionBlockList;
    }

    public String getDescn() {
        return StringUtils.trimToEmpty(dimension.getDescn());
    }

    public Formula getFormula() {
        return dimension.getFormula();
    }

    public String getId() {
        return dimension.getId();
    }

    public boolean isInterior() {
        return dimension.isInterior();
    }

    public boolean isLeaf() {
        return dimension.isLeaf();
    }

    public boolean isRoot() {
        return dimension.isRoot();
    }

    public Number getRawScore() throws DimensionException {
        // if (!isCompleteRawCalc())
        // throw new
        // DimensionException(questionnaire.getScale().getTitle()+":维度:"
        // +this.getTitle()+":"+ this.getId() + ":没有完成粗分计算.");
        return rawScore;
    }

    public void setRawScore(Number rawScore) {
        this.rawScore = rawScore;
    }

    public Number getStdScore() throws DimensionException {
        // if (!isCompleteStdCalc())
        // throw new DimensionException("维度" + this.getId() + ":没有完成标准分计算.");
        return stdScore;
    }

    public void setStdScore(Number stdScore) {
        this.stdScore = stdScore;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public Number getSumScore() {
        return sumScore;
    }

    public void setSumScore(Number sumScore) {
        this.sumScore = sumScore;
    }

    public void completeStdCalc() {
        completeFlag |= COMPLETE_STD;
    }

    public void completeRawCalc() {
        completeFlag |= COMPLETE_RAW;
    }

    public void clearCompleteFlag() {
        completeFlag &= ~COMPLETE_RAW;
        completeFlag &= ~COMPLETE_STD;
    }

    public boolean isCompleteRawCalc() {
        if (getFormula() == null) {
            return true;
        }
        return (completeFlag & COMPLETE_RAW) != 0;
    }

    public boolean isCompleteStdCalc() {
        if (getFormula() == null || getFormula().getStdexp() == null) {
            return true;
        }
        return (completeFlag & COMPLETE_STD) != 0;
    }

    public boolean isCompleteStdCalc1() {
        return (completeFlag & COMPLETE_STD) != 0;
    }

    public boolean isCompleteCalc() {
        return isCompleteRawCalc() && isCompleteStdCalc1();// 赵万锋修改
    }

    public String getTitle() {
        return dimension.getTitle();
    }

    public QuestionBlock findQuestionBlock(String questionId) {
        return questionnaire.findQuestionBlock(questionId);
    }

    public Number findQuestionScore(String questionId) throws CalcException {
        return questionnaire.findQuestionScore(questionId);
    }

    public int getConstrain() {
        return dimension.getConstrain();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Number getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Number dimStdScore) {
        this.finalScore = dimStdScore;
        // this.tScore = dimStdScore;
    }

    public Number getTScore() {
        if (tScore == null)
            try {
                return getRawScore();
            } catch (DimensionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return tScore;

    }

    public void setTScore(Number tScore) {
        this.tScore = tScore;
    }

    public Number getAScore() {
        Number score;
        try {
            score = getFinalScore();
            if (score == null)
                score = getStdScore();
            if (score == null)
                score = getRawScore();
            return score;
        } catch (DimensionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Number getScore() {
        Number score;
        try {
            score = getTScore();
            if (score == null)
                score = getStdScore();
            if (score == null)
                score = getRawScore();
            return score;
        } catch (DimensionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getWarningGrade() {
        return warningGrade;
    }

    public void setWarningGrade(int warningGrade) {
        this.warningGrade = warningGrade;
    }

    public void setScoreGrade(int scoreGrade) {
        this.scoreGrade = scoreGrade;
    }

    public int getScoreGrade() {
        return scoreGrade;
    }

}
