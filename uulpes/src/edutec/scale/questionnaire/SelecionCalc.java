package edutec.scale.questionnaire;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import edutec.scale.exception.CalcException;
import edutec.scale.model.Option;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.SelectionQuestion;
import heracles.util.UtilCollection;

public class SelecionCalc extends QuestionCalc {

    /**
     * 计算数学表达式 形如:${1} + ${2} + ${4} 数字是选项索引
     */
    @Override
    protected double evaluateArithmecticExp(String cmputor) {
        String answer = block.getAnswer();
        int selectedIdx[] = UtilCollection.toIntArray(answer);
        SelectionQuestion sQ = (SelectionQuestion) block.getQuestion();
        Option option = null;
        HashMap<String, String> valueMap = new HashMap<String, String>(selectedIdx.length);
        for (int i = 0; i < selectedIdx.length; ++i) {
            int position = sQ.isReverse() ? (sQ.optionSize() - 1 - selectedIdx[i]) : selectedIdx[i];
            option = sQ.findOption(position);
            valueMap.put(String.valueOf(position), option.getValue());
        }
        String str = UtilCollection.substitutStr(cmputor, valueMap);
        try {
            return jexlEvaluate(str);
        } catch (CalcException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected double[] getVals() {
        try {
            String answer = block.getAnswer();
            int selectedIdx[] = UtilCollection.toIntArray(answer);
            SelectionQuestion sQ = (SelectionQuestion) block.getQuestion();
            Option option = null;
            double vals[] = new double[selectedIdx.length];
            for (int i = 0; i < selectedIdx.length; ++i) {
                // int position = sQ.isReverse() ? (sQ.optionSize() - 1 -
                // selectedIdx[i]) : selectedIdx[i];
                int position = selectedIdx[i];// 导入量表时已经将反向题值进行了反向，这里不再需要反向
                option = sQ.findOption(position);
                vals[i] = Double.parseDouble(option.getValue());
            }
            return vals;
        } catch (Exception ex) {
            return new double[] { 0D };
        }
    }

    /**
     * 处理：1）带有正确答案的；2）使用其它特殊的计算类来计算本题分数
     */
    @Override
    protected double evaluateDesn() {
        /* 如果选择的答案正确，则得一分 */
        Map<String, String> props = block.getQuestion().getDescnProps();
        String correct = props.get(QuestionConsts.DESCN_CALC_CORRECT_KEY);
        String clazz = props.get(QuestionConsts.DESCN_CALC_CLASS_KEY);

        if (StringUtils.isNotEmpty(correct)) {
            String answeridx = block.getAnswer(); // option索引值
            if (correct.equals(answeridx)) {
                String score = props.get(QuestionConsts.DESCN_SCORE_KEY);
                if (StringUtils.isNotEmpty(score)) {
                    return NumberUtils.toDouble(score);
                } else {
                    return 1;
                }
            }
        } else if (StringUtils.isNotEmpty(clazz)) {
            QuestionCalc calc = createCalc(clazz);
            try {
                calc.setBlock(getBlock());
                return calc.evaluate().doubleValue();
            } catch (CalcException e) {
                e.printStackTrace();
            }
        }
        return 0;

    }

}
