package edutec.scale.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.math.util.MathUtils;

import edutec.scale.exception.CalcException;
import edutec.scale.exception.DimensionException;
import edutec.scale.exception.QuestionException;
import edutec.scale.model.Question;
import edutec.scale.questionnaire.DimensionBlock;
import edutec.scale.questionnaire.QuestionBlock;
import edutec.scale.questionnaire.Questionnaire;
import heracles.util.Arith;
import heracles.util.Pools;
import heracles.util.UtilCollection;

public abstract class QuestionnaireUtils {

    /**
     * 生成答案行,保存在数据库中<br>
     * 每行的格式：<br>
     * //fomat Q1:1:0\n, 编号：选择的项索引号(答案):分数<br>
     * 
     * @param questionnaire
     *            问卷对象
     * @return
     * @throws QuestionException
     *             当获取分数时，抛出无法获得分数异常
     */
    public static String buildAnswerReslut(Questionnaire questionnaire, boolean isIndividual) throws QuestionException {
        List<QuestionBlock> qbs = questionnaire.getQuestionBlocks(isIndividual);
        if (CollectionUtils.isEmpty(qbs)) {
            return StringUtils.EMPTY;
        }
        StrBuilder sb = null;
        try {
            sb = Pools.getInstance().borrowStrBuilder();
            LineFormat format = new QuestionBlockFormat();
            for (QuestionBlock qb : qbs) {
                // 占位题，测谎题，模板题不记录问题答案
                if (qb.getQuestion().isPlaceholder() | qb.getQuestion().isLie() | qb.getQuestion().isTemplate()) {
                    continue;
                }
                /* 暂时为选择题 */
                // 模版题的作用包含要被计算的题和内容，本身的答案不需要保存，所以不建立此答案串;date:2008-06-05
                String str = format.formatAnswers(qb);
                if (StringUtils.isNotEmpty(str)) {
                    sb.append(str);
                    sb.append(QuestionnaireConsts.PART_SPARATOR); // #号
                }
            }
            sb.setLength(sb.length() - 1);
            return sb.toString();
        } finally {
            Pools.getInstance().returnStrBuilder(sb);
        }
    }

    /**
     * 生成维度结果行 每行的格式：<br>
     * //fomat W1:23:34#, 维度编号：粗分:最终分<br>
     * 
     * @param questionnaire
     * @return
     * @throws DimensionException
     */
    public static String buildDimensionReslut(Questionnaire questionnaire) throws DimensionException {
        List<DimensionBlock> dims = questionnaire.getDimensionBlocks();
        StrBuilder sb = null;
        try {
            sb = Pools.getInstance().borrowStrBuilder();
            for (DimensionBlock dim : dims) {
                sb.append(dim.getId()).append(QuestionnaireConsts.UNIT_SPARATOR);
                sb.append(dim.getRawScore()); // 粗分
                sb.append(QuestionnaireConsts.UNIT_SPARATOR);// #号
                if (dim.getStdScore() != null) {
                    double dval = MathUtils.round(dim.getStdScore().doubleValue(), 2);// 最终标准分，或许等同raw分
                    sb.append(dval);
                }
                sb.append(QuestionnaireConsts.UNIT_SPARATOR);// #号
                sb.append(dim.getScoreGrade());
                sb.append(QuestionnaireConsts.UNIT_SPARATOR);// #号
                if (questionnaire.getScale().isWarningOrNot())
                    sb.append(dim.getWarningGrade());
                // dval = MathUtils.round(dim.getTScore().doubleValue(), 2);//
                // T分，或许等同raw分
                // sb.append(dval);
                sb.append(QuestionnaireConsts.PART_SPARATOR); // #号
            }
            if (!sb.isEmpty()) {
                sb.setLength(sb.length() - 1);
            }
            return sb.toString();
        } finally {
            Pools.getInstance().returnStrBuilder(sb);
        }
    }

