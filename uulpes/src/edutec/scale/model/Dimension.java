package edutec.scale.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;

import edutec.scale.exception.DimensionException;
import freemarker.template.utility.OptimizerUtil;
import heracles.util.UtilCollection;
import heracles.web.util.HtmlStr;

/**
 * @author Administrator
 */
@SuppressWarnings("unchecked")
public class Dimension extends TitleableSupport implements Serializable, Calculable, Comparable {

    /**
     * 
     */
    private static final long serialVersionUID = -2753585921764745391L;
    public static final int NO_CALC = 1;// 不再做任何计算，留有原来的原始和/或标准分
    public static final String SUM_SCORE_DIM = "W0";
    public static final String SUM_SCORE_DIM0 = "W00";

    static final char SPLIT_TOKEN = ',';

    private String id;
    private String title;
    private String descn;
    private int constrain;
    private String questionIds;
    private String reverseQuestionIds;// 反向题
    private String dimensionIds;
    private Formula formula;
    private Scale scale;

    private Dimension parenDimension;
    private List<Dimension> subdimensionList = null;
    private List<Question> questionList = null;
    private List<String> tmplist = new ArrayList<String>();

    public Dimension() {

    }

    public Dimension(String id) {
        this.setId(id);
    }

    public Dimension(String id, Scale scale) {
        setId(id);
        setScale(scale);
        getScale().addDimension(this);
    }

    public Dimension(String id, String title, Scale scale) {
        setId(id);
        setScale(scale);
        setTitle(title);
        getScale().addDimension(this);
    }

    public void buildFormula(String rawexp, String stdexp, String texp) {
        setFormula(new Formula(rawexp, stdexp, texp));
    }

    public boolean isInterior() {
        return parenDimension != null && CollectionUtils.isEmpty(subdimensionList);
    }

    public boolean isRoot() {
        return parenDimension == null;
    }

    public boolean isLeaf() {
        return CollectionUtils.isNotEmpty(questionList);
    }

    public void mount() throws DimensionException {
        if (StringUtils.isNotBlank(dimensionIds)) {
            mountDimensions();
        } else if (StringUtils.isNotBlank(questionIds)) {
            mountQuestions();
        } else {
            throw new DimensionException("量表:" + getScale().getTitle() + "-维度:" + getTitle() + ":" + getId()
                    + ":在量表配置文件中，没有配置此维度与[其它维度/量表问题]间的所属关系.");
        }
        // 不再需要dimensionIds；
        this.setDimensionIds(null);
        // 不再需要questionIds；
        // this.setQuestionIds(null);
    }

    /**
     * 组装具有子维度的维度
     */
    private void mountDimensions() {
        if (subdimensionList == null) {
            subdimensionList = new ArrayList<Dimension>();
        }
        subdimensionList.clear();
        UtilCollection.toList(dimensionIds, SPLIT_TOKEN, tmplist);
        for (String dimensionId : tmplist) {
            Dimension dimension = scale.findDimension(dimensionId);
            Validate.notNull(dimension, "维度：[" + dimensionId + "]不存在");
            dimension.setParenDimension(this);
            subdimensionList.add(dimension);
        }
        OptimizerUtil.optimizeListStorage(subdimensionList);
    }

    /**
     * 组装具有问题的维度，并建立问题和维度的关系
     */
    private void mountQuestions() {
        if (questionList == null) {
            questionList = new ArrayList<Question>();
        }
        questionList.clear();
        String qIds = questionIds.trim();
        if (qIds.equalsIgnoreCase("all") || qIds.equals("*")) {
            mountAllQuestion(); // 所有问题
        } else if (qIds.indexOf("*") != StringUtils.INDEX_NOT_FOUND) {
            mountPattenQuestion(qIds); // 如QA*,所有QA开头的题目
        } else if (qIds.indexOf(":") != StringUtils.INDEX_NOT_FOUND) {
            mountIncQuestion(qIds); // 累加如Q:1+4:78
        } else {
            mountSplitQuestion(qIds); // 默认的mount方法
        }
        for (Question question : questionList) {
            question.setDimension(this);
        }
        OptimizerUtil.optimizeListStorage(questionList);

    }

