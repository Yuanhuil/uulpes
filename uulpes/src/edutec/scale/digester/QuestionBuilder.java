package edutec.scale.digester;

import org.apache.commons.digester.Digester;

import edutec.scale.model.Option;
import heracles.digester.DigesterUtils;

public final class QuestionBuilder {
    static private final String ADD_QUESTION = "addQuestion";

    /* 题目 */
    static private final String PATTEN_QUEST = "*/questions/q";

    /* 各类题型的通用属性 */
    static private final String PATTEN_QUEST_TITLE = "*/questions/q/t";
    static private final String PATTEN_QUEST_PREFIX = "*/questions/q/prefix";
    static private final String PATTEN_QUEST_POSTFIX = "*/questions/q/postfix";
    static private final String PATTEN_QUEST_HEADTITLE = "*/questions/q/headtitle";
    static private final String PATTEN_QUEST_HASPIC = "*/questions/q/haspic";
    static private final String PATTEN_QUEST_LIMITTIME = "*/questions/q/limittime";
    static private final String PATTEN_QUEST_COMP = "*/questions/q/c";

    /* 问答题的属性 */
    static private final String PATTEN_QA_RS = "*/questions/q/rs";
    static private final String PATTEN_QA_CS = "*/questions/q/cs";

    /* 矩阵题的属性 */
    static private final String PATTEN_MX_CTS = "*/questions/q/cts";
    static private final String PATTEN_MX_RTS = "*/questions/q/rts";
    static private final String PATTEN_MX_VAS = "*/questions/q/vas";
    static private final String PATTEN_MX_SELE = "*/questions/q/sel";
    static private final String PATTEN_MX_DEF = "*/questions/q/def"; // 所有选项默认值
    static private final String PATTEN_MX_RTX = "*/questions/q/rtx"; // 扩展行
    static private final String PATTEN_MX_RSP = "*/questions/q/rsp"; // 空闲行

    /* 排序题的属性 */
    static private final String PATTEN_SORT_OPT = "*/questions/q/opt";
    static private final String PATTEN_SORT_VAS = PATTEN_MX_VAS;

    /* 选择题的属性 */
    static public final String PATTEN_OPTION = "*/o";

    private Digester digester;

    static public QuestionBuilder newInstance(Digester digester) {
        QuestionBuilder factory = new QuestionBuilder();
        factory.digester = digester;
        return factory;
    }

    private QuestionBuilder() {

    }

    public void buileQuestion() {
        digester.addRule(PATTEN_QUEST, new QuestionCreateRule());

        // 各类问题的通用属性
        /* 将问题加入问题列表 */
        digester.addSetNext(PATTEN_QUEST, ADD_QUESTION); /* 加入问题 */
        /* 为问题添加属性 */
        digester.addSetProperties(PATTEN_QUEST);
        /* 题目的标题 */
        digester.addBeanPropertySetter(PATTEN_QUEST_TITLE, "title");
        // 前缀
        digester.addBeanPropertySetter(PATTEN_QUEST_PREFIX, "prefix");
        // 后缀
        digester.addBeanPropertySetter(PATTEN_QUEST_POSTFIX, "postfix");
        // 有主标题的题目，如矩阵题
        digester.addBeanPropertySetter(PATTEN_QUEST_HEADTITLE, "headtitle");

        digester.addBeanPropertySetter(PATTEN_QUEST_LIMITTIME, "limittime");
        // 图片
        digester.addBeanPropertySetter(PATTEN_QUEST_HASPIC, "haspic");
        /* 题目本身取值的计算公式 */
        digester.addBeanPropertySetter(PATTEN_QUEST_COMP, "calcExp");

        // 以下是各类问题的特殊属性
        /* 选项题 */
        addOptAndSetprops();
        /* 问答题 */
        setQaProps();
        /* 矩阵题 */
        setMxProps();
        /* 排序题 */
        setSortProps();
    }

    /* 排序题 */
    private void setSortProps() {
        /* 排序题 */
        digester.addBeanPropertySetter(PATTEN_SORT_OPT, "options");
        /* 问答题答题时所需文本列数 */
        digester.addBeanPropertySetter(PATTEN_SORT_VAS, "values");
    }

    /* 选项题 */
    private void addOptAndSetprops() {
        DigesterUtils.add3(digester, PATTEN_OPTION, Option.class, "addOption");
        digester.addBeanPropertySetter(PATTEN_OPTION, "title");
    }

    /* 矩阵题 */
    private void setMxProps() {
        /* 矩阵题目的标题 */
        digester.addBeanPropertySetter(PATTEN_MX_RTS, "rowTitles");
        /* 矩阵题目的列标题 */
        digester.addBeanPropertySetter(PATTEN_MX_CTS, "columnTitles");
        /* 矩阵题目的行标题 */
        digester.addBeanPropertySetter(PATTEN_MX_RTS, "rowTitles");
        /* 矩阵题目的值标题 */
        digester.addBeanPropertySetter(PATTEN_MX_VAS, "values");
        /* 矩阵题目的选项规则: */
        digester.addBeanPropertySetter(PATTEN_MX_SELE, "selectOpts");
        /* 矩阵题目的选项的默认值，当所有的选项值都一样时，使用这个属性: */
        digester.addBeanPropertySetter(PATTEN_MX_DEF, "defval");
        /* 矩阵题目有扩展项时: */
        digester.addBeanPropertySetter(PATTEN_MX_RTX, "rtx");
        /* 矩阵题目被划分两部分时，有此属性 */
        digester.addBeanPropertySetter(PATTEN_MX_RSP, "rowParts");

    }

    /* 问答题 */
    private void setQaProps() {
        /* 问答题答题时所需文本行数 */
        digester.addBeanPropertySetter(PATTEN_QA_RS, "rows");
        /* 问答题答题时所需文本列数 */
        digester.addBeanPropertySetter(PATTEN_QA_CS, "cols");
    }
}