    // 3.0设置维度原始分、标准分
    public static String buildDimensionReslut1(Questionnaire questionnaire) throws DimensionException {
        List<DimensionBlock> dims = questionnaire.getDimensionBlocks();
        StrBuilder sb = null;
        try {
            sb = Pools.getInstance().borrowStrBuilder();
            for (DimensionBlock dim : dims) {
                sb.append(dim.getId()).append(QuestionnaireConsts.UNIT_SPARATOR);
                sb.append(dim.getRawScore()); // 粗分
                sb.append(QuestionnaireConsts.UNIT_SPARATOR);// #号
                double dval = MathUtils.round(dim.getAScore().doubleValue(), 2);// 最终标准分，或许等同raw分
                sb.append(dval);
                sb.append(QuestionnaireConsts.PART_SPARATOR); // #号
            }
            if (!sb.isEmpty()) {
                sb.setLength(sb.length() - 1);
            }
            return sb.toString();
        } finally {
            Pools.getInstance().returnStrBuilder(sb);
        }
    }

    /**
     * 得到维度编号和维度值两个字符串，目的是为了满足SQL语句 添加了sepecalStd变量,为了那个可恶的三视角，
     * 处理三视角时，在examresult_student和examresult_dim_mental_health表中要存各自的标准分
     * 以便随时计算它的三视角分数 而在examresult_dim_student表中，始终要保存最终分数，因为需要群体分析
     * 所以处理三视角量表时，调用了两次这个函数
     * 1）向examresult_dim_mental_health中插入数据，满足三视角的计算，设置sepecalStd=true
     * 2）向examresult_dim_student中插入数据，满足群体分析，设置sepecalStd=false
     * 
     * @param questionnaire
     * @return
     * @throws DimensionException
     */
    public static String[] buildDimAndVals(Questionnaire questionnaire, boolean sepecalStd) throws DimensionException {
        String result[] = new String[2];
        List<DimensionBlock> dims = questionnaire.getDimensionBlocks();
        StrBuilder sb = null;
        try {
            sb = Pools.getInstance().borrowStrBuilder();
            // 生成各维度字段名
            for (DimensionBlock dim : dims) {
                sb.append(dim.getId()).append(",");
            }
            sb.setLength(sb.length() - 1);
            result[0] = sb.toString();
            sb.clear();
            // 判定是否是能力量表
            boolean isAbility = ScaleUtils.isAbilityScale(questionnaire.getScaleId());
            for (DimensionBlock dim : dims) {
                short score = 0;
                // Number number = dim.getAScore(); // 获取次序：标准分->中间分(标准分)->粗分
                Number number = dim.getRawScore();
                if (sepecalStd) {
                    number = dim.getStdScore();
                    if (number == null) {
                        number = dim.getRawScore();
                    }
                }
                // 如果是能力量表则保存它的粗分，因为
                if (isAbility) {
                    number = dim.getRawScore();
                }
                score = transToShortScore(number);
                sb.append(score);
                sb.append(",");
            }
            sb.setLength(sb.length() - 1);
            result[1] = sb.toString();
            return result;
        } finally {
            Pools.getInstance().returnStrBuilder(sb);
        }
    }

    public static String buildDimensionWarningResult(Questionnaire questionnaire) throws DimensionException {
        List<DimensionBlock> dims = questionnaire.getDimensionBlocks();
        StrBuilder sb = null;
        try {
            sb = Pools.getInstance().borrowStrBuilder();
            for (DimensionBlock dim : dims) {
                sb.append(dim.getId()).append(QuestionnaireConsts.UNIT_SPARATOR);
                sb.append(dim.getWarningGrade()); // 告警等级
                sb.append(QuestionnaireConsts.PART_SPARATOR); // #号
            }
            if (!sb.isEmpty()) {
                sb.setLength(sb.length() - 1);
            }
            return sb.toString();
        } finally {
            Pools.getInstance().returnStrBuilder(sb);
        }
    }

