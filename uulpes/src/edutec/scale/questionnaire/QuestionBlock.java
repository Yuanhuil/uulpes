package edutec.scale.questionnaire;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import edutec.scale.exception.CalcException;
import edutec.scale.exception.QuestionException;
import edutec.scale.model.Option;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.SelectionQuestion;

public class QuestionBlock {
    /* 题目位置 */
    public static final byte POS_FIRST = 1;
    public static final byte POS_MIDDLE = 2;
    public static final byte POS_LAST = 3;

    private byte positionFlag = POS_MIDDLE;

    private final Question question;
    private String answer;
    private DimensionBlock dimensionBlock;
    private String subjoin;
    private Number score;
    private Questionnaire questionnaire;
    // private String displayId;

    public QuestionBlock(final Question question) {
        this.question = question;
    }

    public QuestionBlock(final Question question, Questionnaire questionnaire) {
        this(question);
        this.setQuestionnaire(questionnaire);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
        this.score = null;
    }

    public boolean isFinish() {
        return StringUtils.isNotBlank(answer);
    }

    /**
     * 得到答题的分数，如果反向积分，取反的索引
     * 
     * @return
     * @throws QuestionException
     */
    public Number getScore() throws CalcException {
        if (score == null) {
            Calc calc = QuestionCalc.newInstance(this);
            if (calc == null) {
                score = 0;
            } else {
                score = calc.evaluate();
            }
        }
        return score;
    }

    public String getAnswerValue() {
        if (getQuestion().getTypeMode() == QuestionConsts.TYPE_SELECTION_MODE) {
            String answer = getAnswer();
            int selectedIdx = NumberUtils.toInt(answer);
            SelectionQuestion sQ = (SelectionQuestion) getQuestion();
            if (sQ.getChoiceMode() == QuestionConsts.CHOICE_MULTI_MODE) {// 如果的多选题，那么得分就是值本身（选一个得一分）added
                                                                         // by
                                                                         // zhaowanfeng
                return answer;
            }
            Option option = null;
            option = sQ.findOption(selectedIdx);
            return option.getValue();
        }
        return null;
    }

    public Question getQuestion() {
        return question;
    }

    public DimensionBlock getDimensionBlock() {
        return dimensionBlock;
    }

    public void setDimensionBlock(DimensionBlock dimensionBlock) {
        this.dimensionBlock = dimensionBlock;
    }

    public String getId() {
        return question.getId();
    }

    public String getDisplayId() {
        if (getQuestion().isPlaceholder() || getQuestion().isTemplate())
            return null;
        // if(getAnswer()==null)return null;
        String id = getId();
        String qId = id;
        if (id.contains("_")) {
            String[] temp = id.split("_");
            qId = temp[0];
        }
        if (id.contains(".")) {// 矩阵题连接由"-"改成".",否则不好写公式。赵万锋
            String[] temp = id.split(".");
            qId = temp[0];
        }
        return qId;

    }

    public boolean isMustAnswer() {
        return question.isMustAnswer();
    }

    public String getType() {
        return question.getType();
    }

    public byte getPositionFlag() {
        return positionFlag;
    }

    public void setPositionFlag(byte positionFlag) {
        this.positionFlag = positionFlag;
    }

    public String getSubjoin() {
        return subjoin;
    }

    public void setSubjoin(String subjoin) {
        this.subjoin = subjoin;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public void setScore(Number score) {
        this.score = score;
    }

}
