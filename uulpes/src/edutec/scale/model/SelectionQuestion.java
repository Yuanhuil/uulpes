package edutec.scale.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.StrBuilder;

import heracles.util.OrderValueMap;
import heracles.util.UtilCollection;
import heracles.web.util.HtmlStr;

/**
 * 选择题，是心理问卷的主要体型，本系统的设计需要将这类题型放入XML文件中，所以设置 这个题型的常用默认值，以便不写在XML中；
 * 其中包括type，choice，和size； size的默认值是option的数量
 * 
 * @author Administrator
 * 
 */

/**
 * @author admin
 */
public class SelectionQuestion extends Question implements Serializable, Calculable {
    /**
     * 
     */
    private static final long serialVersionUID = 3087799767555723916L;

    /**
     * 
     * 
     */
    public SelectionQuestion() {
        this.setType(QuestionConsts.TYPE_SELECTION);
        this.setChoice(QuestionConsts.CHOICE_SINGLE);
    }

    public SelectionQuestion(String id, Scale scale) {
        super(id, scale);
        this.setType(QuestionConsts.TYPE_SELECTION);
        this.setChoice(QuestionConsts.CHOICE_SINGLE);
    }

    public SelectionQuestion(String id) {
        super(id);
        this.setType(QuestionConsts.TYPE_SELECTION);
        this.setChoice(QuestionConsts.CHOICE_SINGLE);
    }

    private String onclick;
    private int size;
    private boolean writeOptXml;

    public boolean isWriteOptXml() {
        return writeOptXml;
    }

