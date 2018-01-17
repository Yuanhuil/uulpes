package edutec.scale.util.html;

import java.util.Map;

public class Textarea extends Component {
    private final static String HTML_TEXTAREA = "<input  type=${t} ${id} >";

    public Textarea(String id) {
        super(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map buildSubstitutorMap() {
        return null;
    }

    @Override
    public String getHtmlTemplate() {
        return HTML_TEXTAREA;
    }

}
