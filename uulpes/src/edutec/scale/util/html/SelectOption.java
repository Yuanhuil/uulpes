package edutec.scale.util.html;

import java.util.Map;

import heracles.util.UtilMisc;

public class SelectOption extends Component {
    private static final String HTML_OPTION = "\t<option ${v}>${t}</option>\n";
    @SuppressWarnings("unchecked")
    private Map map;

    public SelectOption(String value, String text) {
        super(null);
        this.setValue(value);
        map = UtilMisc.toMap("v", getValue(), "t", text);
    }

    @Override
    public String getHtmlTemplate() {
        return HTML_OPTION;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map buildSubstitutorMap() {
        return map;
    }

}
