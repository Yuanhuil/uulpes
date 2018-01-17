package edutec.scale.questionnaire;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.stat.StatUtils;

import edutec.scale.exception.CalcException;
import edutec.scale.model.Calculable;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.SelectionQuestion;
import heracles.util.Arith;
import heracles.util.Resources;
import heracles.util.UtilCollection;

public abstract class QuestionCalc implements Calc {
    final static Log logger = LogFactory.getLog(QuestionCalc.class);

    protected QuestionBlock block;

    public static Calc newInstance(QuestionBlock block) {
        Validate.isTrue(block.getQuestion() instanceof Calculable, "不是Calculable类型的对象");
        QuestionCalc cale = null;
        if (block.getQuestion().isTemplate()) {
            cale = new MuiltQuestionCalc();
        } else {
            if (block.getQuestion().getTypeMode() == QuestionConsts.TYPE_SELECTION_MODE) {
                cale = new SelecionCalc();
            } else if (block.getQuestion().getTypeMode() == QuestionConsts.TYPE_MATRIX_MODE) {
                cale = new MxCalc();
            }
        }
        if (cale != null) {
            cale.setBlock(block);
        }
        return cale;
    }

    public Number evaluate() throws CalcException {
        // 当为-1时，用户没有选择答案，按0分处理
        String exp = block.getQuestion().getCalcExp();
        String answer = block.getAnswer();
        if (answer == null) {
            logger.error(block.getQuestion().getId() + ":answer is null");
            return 0;
        }
        if (answer.equals("-1")) {
            return 0;
        }
        if (block.getQuestion().getDescn() != null && block.getQuestion().getDescn().startsWith("calc.")) {
            return evaluateDesn();
        }
        if (StringUtils.isNotBlank(exp)) {
            if (isFunction(exp)) {
                return evaluateFunctionExp(exp);
            } else if (isStmtBlock(exp)) {
                try {
                    return evaluateStmtBlock(exp);
                } catch (CalcException e) {
                    logger.debug(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            } else if (isArithExp(exp)) {
                return evaluateArithmecticExp(exp);
            } else {
                return 0D;
            }
        } else {
            if (block.getQuestion().getTypeMode() == QuestionConsts.TYPE_SELECTION_MODE) {
                SelectionQuestion sQ = (SelectionQuestion) block.getQuestion();
                if (sQ.getChoiceMode() == QuestionConsts.CHOICE_SINGLE_MODE)// 单选题
                    return this.getVals()[0]; /* 没有计算式子，表示只有一个值 */
                if (sQ.getChoiceMode() == QuestionConsts.CHOICE_MULTI_MODE)// 多选题，分数之和
                {
                    double sum = 0.0;
                    double[] values = this.getVals();
                    for (int i = 0; i < values.length; i++) {
                        sum = sum + values[i];
                    }
                    return sum;
                }
            }
            return this.getVals()[0]; /* 没有计算式子，表示只有一个值 */
        }
    }

    public QuestionBlock getBlock() {
        return block;
    }

    protected void setBlock(QuestionBlock block) {
        this.block = block;
    }

    abstract protected double evaluateDesn();

    abstract protected double evaluateArithmecticExp(String cmputor);

    abstract protected double[] getVals();

    public static double jexlEvaluate(String stmt) throws CalcException {
        Expression e;
        try {
            e = ExpressionFactory.createExpression(stmt);
            return Arith.round(Double.parseDouble(e.evaluate(null).toString()), 2);
        } catch (Exception ex) {
            throw new CalcException(ex.getMessage());
        }

    }

    protected double evaluateStmtBlock(String cmputor) throws CalcException {
        StrBuilder var = new StrBuilder(), val = new StrBuilder();
        Map<String, String> smap = new HashMap<String, String>();
        int idx = 0;
        int state = 0;
        int length = cmputor.length();
        for (; idx < length; ++idx) {
            char c = cmputor.charAt(idx);
            if (c == OPEN_BLOCK)
                break;
        }
        for (++idx; idx < length; ++idx) {
            char c = cmputor.charAt(idx);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c == EQ) {
                state = 1;
                continue;
            }
            if (c == STMT_SEPARATOR) {
                smap.put(var.toString(), val.toString());
                var.clear();
                val.clear();
                state = 0;
            }
            if (c == VARS_PROC_GAP) {
                break;
            }
            switch (state) {
            case 0:
                var.append(c);
                break;
            case 1:
                val.append(c);
                break;
            }
        }
        Map<String, Double> vmap = new HashMap<String, Double>();
        for (Map.Entry<String, String> ent : smap.entrySet()) {
            double calcVal = 0D;
            if (isFunction(ent.getValue())) {
                calcVal = evaluateFunctionExp(ent.getValue());
            } else if (isArithExp(ent.getValue())) {
                calcVal = evaluateArithmecticExp(ent.getValue());
            } else {
                calcVal = Double.parseDouble(ent.getValue());
            }
            vmap.put(ent.getKey(), calcVal);
        }
        String stmt = cmputor.substring(idx + 1, length - 1);
        StrSubstitutor ss = new StrSubstitutor(vmap);
        stmt = ss.replace(stmt);
        if (logger.isDebugEnabled()) {
            logger.debug("vars=" + smap);
            logger.debug("stmt=" + stmt);
        }
        return jexlEvaluate(stmt);
    }

    protected double evaluateFunctionExp(String cmputor) {
        double[] vals = getVals();
        double result;
        int pos = cmputor.indexOf("(");
        int pos1 = cmputor.indexOf(")", pos);
        if (pos != StringUtils.INDEX_NOT_FOUND) {
            String incontent = cmputor.substring(pos + 1, pos1);
            if (StringUtils.isNotBlank(incontent)) {
                /* 处理形如：sum（Q1-23） */
                if (incontent.indexOf('-') != StringUtils.INDEX_NOT_FOUND) {
                    int ports[] = UtilCollection.toIntArray(incontent, '-');
                    int left = ports[0];
                    int right = ports[1];
                    double newvals[] = new double[right + 1];
                    for (int i = 0; i < newvals.length; ++i) {
                        newvals[i] = vals[left++];
                    }
                    vals = newvals;
                } else if (incontent.indexOf(',') != StringUtils.INDEX_NOT_FOUND) {
                    /* 处理形如：sum（Q1，Q23，Q28） */
                    int idxs[] = UtilCollection.toIntArray(incontent, ',');
                    double newvals[] = new double[idxs.length];
                    for (int i = 0; i < newvals.length; ++i) {
                        newvals[i] = vals[idxs[i]];
                    }
                    vals = newvals;
                }
            }
        }

        if (cmputor.startsWith(SUM_LEXEME)) {
            result = StatUtils.sum(vals);
        } else if (cmputor.startsWith(ARG_LEXEME)) {
            result = StatUtils.mean(vals);
        } else if (cmputor.startsWith(PRODUCT_LEXEME)) {
            result = StatUtils.product(vals);
        } else {
            result = 0D;
        }
        return Arith.round(result, 2);
    }

    protected boolean isArithExp(String cmputor) {
        return StringUtils.indexOfAny(cmputor, ARITH_OPERATOR) != StringUtils.INDEX_NOT_FOUND;
    }

    protected boolean isStmtBlock(String cmputor) {
        return cmputor.charAt(0) == Calc.OPEN_BLOCK;
    }

    protected boolean isFunction(String cmputor) {
        return cmputor.startsWith(SUM_LEXEME) || cmputor.startsWith(ARG_LEXEME) || cmputor.startsWith(PRODUCT_LEXEME);
    }

    static private String MY_PACKEG = "edutec.scale.questionnaire.";

    protected QuestionCalc createCalc(String clazz) {
        try {
            return (QuestionCalc) Resources.instantiate(MY_PACKEG + clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("ClassNotFoundException:" + e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("InstantiationException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("IllegalAccessException:" + e.getMessage());
        }

    }
}