    public static String buildDimensionScoreGradeResult(Questionnaire questionnaire) throws DimensionException {
        List<DimensionBlock> dims = questionnaire.getDimensionBlocks();
        StrBuilder sb = null;
        try {
            sb = Pools.getInstance().borrowStrBuilder();
            for (DimensionBlock dim : dims) {
                sb.append(dim.getId()).append(QuestionnaireConsts.UNIT_SPARATOR);
                sb.append(dim.getScoreGrade()); // 告警等级
                sb.append(QuestionnaireConsts.PART_SPARATOR); // #号
            }
            if (!sb.isEmpty()) {
                sb.setLength(sb.length() - 1);
            }
            return sb.toString();
        } finally {
            Pools.getInstance().returnStrBuilder(sb);
        }
    }

    private static short transToShortScore(Number number) {
        short score = 0;
        if (number instanceof Double) {
            Double val = (Double) number;
            double rd = Arith.round(val, 2);
            rd *= 100;
            score = (short) rd;
        } else {
            score = number.shortValue();
            score *= 100;
        }
        return score;
    }

    public static void fillQuestionContentFromStr(Questionnaire questionnaire, String questionScore,
            boolean isIndividual) {
        if (StringUtils.isEmpty(questionScore)) {
            return;
        }
        List<List<String>> strList = UtilCollection.toStringList(questionScore, QuestionnaireConsts.PART_SPARATOR,
                QuestionnaireConsts.UNIT_SPARATOR);
        for (List<String> list : strList) {
            QuestionBlock qBlk = null;
            if (isIndividual) {
                qBlk = questionnaire.findIndivQuestionBlock(list.get(0));
            } else {
                qBlk = questionnaire.findQuestionBlock(list.get(0));
            }
            String aw = list.get(1);
            if (aw.indexOf(';') != -1) {// 多选题
                aw = aw.replace(';', ',');
            }
            qBlk.setAnswer(aw);
            if (list.size() == 3) {
                qBlk.setScore(NumberUtils.toDouble(list.get(2)));
            } else if (list.size() == 4) {
                qBlk.setSubjoin(list.get(2));
                qBlk.setScore(NumberUtils.toDouble(list.get(3)));
            }
        }
    }

    public static void fillDimContentFromStr(Questionnaire questionnaire, String dimSore) {
        if (StringUtils.isEmpty(dimSore)) {
            return;
        }
        List<List<String>> strList = UtilCollection.toStringList(dimSore, QuestionnaireConsts.PART_SPARATOR,
                QuestionnaireConsts.UNIT_SPARATOR);
        for (List<String> list : strList) {
            DimensionBlock dimBlk = questionnaire.findDimensionBlock(list.get(0));
            dimBlk.completeRawCalc();
            dimBlk.setRawScore(NumberUtils.toDouble(list.get(1)));
            if (list.size() == 3) {
                dimBlk.completeStdCalc();
                dimBlk.setStdScore(NumberUtils.toDouble(list.get(2)));
                dimBlk.setFinalScore(NumberUtils.toDouble(list.get(2)));
            }
        }
    }

