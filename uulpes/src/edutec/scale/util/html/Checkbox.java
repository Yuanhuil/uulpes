package edutec.scale.util.html;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import heracles.util.UtilMisc;

public class Checkbox extends Input {
    private final static String HTML_CHKBOX = "<input type='checkbox' ${n} ${v} ${onclick} ${c}>";
    private String onClick;

    public Checkbox(String name) {
        super(null);
        this.setName(name);
    }

    public Checkbox(String name, String onclickjs) {
        this(name);
        this.onClick = onclickjs;
    }

    public Checkbox(String name, String value, String onclickjs) {
        this(name, onclickjs);
        this.setValue(value);
    }

    public String getOnClick() {
        return onClick != null ? "onclick=\"" + onClick + "\"" : StringUtils.EMPTY;
    }

    public void setOnClick(String onclickjs) {
        this.onClick = onclickjs;
    }

    @Override
    public String getHtmlTemplate() {
        return HTML_CHKBOX;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map buildSubstitutorMap() {
        return UtilMisc.toMap("n", getName(), "v", getValue(), "onclick", getOnClick(), "c", getStyleClass());
    }
}