    private void mountAllQuestion() {
        questionList.addAll(scale.getQuestions());
    }

    /**
     * 已经被并入mountSplitQuestion方法中
     * 
     * @param qIds
     */
    @SuppressWarnings("unused")
    private void mountRangQuestion(String qIds) {
        int idx = 0;
        String prefix = StringUtils.EMPTY;
        String arang[] = { StringUtils.EMPTY, StringUtils.EMPTY };
        for (int i = 0; i < qIds.length(); ++i) {
            char ch = qIds.charAt(i);
            if (CharUtils.isAsciiAlpha(ch)) {
                prefix += ch;
            } else if (CharUtils.isAsciiNumeric(ch)) {
                arang[idx] += ch;
            } else if (ch == '-') {
                idx++;
                if (idx > 1) {
                    throw new RuntimeException("区间符号[-]只应该有一个");
                }
            }
        }
        int start = NumberUtils.toInt(arang[0]);
        int end = NumberUtils.toInt(arang[1]) + 1;
        for (int i = start; i < end; ++i) {
            String qId = prefix + i;
            Question question = scale.findQuestion(qId);
            Validate.notNull(question, "没有题目：[" + qId + "]");
            questionList.add(question);
        }
    }

    /**
     * 处理诸如：Q1,Q2,Q3-8,....Qn序列.
     * 
     * @param qIds
     */
    private void mountSplitQuestion(String qIds) {
        StrBuilder sbChar = new StrBuilder(8);
        StrBuilder sbNumb = new StrBuilder(4);
        StrBuilder sbnext = new StrBuilder(4);
        for (int index = 0; index < qIds.length(); ++index) {
            char ch = qIds.charAt(index);
            if (Character.isWhitespace(ch))
                continue;
            if (ch == UtilCollection.COMMA || ch == UtilCollection.COMMACHINA || ch == UtilCollection.DUNHAO) {
                sbChar.append(sbNumb);
                Question question = scale.findQuestion(sbChar.toString());
                Validate.notNull(question,
                        "没有题目：[" + getScale().getTitle() + "|" + this.getId() + "-" + sbChar.toString() + "]");
                questionList.add(question);
                sbChar.clear();
                sbNumb.clear();
                continue;
            }
            if (Character.isLetter(ch)) {
                sbChar.append(ch);
            } else if (Character.isDigit(ch)) {
                sbNumb.append(ch);
            } else if (ch == '_') {
                sbNumb.append(ch);
            } else if (ch == '.') {
                sbNumb.append(ch);
                continue;
                /*
                 * sbnext.clear(); int nextpos = index + 1; for (; nextpos <
                 * qIds.length(); ++nextpos) { char c = qIds.charAt(nextpos); if
                 * (c == UtilCollection.COMMA) { break; } sbnext.append(c); }
                 * index = nextpos; int left =
                 * NumberUtils.toInt(sbNumb.toString()); int right =
                 * NumberUtils.toInt(sbnext.toString()); sbnext.clear();
                 * sbnext.append(sbChar); for (int i = left; i <= right; ++i) {
                 * sbnext.append(i); Question question =
                 * scale.findQuestion(sbnext.toString());
                 * Validate.notNull(question, "没有题目：[" + getScale().getTitle() +
                 * "|" + this.getId() + "-" + sbnext.toString() + "]");
                 * questionList.add(question); sbnext.clear();
                 * sbnext.append(sbChar); } sbChar.clear(); sbNumb.clear();
                 */
            }
        }
        if (!sbChar.isEmpty() || !sbNumb.isEmpty()) {
            sbChar.append(sbNumb);
            Question question = scale.findQuestion(sbChar.toString());
            if (question == null)
                return;
            // Validate.notNull(question, "没有题目：[" + getScale().getTitle() + "|"
            // + this.getId() + "-" + sbChar.toString() + "]");
            questionList.add(question);
        }

    }