    public static void fillDimContentFromStr1(Questionnaire questionnaire, String dimSore) {
        if (StringUtils.isEmpty(dimSore)) {
            return;
        }
        List<List<String>> strList = UtilCollection.toStringList(dimSore, QuestionnaireConsts.PART_SPARATOR,
                QuestionnaireConsts.UNIT_SPARATOR);
        try {
            for (List<String> list : strList) {
                DimensionBlock dimBlk = questionnaire.findDimensionBlock(list.get(0));
                dimBlk.completeRawCalc();
                dimBlk.setRawScore(NumberUtils.toDouble(list.get(1)));
                if (list.size() > 3) {
                    dimBlk.completeStdCalc();
                    dimBlk.setStdScore(NumberUtils.toDouble(list.get(2)));
                    // dimBlk.setTScore(5.5+1.5*NumberUtils.toDouble(list.get(2)));
                    // dimBlk.setFinalScore(NumberUtils.toDouble(list.get(2)));
                    if (dimBlk.getFormula() == null) {
                        dimBlk.setStdScore(NumberUtils.toDouble(list.get(1)));
                        dimBlk.setTScore(NumberUtils.toDouble(list.get(1)));
                        dimBlk.setRank(Integer.parseInt(list.get(3)));
                        dimBlk.setScoreGrade(Integer.parseInt(list.get(3)));
                        continue;
                    }
                    if (dimBlk.getFormula().getTexp() == null) {
                        dimBlk.setStdScore(NumberUtils.toDouble(list.get(1)));
                        dimBlk.setTScore(NumberUtils.toDouble(list.get(1)));
                        dimBlk.setRank(Integer.parseInt(list.get(3)));
                        dimBlk.setScoreGrade(Integer.parseInt(list.get(3)));
                        continue;
                    }
                    String texp = dimBlk.getFormula().getTexp();
                    if (texp.contains("T="))
                        ;
                    texp = texp.substring(2);
                    if (texp.contains("Z"))
                        texp = texp.replace("Z", list.get(2));
                    Expression e = ExpressionFactory.createExpression(texp);
                    double tScore = Arith.round(Double.parseDouble(e.evaluate(null).toString()), 2);
                    dimBlk.setTScore(tScore);
                    dimBlk.setFinalScore(tScore);

                    dimBlk.setRank(Integer.parseInt(list.get(3)));
                    dimBlk.setScoreGrade(Integer.parseInt(list.get(3)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    static interface LineFormat {
        String formatAnswers(QuestionBlock block) throws QuestionException;
    }

    static class QuestionBlockFormat implements LineFormat {
        public String formatAnswers(QuestionBlock block) {
            StrBuilder sb = null;
            try {
                sb = Pools.getInstance().borrowStrBuilder();
                Question sQ = block.getQuestion();
                if (block.isFinish()) {
                    sb.append(sQ.getId());
                    sb.append(QuestionnaireConsts.UNIT_SPARATOR); // 逗号
                    String answer = block.getAnswer();
                    if (answer.indexOf(',') != -1)// 多选题
                        answer = answer.replace(',', ';');
                    // sb.append(block.getAnswer());
                    sb.appendln(answer);
                    if (block.getSubjoin() != null) {
                        sb.append(QuestionnaireConsts.UNIT_SPARATOR); // 逗号
                        sb.append(block.getSubjoin());
                    }
                    sb.append(QuestionnaireConsts.UNIT_SPARATOR); // 逗号
                    try {
                        sb.append(block.getScore());// 分数
                    } catch (CalcException e) {
                        // ignore
                    }
                }
                return sb.toString();
            } finally {
                Pools.getInstance().returnStrBuilder(sb);
            }
        }
    }

    static public String generateDimScoreStr(List<DimensionInfo> list) {
        StrBuilder sb = null;
        try {
            sb = Pools.getInstance().borrowStrBuilder();
            for (DimensionInfo info : list) {
                sb.append(info.getId()).append(QuestionnaireConsts.UNIT_SPARATOR);
                sb.append(info.getRawScore()).append(QuestionnaireConsts.UNIT_SPARATOR);
                sb.append(info.getStdScore()).append(QuestionnaireConsts.PART_SPARATOR);
            }
            return sb.toString();
        } finally {
            Pools.getInstance().returnStrBuilder(sb);
        }
    }

    static public class DimensionInfo {
        private String id;
        private String rawScore;
        private String stdScore;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRawScore() {
            return rawScore;
        }

        public void setRawScore(String rawScore) {
            this.rawScore = rawScore;
        }

        public String getStdScore() {
            return StringUtils.defaultString(stdScore);
        }

        public void setStdScore(String stdScore) {
            this.stdScore = stdScore;
        }
    }

}
