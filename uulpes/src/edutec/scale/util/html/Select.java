package edutec.scale.util.html;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import heracles.util.UtilMisc;

public class Select extends Component {
    private static final String HTML_OPEN_SELECT = "<select ${id} ${m} ${c} ${oc}>\n";
    private static final String HTML_SEL_MULT = "multiple";
    private static final String HTML_CLOSE_SELECT = "</select>\n";
    private boolean multiple = true;
    private SelectOption[] options;
    private String onChange;

    public Select(String id) {
        super(id);
    }

    public String getOnChange() {
        if (StringUtils.isNotEmpty(onChange)) {
            StrBuilder sb = new StrBuilder(32);
            sb.append("onchange=\"").append(onChange).append("\"");
            return sb.toString();
        }
        return StringUtils.EMPTY;
    }

    public void setOnChange(String onchangeJs) {
        this.onChange = onchangeJs;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public SelectOption[] getOptions() {
        return options;
    }

    public void setOptions(SelectOption[] options) {
        this.options = options;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map buildSubstitutorMap() {
        String multi = this.isMultiple() ? HTML_SEL_MULT : StringUtils.EMPTY;
        return UtilMisc.toMap("id", getId(), "m", multi, "c", getStyleClass(), "oc", getOnChange());
    }

    @Override
    public String getHtmlTemplate() {
        StrBuilder sb = new StrBuilder(128);
        sb.append(HTML_OPEN_SELECT);
        for (int i = 0; i < options.length; ++i) {
            sb.append(options[i].toHtml());
        }
        sb.append(HTML_CLOSE_SELECT);
        return sb.toString();
    }

}