    public void setWriteOptXml(boolean writeOptXml) {
        this.writeOptXml = writeOptXml;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private OrderValueMap<String, Option> optionMap = new OrderValueMap<String, Option>();

    public OrderValueMap<String, Option> getOptionMap() {
        return optionMap;
    }

    public void setOptionMap(OrderValueMap<String, Option> optionMap) {
        this.optionMap = optionMap;
    }

    /**
     * 由于许多量表选择题的选项都是同样的内容，除了ID不同，为了节省内存，所以建立一个 <br>
     * Option对象池，如果有同等内容的Option对象，则从池中取;
     */
    private static Map<String, Option> optionPool = new HashMap<String, Option>();

    public void addOption(Option o) {
        /*
         * synchronized (optionPool) { Validate.notNull(o, "Option对象不能为空");
         * String id = o.getId(); String key = o.toStringWithoutId(); if
         * (!optionPool.containsKey(key)) { optionPool.put(key, o); } o = null;
         * o = optionPool.get(key); optionMap.put(id, o); }
         */
        String id = o.getId();
        optionMap.put(id, o);
    }

    public List<Option> getOptions() {
        return optionMap.valueList();
    }

    public int optionSize() {
        return getOptions().size();
    }

    /**
     * 根据位置得到Option对象
     * 
     * @param position
     * @return
     */
    public Option findOption(int position) {
        Option option = getOptions().get(position);
        Validate.notNull(option, "选项 position." + position + " 在此'" + this.getId() + "'选择题中没有.");
        return option;
    }

    /**
     * 根据Id号获得Option对象
     * 
     * @param optionId
     * @return
     */

    public Option findOption(String optionId) {
        Option option = optionMap.get(optionId);
        Validate.notNull(option, "选项 No." + optionId + " 在此'" + this.getId() + "'选择题中没有.");
        return option;
    }

    public void print() {

    }

    @Override
    public String toString() {
        StrBuilder sb = new StrBuilder(128);
        sb.append("id=\"" + this.getId());
        sb.append("\" type=" + this.getType());
        sb.append("\" choice=" + this.getChoice());
        sb.append("\" title=\"" + this.getTitle());
        if (StringUtils.isNotBlank(getCalcExp()))
            sb.append("\" calcExp=" + this.getCalcExp());
        if (isReverse())
            sb.append("\" reverse=\"").append(getReverseStr());
        sb.append("\"");
        sb.append("\n");
        sb.append("---------options----------------\n");
        Collection<Option> options = getOptions();
        for (Option o : options) {
            sb.append(o.toString()).append("\n");
        }
        sb.append("--------------------------------\n");
        return sb.toString();
    }

    // huangc,返回到前台页面的html
    @Override
    public String toHTML() {
        StringBuilder sb = new StringBuilder();
        String tempid = "";
        // 针对各种题型的处理，需要把前面的标志位去掉
        if (this.getId().contains("Q")) {
            tempid = this.getId().substring(1);
        }
        sb.append("<h4>" + tempid + "." + HtmlStr.decodeString(this.getTitle()) + "</h4>");
        sb.append("<br>");
        sb.append("<p>");
        Collection<Option> options = getOptions();
        if (StringUtils.isNotEmpty(getPrefix()))
            sb.append(getPrefix());
        for (Option o : options) {
            sb.append("<span style='margin-left:20px'>" + o.getId() + "." + HtmlStr.decodeString(o.getTitle())
                    + "</span>");
        }
        if (StringUtils.isNotEmpty(getPostfix()))
            sb.append("<span style='margin-left:20px'>" + getPostfix());
        sb.append("</p>");
        sb.append("<br>");
        return sb.toString();
    }

    @Override
    public String toAnswerHtml(int index) {
        StringBuilder sb = new StringBuilder();
        String tempid = "";
        // 针对各种题型的处理，需要把前面的标志位去掉
        if (this.getId().contains("Q")) {
            tempid = this.getId().substring(1);
        }
        sb.append("<h4>" + tempid + "." + this.getTitle() + "</h4>");
        sb.append("<br>");
        sb.append("<p>");
        if (StringUtils.isNotEmpty(getPrefix()))
            sb.append(getPrefix());
        Collection<Option> options = getOptions();
        int n = 0;
        for (Option o : options) {
            if (n == index - 1)
                sb.append("<span style='margin-left:20px'>" + "<input type='radio' checked='checked'/>"
                        + HtmlStr.decodeString(o.getTitle()) + "</span>");
            else
                sb.append("<span style='margin-left:20px'>" + "<input type='radio'/>"
                        + HtmlStr.decodeString(o.getTitle()) + "</span>");
            n++;
        }
        if (StringUtils.isNotEmpty(getPostfix()))
            sb.append("<span style='margin-left:20px'>" + getPostfix());
        sb.append("</p>");
        sb.append("<br>");
        return sb.toString();
    }

    @Override
    protected void xmlInter(StrBuilder sb) {
        super.xmlInter(sb);
        if (writeOptXml) {
            Collection<Option> options = getOptions();
            for (Option o : options) {
                sb.append("\t");
                sb.append(o.toXml());
                sb.append("\n");
            }
        }
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    /**
     * selection|A,1,完全不符合|B,2,多数不符合|C,3,一般/不确定|D,4,多数符合|E,5,完全符合
     * 
     * @return
     */
    public String optionsToString() {
        StrBuilder sb = new StrBuilder("selection|");
        Iterator<Option> iter = optionMap.values().iterator();
        while (iter.hasNext()) {
            Option opt = iter.next();
            sb.append(opt.getId()).append(",");
            sb.append(opt.getValue()).append(",");
            sb.append(opt.getTitle());
            if (iter.hasNext()) {
                sb.append("|");
            }
        }
        return sb.toString();
    }

    public void setOptionContent(String str) {
        List<List<String>> lists = UtilCollection.toStringList(str, '|', ',');
        for (List<String> list : lists) {
            Option option = new Option(list.get(0), list.get(1), list.get(2));
            this.addOption(option);
        }
    }

    public String getOptionContent() {
        StrBuilder sb = new StrBuilder();
        Iterator<Option> iter = optionMap.values().iterator();
        while (iter.hasNext()) {
            Option opt = iter.next();
            sb.append(opt.getTitle()).append("=");
            sb.append(opt.getValue());
            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public String[] getOptionTitles() {
        Iterator<Option> iter = optionMap.values().iterator();
        String[] result = new String[optionMap.values().size()];
        int idx = 0;
        while (iter.hasNext()) {
            Option opt = iter.next();
            result[idx] = opt.getTitle();
            idx++;
        }
        return result;
    }

    @Override
    public String toHeadtitleHTML() {
        StringBuilder sb = new StringBuilder();
        String tempid = "";
        // 针对各种题型的处理，需要把前面的标志位去掉
        if (this.getId().contains("Q")) {
            tempid = "" + this.getId().charAt(1);
        }
        sb.append("<h3>" + tempid + "." + HtmlStr.decodeString(this.getHeadtitle()) + "</h3>");
        sb.append("<br>");
        return sb.toString();
    }
}
