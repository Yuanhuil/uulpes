package edutec.scale.model;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import heracles.web.util.HtmlStr;

public class Option extends TitleableSupport implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4685246247552542925L;
    private String id;
    private String title;
    private String value;
    private int choiceMode;
    private String descn;

    public Option() {
        choiceMode = QuestionConsts.CHOICE_SINGLE_MODE;
    }

    public Option(String id, String value) {
        this();
        this.id = id;
        this.value = value;
    }

    public Option(String id, String value, String title) {
        this();
        this.id = id;
        this.value = value;
        this.title = title;
    }

    public String getChoice() {
        return QuestionConsts.CHOICE_TYPE[choiceMode];
    }

    public void setChoice(String choiceType) {
        this.choiceMode = ArrayUtils.indexOf(QuestionConsts.CHOICE_TYPE, choiceType);
    }

    public int getChoiceMode() {
        return choiceMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        String titleStr = HtmlStr.decodeString(title);
        return titleStr;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescn() {
        return descn;
    }

    public String toStringWithoutId() {
        StrBuilder sb = new StrBuilder(50);
        sb.append(StringUtils.trimToEmpty(getTitle()));
        sb.append(StringUtils.trimToEmpty(getValue()));
        sb.append(StringUtils.trimToEmpty(getDescn()));
        return sb.toString();
    }

    public boolean equalsWithoutId(Option obj) {
        // boolean b1 = StringUtils.trimToEmpty(getSn()).equals(obj.getSn());
        boolean b2 = StringUtils.trimToEmpty(getTitle()).equals(obj.getTitle());
        boolean b3 = StringUtils.trimToEmpty(getValue()).equals(obj.getValue());
        // return b1 && b2 && b3;
        return b2 && b3;
    }

    @Override
    public String toString() {
        StrBuilder sb = new StrBuilder(50);
        sb.append("id=" + this.id);
        sb.append(" value=" + this.value);
        sb.append(" title=" + this.title);
        return sb.toString();
    }

    public String toXml() {
        StrBuilder sb = new StrBuilder(64);
        sb.append("<o id=\"").append(this.id);
        sb.append("\" value=\"").append(this.value);
        // sb.append("\">").append(HtmlStr.htmlEncode(title)).append("</o>");
        sb.append("\">").append(HtmlStr.htmlEncode(title)).append("</o>");
        return sb.toString();
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }
}
