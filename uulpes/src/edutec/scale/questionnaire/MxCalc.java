package edutec.scale.questionnaire;

import java.util.HashMap;
import java.util.Map;

import edutec.scale.exception.CalcException;
import edutec.scale.model.MatrixQuestion;
import heracles.util.ArrayIntClosure;
import heracles.util.UtilCollection;

public class MxCalc extends QuestionCalc {
    private MatrixQuestion.MxInfo mxInfo;

    @Override
    protected void setBlock(QuestionBlock block) {
        super.setBlock(block);
        MatrixQuestion mQ = (MatrixQuestion) block.getQuestion();
        mxInfo = mQ.getMxInfo();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected double evaluateArithmecticExp(String cmputor) {
        ForArithExpClosure closure = new ForArithExpClosure();
        UtilCollection.forEach(getSelectedIdx(), closure);
        Map map = (Map) closure.getObject();
        String str = UtilCollection.substitutStr(cmputor, map);
        try {
            return jexlEvaluate(str);
        } catch (CalcException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected double[] getVals() {
        ForGetValsClosure closure = new ForGetValsClosure();
        UtilCollection.forEach(getSelectedIdx(), closure);
        return (double[]) closure.getObject();
    }

    private int[] getSelectedIdx() {
        String answer = block.getAnswer();
        int selectedIdx[] = UtilCollection.toIntArray(answer);
        return selectedIdx;
    }

    private class ForGetValsClosure extends ArrayIntClosure {
        private double vals[];

        @Override
        public void setArrayLength(int arrayLength) {
            super.setArrayLength(arrayLength);
            if (vals == null) {
                vals = new double[arrayLength];
            }
        }

        public void execute(int input) {
            vals[getIndex()] = mxInfo.score(input);
        }

        public Object getObject() {
            return vals;
        }
    }

    private class ForArithExpClosure extends ArrayIntClosure {
        HashMap<String, String> valueMap;

        public void setArrayLength(int arrayLength) {
            super.setArrayLength(arrayLength);
            if (valueMap == null) {
                valueMap = new HashMap<String, String>(arrayLength);
            }
        }

        public void execute(int input) {
            valueMap.put(String.valueOf(getIndex()), String.valueOf(input));
        }

        public Object getObject() {
            return valueMap;
        }
    }

    @Override
    protected double evaluateDesn() {
        return 0;
    }

}