    private void mountPattenQuestion(String qIds) {
        qIds = StringUtils.replace(qIds, "*", StringUtils.EMPTY);
        for (Question question : scale.getQuestions()) {
            if (question.getId().indexOf(qIds) != StringUtils.INDEX_NOT_FOUND) {
                questionList.add(question);
            }
        }
    }

    /**
     * MBTI职业性格测试-5 累加如Q:1+4:97，表示Q开头Q1,Q5,Q9,....Q1+4n
     * 
     * @param qIds
     */
    private void mountIncQuestion(String qIds) {
        UtilCollection.toList(qIds, ':', tmplist);// Q 1+4 97
        List<String> inx = UtilCollection.toList(tmplist.get(1), '+');// 1 , 4
        String prefix = tmplist.get(0);// 1
        int start = NumberUtils.toInt(inx.get(0));// 1
        int degree = NumberUtils.toInt(inx.get(1));// 4
        int end = NumberUtils.toInt(tmplist.get(2));// 97
        for (int i = start; i <= end; i += degree) {
            String questionId = prefix + i;
            Question question = scale.findQuestion(questionId);
            Validate.notNull(question, "没有题目：[" + questionId + "]");
            questionList.add(question);
        }
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public String getDimensionIds() {
        return dimensionIds;
    }

    public void setDimensionIds(String dimensionIds) {
        this.dimensionIds = dimensionIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(String questionIds) {
        this.questionIds = questionIds;
    }

    public String getReverseQuestionIds() {
        return reverseQuestionIds;
    }

    public void setReverseQuestionIds(String reverseQuestionIds) {
        this.reverseQuestionIds = reverseQuestionIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public Dimension getParenDimension() {
        return parenDimension;
    }

    public void setParenDimension(Dimension parenDimension) {
        this.parenDimension = parenDimension;
    }

    public List<Dimension> getSubdimensions() {
        return subdimensionList;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public String toXml() {
        StrBuilder sb = new StrBuilder(512);
        sb.append("\t<d ");
        sb.append(" id=\"").append(getId()).append("\"");
        sb.append(" title=\"").append(HtmlStr.htmlEncode(getTitle())).append("\"");
        sb.append(" descn=\"").append(HtmlStr.htmlEncode(getDescn())).append("\"");
        sb.append(">\n");
        if (StringUtils.isNotBlank(dimensionIds)) {
            sb.append("\t<dimensionIds>\n");
            sb.append(dimensionIds);
            sb.append("\n\t</dimensionIds> ");
        } else if (StringUtils.isNotBlank(questionIds)) {
            sb.append("\t<questionIds>");
            sb.append(questionIds);
            sb.append("\n\t</questionIds>\n");
            if (StringUtils.isNotBlank(reverseQuestionIds)) {
                sb.append("\t<reverseQuestionIds>");
                sb.append(reverseQuestionIds);
                sb.append("\n\t</reverseQuestionIds>\n");
            }
        }
        // huangc添加那些没有计算公式的
        if (formula != null)
            // ---------------
            sb.append(formula.toXml());
        sb.append("\t</d>\n");
        return sb.toString();
    }

    public boolean isMyDimension(final String dimensionId) {
        List<Dimension> list = getSubdimensions();
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return CollectionUtils.exists(list, new Predicate() {
            public boolean evaluate(Object object) {
                Dimension dimension = (Dimension) object;
                return dimension.getId().equals(dimensionId);
            }
        });
    }

    public void print() {
        // System.out.println("id=" + this.id);
        // System.out.println("title=" + title);
        // System.out.println("questionIds=" + questionIds);
        // System.out.println("dimensionIds=" + dimensionIds);
    }

    public String getCalcExp() {
        return null;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public int getConstrain() {
        return constrain;
    }

    public void setConstrain(int constrain) {
        this.constrain = constrain;
    }

    public int compareTo(Object o) {
        Dimension d = (Dimension) o;
        return this.getId().compareTo(d.getId());
    }
}
