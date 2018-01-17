package heracles.enums;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.TransformedMap;

public class EnumUtils {
    static public Map<String, String> toMap(List<? extends EnumEntry> nvList) {
        Map<String, String> result = new LinkedHashMap<String, String>(nvList.size());
        for (EnumEntry nv : nvList) {
            result.put(nv.getName(), nv.getValue());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    static public Map<String, String> toMap(List<? extends EnumEntry> nvList, Transformer keyTransformer) {
        Map<String, String> result = new LinkedHashMap<String, String>(nvList.size());
        Map<String, String> map = TransformedMap.decorate(result, keyTransformer, null);
        for (EnumEntry nv : nvList) {
            map.put(nv.getName(), nv.getValue());
        }
        return result;
    }
}
