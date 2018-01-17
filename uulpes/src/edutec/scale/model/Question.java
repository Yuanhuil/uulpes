package edutec.scale.model;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import heracles.util.UtilMisc;
import heracles.web.util.HtmlStr;

public abstract class Question extends TitleableSupport implements Calculable, Cloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private static int FLAG_MUSTANSWER = 1 << 1; // 必答
    private static int FLAG_REVERSE = 1 << 2; // 反向计分

    // 此类题目不显示出来,不作为用户答题数计数
    private static int FLAG_VISIBLE = 1 << 0; // 可视

    // 一类题目仅显示，但不能作为题目计数
    private static int FLAG_PLACEHOLDER = 1 << 3; // 占位题

    /**
     * 模板题,模板题是一个特殊的题目，其必含一个网页，网页上包含多个题目 其所包含的多个题目，<br>
     * 用户用这个题目答题后，一次性从那个网页上提交 其包含的题目的答案，如《道德量表》，就是这样，<br>
     * 还有《语文能力量表》此类题目一般都同时也被计为placeholder题，表示不占用用户答题的题目计算,<br>
     * 说明：还有一个type="tmpl"的题，其作用仅仅是显示信息。不会传递答案到服务器上。将废弃这类题，只有 一种模板题 why？
     * 1）因为有些题目不能一道一道的显示在页面上，如《语文能力量表》，答题人需读取一篇文章，根据文章答题 or/and
     * 2）答题过程中，可能出现一些介绍/解释/说明，这时仅需要显示这些静态信息,供答题人阅读；
     **/
    private static int FLAG_TEMPLATE = 1 << 4;

    // 不保存此类题答案
    private static int FLAG_LIE = 1 << 5; // 测谎题

    // 新的扩展，将个人题目和量表本身题目，合成一个题目列表，设置此标志，就是为了区分
    private static int FLAG_INDIV = 1 << 6; // 是否是个人题目

    private String id;
    private String descn;
    private String headtitle;// 矩阵题的主标题
    private String title;
    private String validator;
    private String msg;
    private String format;
    private String calcExp;
    private int typeMode;
    private int choiceMode;
    private String prefix;// 前缀
    private String postfix;// 后缀
    private String limittime;// 限时
    private String haspic;

    private String[] templateQeslist;// 模板题所包含的答题集合

    // 默认为可视和必须回答
    private int flag = FLAG_VISIBLE | FLAG_MUSTANSWER;

    private Scale scale;
    private Dimension dimension;

    public Question() {
    }

    public String getChoice() {
        return QuestionConsts.CHOICE_TYPE[choiceMode];
    }

    public void setChoice(String choiceType) {
        this.choiceMode = ArrayUtils.indexOf(QuestionConsts.CHOICE_TYPE, choiceType);
    }

    public Question(String id) {
        this.setId(id);
    }

    public Question(String id, Scale scale) {
        this.setId(id);
        this.setScale(scale);
        scale.addQuestion(this);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public String getDimensionId() {
        return dimension.getId();
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public String getType() {
        return QuestionConsts.QUESTION_TYPE[typeMode];
    }

    public void setType(String type) {
        this.typeMode = ArrayUtils.indexOf(QuestionConsts.QUESTION_TYPE, type);
    }

    public String getTitle() {
        if (title != null)
            title = title.replaceAll("\n", "<br>");
        title = HtmlStr.decodeString(title);
        return StringUtils.defaultString(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadtitle() {
        return headtitle;
    }

    public void setHeadtitle(String headtitle) {
        this.headtitle = headtitle;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // 获得题目的主索引号，如Q65_1,Q64分别获得65，64
    public String getQIndex() {
        String[] temp;
        String idx;
        if (this.id.contains("_")) {
            temp = this.id.split("_");
            idx = temp[0].substring(1);
        } else if (this.id.contains(".")) {
            temp = this.id.split("\\.");
            idx = temp[0].substring(1);
        } else
            idx = this.id.substring(1);
        return idx;
    }

    public String getDescription() {
        return null;
    }

    public boolean isReverse() {
        return (this.flag & FLAG_REVERSE) > 0;
    }

    public void setReverse(boolean reverse) {
        if (!reverse) {
            this.flag &= ~FLAG_REVERSE;
        } else {
            this.flag |= FLAG_REVERSE;
        }
    }

    public boolean isLie() {
        return (this.flag & FLAG_LIE) > 0;
    }

    public void setLie(boolean reverse) {
        if (!reverse) {
            this.flag &= ~FLAG_LIE;
        } else {
            this.flag |= FLAG_LIE;
        }
    }

    public boolean isMustAnswer() {
        return (this.flag & FLAG_MUSTANSWER) > 0;
    }

    public void setMustAnswer(boolean mustAnswer) {
        if (!mustAnswer) {
            this.flag &= ~FLAG_MUSTANSWER;
        } else {
            this.flag |= FLAG_MUSTANSWER;
        }
    }

    public boolean isPlaceholder() {
        return (this.flag & FLAG_PLACEHOLDER) > 0;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * 作为占位符的题目
     * 
     * @param placeholder
     */
    public void setPlaceholder(boolean placeholder) {
        if (!placeholder) {
            this.flag &= ~FLAG_PLACEHOLDER;
        } else {
            this.flag |= FLAG_PLACEHOLDER;
        }
    }

    public boolean isVisible() {
        return (this.flag & FLAG_VISIBLE) > 0;
    }

    public void setVisible(boolean isVisible) {
        if (!isVisible) {
            this.flag &= ~FLAG_VISIBLE;
        } else {
            this.flag |= FLAG_VISIBLE;
        }
    }

    public boolean isTemplate() {
        return (this.flag & FLAG_TEMPLATE) > 0;
    }

    public void setTemplate(boolean isTemplate) {
        if (!isTemplate) {
            this.flag &= ~FLAG_TEMPLATE;
        } else {
            this.flag |= FLAG_TEMPLATE;
        }
    }

    public void setIndiv(boolean b) {
        if (!b) {
            this.flag &= ~FLAG_INDIV;
        } else {
            this.flag |= FLAG_INDIV;
        }
    }

    public boolean isIndiv() {
        return (this.flag & FLAG_INDIV) > 0;
    }

    public boolean isValid() {
        return (flag & FLAG_LIE) == 0 && (flag & FLAG_PLACEHOLDER) == 0;
    }

    public int getFlag() {
        return flag;
    }

    public String getReverseStr() {
        return isReverse() ? "y" : "n";
    }

    public String getMustAnswerStr() {
        return isMustAnswer() ? "y" : "n";
    }

    /**
     * <q id="Q4"><t>他（她）让我感到可靠吗？</t></q>
     */
    public String toXml() {
        StrBuilder sb = new StrBuilder(256);
        xmlOpen(sb);
        // 添加option选项，huangc
        xmlOptions(sb);
        xmlInter(sb);
        xmlClose(sb);
        return sb.toString();
    }

    protected void xmlOptions(StrBuilder sb) {
        if (this instanceof SelectionQuestion) {
            // List<Option> optionList = ((SelectionQuestion)this).getOptions();
            // for(int i=0;i<optionList.size();i++){
            // Option option = optionList.get(i);
            // sb.append("<option
            // id=").append(UtilMisc.quote(option.getId())).append("
            // value=").append(UtilMisc.quote(option.getValue())).append("
            // title=").append(UtilMisc.quote(option.getTitle())).append("/>\n");
            // }
            ((SelectionQuestion) this).setWriteOptXml(true);
        }
    }

    protected void xmlOpen(StrBuilder sb) {
        sb.append("<q ");
        if (getTypeMode() != QuestionConsts.TYPE_SELECTION_MODE) {
            sb.append(" type=").append(UtilMisc.quote(getType()));
        }
        sb.append(" id=").append(UtilMisc.quote(getId()));
        if (isReverse())
            sb.append(" reverse=").append(UtilMisc.quote(getReverseStr()));
        if (!isMustAnswer()) {
            sb.append(" mustAnswer=").append(UtilMisc.quote(getMustAnswerStr()));
        }
        if (StringUtils.isNotEmpty(getValidator())) {
            sb.append(" validator=").append(UtilMisc.quote(getValidator()));
        }
        if (StringUtils.isNotEmpty(getMsg())) {
            sb.append(" msg=").append(UtilMisc.quote(HtmlStr.htmlEncode(getMsg())));
        }
        if (StringUtils.isNotEmpty(getFormat())) {
            sb.append(" format=").append(UtilMisc.quote(getFormat()));
        }
        if (getChoiceMode() != QuestionConsts.CHOICE_SINGLE_MODE) {
            sb.append(" choice=").append(UtilMisc.quote(getChoice()));
        }
        if (StringUtils.isNotEmpty(descn)) {
            sb.append(" descn=").append(UtilMisc.quote(HtmlStr.htmlEncode(descn)));
        }
        // 下面的部分由huangc添加，在问题的头部添加占位符
        if (isTemplate()) {
            sb.append(" template=\"y\"");
        }
        if (isPlaceholder()) {
            sb.append(" placeholder=\"y\"");
        }
        // -----------------------
        sb.append(">\n");
    }

    protected void xmlInter(StrBuilder sb) {
        sb.append("<t>").append(HtmlStr.htmlEncode(getTitle())).append("</t>\n");
        if (StringUtils.isNotEmpty(getCalcExp())) {
            sb.append("\t").append("<c>").append(getCalcExp()).append("</c>\n");
        }
        // 前缀后缀
        if (StringUtils.isNotEmpty(prefix)) {
            sb.append("<prefix>").append(HtmlStr.htmlEncode(getPrefix())).append("</prefix>\n");
            sb.append("<postfix>").append(HtmlStr.htmlEncode(getPostfix())).append("</postfix>\n");
        }
        if (StringUtils.isNotEmpty(haspic)) {
            sb.append("<haspic>").append(HtmlStr.htmlEncode(getHaspic())).append("</haspic>\n");
        }
        if (StringUtils.isNotEmpty(headtitle)) {
            sb.append("<headtitle>").append(HtmlStr.htmlEncode(getHeadtitle())).append("</headtitle>\n");
        }
        if (StringUtils.isNotEmpty(limittime)) {
            sb.append("<limittime>").append(HtmlStr.htmlEncode(getLimittime())).append("</limittime>\n");
        }

    }

    protected void xmlInter1(StrBuilder sb) {
        sb.append("<t>").append(HtmlStr.htmlEncode(getTitle())).append("</t>\n");
        if (StringUtils.isNotEmpty(getCalcExp())) {
            sb.append("\t").append("<c>").append(getCalcExp()).append("</c>\n");
        }
        // 前缀后缀
        if (StringUtils.isNotEmpty(prefix)) {
            sb.append("<prefix>").append(HtmlStr.htmlEncode(getPrefix())).append("</prefix>\n");
            sb.append("<postfix>").append(HtmlStr.htmlEncode(getPostfix())).append("</postfix>\n");
        }

    }

    protected void xmlClose(StrBuilder sb) {
        sb.append("</q>\n");
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCalcExp() {
        return calcExp;
    }

    public void setCalcExp(String calcExp) {
        this.calcExp = calcExp;
    }

    public int getChoiceMode() {
        return choiceMode;
    }

    public int getTypeMode() {
        return typeMode;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public String getLimittime() {
        return limittime;
    }

    public void setLimittime(String limittime) {
        this.limittime = limittime;
    }

    public String getHaspic() {
        return haspic;
    }

    public void setHaspic(String haspic) {
        this.haspic = haspic;
    }

    public String[] getTemplateQeslist() {
        return templateQeslist;
    }

    public void setTemplateQeslist(String[] templateQeslist) {
        this.templateQeslist = templateQeslist;
    }

    public abstract String toHTML();

    public abstract String toHeadtitleHTML();

    public abstract String toAnswerHtml(int index);
}
