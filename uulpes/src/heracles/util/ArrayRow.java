package heracles.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.ArrayIterator;

@SuppressWarnings("unchecked")
public class ArrayRow implements Map, Iterator, Serializable {
    private Object values[] = new Object[12];
    private int objectNum = 0;
    private Iterator iter;
    Transformer transformer;

    public void clear() {

    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public Set entrySet() {
        return null;
    }

    public Object get(Object key) {
        if (key instanceof Integer) {
            Integer idx = (Integer) key;
            return values[idx];
        }
        return null;
    }

    public boolean isEmpty() {
        return false;
    }

    public Set keySet() {
        return null;
    }

    public Object put(Object key, Object value) {
        values[objectNum++] = value;
        return value;
    }

    public void putAll(Map m) {

    }

    public int size() {
        return objectNum;
    }

    public Object[] getValues() {
        Object[] result = new Object[this.objectNum];
        for (int k = 0; k < objectNum; ++k) {
            if (transformer != null)
                result[k] = transformer.transform(values[k]);
            else {
                result[k] = values[k];
            }
        }
        return result;
    }

    public Object[] getRawValues() {
        return values;
    }

    public int getObjectNum() {
        return objectNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("val_num:").append(objectNum);
        sb.append("||");
        for (Object val : getValues()) {
            sb.append(val).append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public boolean hasNext() {
        if (iter == null) {
            iter = new ArrayIterator(getValues());
        }
        return iter.hasNext();
    }

    public Object next() {
        return iter.next();
    }

    public void remove() {
    }

    public Object remove(Object key) {
        return null;
    }

    public Collection values() {
        return Arrays.asList(values);
    }

}
