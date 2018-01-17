package edutec.scale.util.html;

import org.apache.commons.lang.StringUtils;

public abstract class Input extends Component {
    private String name;

    public String getName() {
        return name != null ? "name=\"" + name + "\"" : StringUtils.EMPTY;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Input(String id) {
        super(id);
    }
}
