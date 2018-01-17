package edutec.scale.util.html;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import heracles.util.UtilMisc;

public class Button extends Input {
    private final static String HTML_BUTTON = "<input ${id} type='button' ${n} ${v} ${onclick} ${c}>";
    private String onClick;

    public Button() {
        super(null);
    }

    public Button(String name, String value, String js) {
        super(null);
        this.setName(name);
        this.setValue(value);
        this.onClick = js;
    }

    @Override
    public String getHtmlTemplate() {
        return HTML_BUTTON;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map buildSubstitutorMap() {
        String onclick = onClick != null ? "onclick=\"" + onClick + "\"" : StringUtils.EMPTY;
        return UtilMisc.toMap("v", getValue(), "onclick", onclick, "c", getStyleClass(), "n", getName(), "id", getId());
    }

    public String getOnClick() {
        return onClick;
    }

    public void setOnClick(String onclickjs) {
        this.onClick = onclickjs;
    }

}
