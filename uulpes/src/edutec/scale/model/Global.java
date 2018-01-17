package edutec.scale.model;

import java.util.Map;
import java.util.TreeMap;

public class Global {
    Map<String, String> props;
    Map<String, String> category = new TreeMap<String, String>();

    public Map<String, String> getCategory() {
        return category;
    }

    public void setCategory(Map<String, String> catalog) {
        this.category = catalog;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

}
