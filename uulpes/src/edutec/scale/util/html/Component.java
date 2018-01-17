package edutec.scale.util.html;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;

public abstract class Component {
    private String id;
    private String value;
    private String styleClass;
    @SuppressWarnings("unchecked")
    private Map map;

    public Component(final String id) {
        this.id = id;
    }

    public String getId() {
        if (id == null) {
            return StringUtils.EMPTY;
        }
        return "id='" + id + "'";
    }

    public String getValue() {
        if (value == null) {
            return StringUtils.EMPTY;
        }
        return "value='" + value + "'";
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toHtml() {
        StrSubstitutor ss = new StrSubstitutor(getSubstitutorMap());
        return ss.replace(getHtmlTemplate());
    }

    @SuppressWarnings("unchecked")
    public Map getSubstitutorMap() {
        if (map == null) {
            map = buildSubstitutorMap();
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public abstract Map buildSubstitutorMap();

    public abstract String getHtmlTemplate();

    public String getStyleClass() {
        return StringUtils.defaultIfEmpty(styleClass, StringUtils.EMPTY);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public void setId(String id) {
        this.id = id;
    }
}
