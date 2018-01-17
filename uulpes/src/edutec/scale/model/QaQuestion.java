package edutec.scale.model;

import java.io.Serializable;

public class QaQuestion extends Question implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7079446654170861551L;
    private int cols;
    private int rows;

    public QaQuestion() {
        this.setType(QuestionConsts.TYPE_QA);
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getCalcExp() {
        throw new UnsupportedOperationException("目前问答题没有计算公式");
    }

    @Override
    public String toHTML() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toAnswerHtml(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toHeadtitleHTML() {
        // TODO Auto-generated method stub
        return null;
    }
}
