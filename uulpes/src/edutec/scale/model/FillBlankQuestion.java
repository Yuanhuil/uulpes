package edutec.scale.model;

import java.io.Serializable;

public class FillBlankQuestion extends Question implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2630658867967670222L;

    public FillBlankQuestion() {
        this.setType(QuestionConsts.TYPE_FILLBLANK);
    }

    public String getCalcExp() {
        throw new UnsupportedOperationException();
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
