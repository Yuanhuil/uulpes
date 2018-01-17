package heracles.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.utility.OptimizerUtil;

/**
 * @author huangchao 画蛇添足的结构，直接用linkedhashmap就可以，可见掌握java每种容器的使用多么的重要
 * @param <K>
 * @param <V>
 */
public class OrderValueMap<K, V> extends LinkedHashMap<K, V> implements Serializable {
    private static final long serialVersionUID = -2440070766056872686L;
    private List<V> valuesList = new ArrayList<V>();

    @Override
    public Collection<V> values() {
        return valueList();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
        if (m != null) {
            valuesList.addAll(m.values());
            optimizeListStorage();
        }
    }

    @Override
    public V put(K key, V value) {
        V v = super.put(key, value);
        if (v != null) {
            valuesList.remove(v);
        }
        valuesList.add(value);
        return v;
    }

    @Override
    public V remove(Object key) {
        V e = super.remove(key);
        if (e != null) {
            valuesList.remove(e);
        }
        return e;
    }

    public void optimizeListStorage() {
        OptimizerUtil.optimizeListStorage(valuesList);
    }

    @Override
    public void clear() {
        super.clear();
        this.valuesList.clear();
    }

    public List<V> valueList() {
        return Collections.unmodifiableList(valuesList);
    }
}
