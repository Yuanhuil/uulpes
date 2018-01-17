package edutec.scale.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.IntRange;
import org.apache.ecs.html.IMG;

import edutec.scale.model.Option;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.Scale;
import edutec.scale.model.SelectionQuestion;
import heracles.util.UtilCollection;

/**
 * 此类处理，从xml读取量表数据（题目、维度等信息）后，统一进行后续的题目配置工作
 * 
 * @author Administrator
 */

public class QuestionDesc {
    public static final char RANGE_SPARATOR = '-';

    public static void postScaleProcess(Scale scale) {
        // 处理题目类型配置
        processQuestionType(scale);
        // 处理反向积分的题目配置
        processReverseQuestions(scale);
    }

    /**
     * 处理各部分的数据类型。
     * 
     * @param scale
     */
    private static void processQuestionType(Scale scale) {
        String questiontype = scale.getQuestiontype();
        String questiontype0 = scale.getQuestiontype0();
        String questiontype1 = scale.getQuestiontype1();
        String questiontype2 = scale.getQuestiontype2();
        String questiontype3 = scale.getQuestiontype3();
        if (StringUtils.isBlank(questiontype)) {
            questiontype = ScaleBeanFactory.getGlobalProp(ScaleBeanFactory.QUESTYPE);
        }
        if ("none".equals(questiontype)) {
            return;
        }
        // order is "questiontype1-questiontype2-questiontype";
        String qts[] = { questiontype0, questiontype1, questiontype2, questiontype3, questiontype };

        for (int i = 0; i < qts.length; ++i) {
            if (StringUtils.isNotEmpty(qts[i])) {
                questiontype = StringUtils.deleteWhitespace(qts[i]);
                parseSelectionType(scale, questiontype);
            }
        }

        // 不再使用Questiontype；
        scale.setQuestiontype(null);
        scale.setQuestiontype0(null);
        scale.setQuestiontype1(null);
        scale.setQuestiontype2(null);
        scale.setQuestiontype3(null);
    }

    /**
     * 为没有选项的选择题添加答案选项<br>
     * 解析的表达式：<br>
     * questiontype="selection|A,0,从来没有|B,1,有时出现,不是每周一次|C,2,至少每周一次"<br>
     * 1-70:questiontype="selection|A,0,从来没有|B,1,有时出现,不是每周一次|C,2,至少每周一次"<br>
     * 中学生职业兴趣问卷-212101;高考志愿指导-10001
     * 
     * @param scale
     * @param questiontype
     */
    private static void parseSelectionType(Scale scale, String questiontype) {
        String[] exp = UtilCollection.splitTwo(questiontype, ":");
        if (null == exp) {
            // questiontype="selection|A,0,从来没有|B,1,有时出现,不是每周一次|C,2,至少每周一次"
            String[][] optstrs = buildOptStrs(questiontype);
            List<Question> ques = scale.getQuestions();
            for (Question q : ques) {
                if (q instanceof SelectionQuestion) {
                    SelectionQuestion sq = (SelectionQuestion) q;
                    if (sq.optionSize() == 0) {
                        for (int i = 0; i < optstrs.length; ++i) {
                            sq.addOption(new Option(optstrs[i][0], optstrs[i][1], optstrs[i][2]));
                        }
                    }
                }
            }
        } else {
            // 1-70:questiontype="selection|A,0,从来没有|B,1,有时出现,不是每周一次|C,2,至少每周一次"<br>
            IntRange range = UtilCollection.toIntRange(exp[0], RANGE_SPARATOR);
            String[][] optstrs = buildOptStrs(exp[1]);
            for (int i = range.getMinimumInteger(); i <= range.getMaximumInteger(); ++i) {
                Question q = scale.findQuestion(i);
                if (q instanceof SelectionQuestion) {
                    SelectionQuestion sq = (SelectionQuestion) q;
                    if (q.getChoice().startsWith(QuestionConsts.CHOICE_IMG))
                        continue;
                    if (sq.optionSize() == 0) {
                        for (int j = 0; j < optstrs.length; ++j) {
                            // -----------------------id-------------value---------title---------
                            sq.addOption(new Option(optstrs[j][0], optstrs[j][1], optstrs[j][2]));
                        }
                    }
                }
            }
        }
    }

    private static void parseImgType(Scale scale) {
        final String IMG_POSTFIX = ".GIF";
        final String IMG_QSCR_STR = "mywork/img_scale/%s/%s" + IMG_POSTFIX;
        final String IMG_QSCR_STR2 = "mywork/img_scale/%s/%s_" + IMG_POSTFIX;
        final String IMG_ASCR_STR = "mywork/img_scale/%s/%s_%d" + IMG_POSTFIX;
        List<Question> ques = scale.getQuestions();
        for (Question q : ques) {
            SelectionQuestion sq = (SelectionQuestion) q;
            String qId = q.getId();
            String scaleId = q.getScale().getId();
            String qimgscr = String.format(IMG_QSCR_STR, scaleId, qId);
            IMG qimg = new IMG(qimgscr);
            String imgshow = qimg.toString();
            if (q.getChoice().endsWith("2")) {
                String qimgscr2 = String.format(IMG_QSCR_STR2, scaleId, qId);
                IMG qimg2 = new IMG(qimgscr2);
                imgshow += qimg2.toString();
            }
            if (StringUtils.isNotEmpty(q.getTitle())) {

            }
            q.setTitle(imgshow);
            int qsize = sq.getSize();
            for (int m = 0; m < qsize; m++) {
                String aimgscr = String.format(IMG_ASCR_STR, scaleId, qId, m + 1);
                IMG aimg = new IMG(aimgscr);
                sq.addOption(new Option("o" + m, m + "", aimg.toString()));
            }
        }
    }

    private static String[][] buildOptStrs(String questiontype) {
        String optionDesc = questiontype.substring(QuestionConsts.TYPE_SELECTION.length() + 1);
        String strs[] = UtilCollection.toArray(optionDesc, '|');
        String optstrs[][] = new String[strs.length][];
        for (int i = 0; i < strs.length; ++i) {
            optstrs[i] = splitThree(strs[i]);
        }
        return optstrs;
    }

    /**
     * 设置反向积分题
     * 
     * @param scale
     */
    private static void processReverseQuestions(Scale scale) {
        String s = StringUtils.deleteWhitespace(scale.getReversequestions());
        List<String> list = UtilCollection.toList(s, UtilCollection.COMMA);
        for (String qid : list) {
            Question q = scale.findQuestion(qid);
            Validate.notNull(q, "当parseReverseQuestions时，发现没有此题目：" + qid);
            q.setReverse(true);
        }
        // 不再使用reversequestions的内容，给与null；
        scale.setReversequestions(null);
    }

    private static String[] splitThree(String str) {
        String[] result = new String[3];
        String[] one = UtilCollection.splitTwo(str, ",");
        if (one == null) {
            throw new RuntimeException("<questions questiontype=\"?\">配置有误.");
        }
        String[] two = UtilCollection.splitTwo(one[1], ",");
        if (two == null) {
            throw new RuntimeException("<questions questiontype=\"?\">配置有误.");
        }
        result[0] = one[0];
        result[1] = two[0];
        result[2] = two[1];
        return result;
    }

}
