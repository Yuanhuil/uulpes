package heracles.web.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Extends Map providing only a different toString() method which can be used in
 * printing attributes inside an html tag.
 */
public class HtmlAttributeMap extends HashMap<String, Object> {
    private static final long serialVersionUID = 899149338534L;

    /**
     * Attribute value delimiter.
     */
    private static final char DELIMITER = '"';

    /**
     * character between name and value.
     */
    private static final char EQUALS = '=';

    /**
     * space before any attribute.
     */
    private static final char SPACE = ' ';

    /**
     * toString method: returns attributes in the format:
     * attributename="attributevalue" attr2="attrValue2" ...
     * 
     * @return String representation of the HtmlAttributeMap
     */
    public String toString() {
        // fast exit when no attribute are present
        if (size() == 0) {
            return StringUtils.EMPTY;
        }

        // buffer extimated in number of attributes * 30
        StringBuffer buffer = new StringBuffer(size() * 30);

        // iterates on attributes
        for (Map.Entry<String, Object> entry : this.entrySet()) {
            // append a new atribute
            buffer.append(SPACE).append(entry.getKey()).append(EQUALS).append(DELIMITER).append(entry.getValue())
                    .append(DELIMITER);
        }
        // return
        return buffer.toString();
    }
}
