package heracles.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool;

@SuppressWarnings("unchecked")
public class Pools {
    private final Log logger = LogFactory.getLog(getClass());
    static Pools pools = new Pools();

    public static Pools getInstance() {
        return pools;
    }

    private StackObjectPool stringBuilderPool = new StackObjectPool(new StringBuilderFactory(), 256);
    private StackObjectPool strBuilderPool = new StackObjectPool(new StrBuilderFactory(), 256);
    private StackObjectPool hashMapPool = new StackObjectPool(new HashMapFactory(), 512);
    private StackObjectPool stringListPool = new StackObjectPool(new ListFactory(), 256);
    private StackObjectPool stringMapPool = new StackObjectPool(new StringMapFactory(), 256);

    public Map<String, String> borrowStringMap() {
        try {
            LogUtils.debug(logger, "borrowStringMap...");
            return (Map<String, String>) stringMapPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void returnStringMap(Map<String, String> map) {
        if (map != null) {
            try {
                LogUtils.debug(logger, "returnStringMap...");
                stringMapPool.returnObject(map);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

    }

    public List<String> borrowStringList() {
        try {
            LogUtils.debug(logger, "borrowStringList...");
            return (ArrayList<String>) stringListPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void returnStringList(List<String> list) {
        if (list != null) {
            try {
                LogUtils.debug(logger, "returnStringList...");
                stringListPool.returnObject(list);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

    }

    public Map<Object, Object> borrowMap() {
        try {
            LogUtils.debug(logger, "borrowMap...");
            return (Map<Object, Object>) hashMapPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void returnMap(Map<Object, Object> objMap) {
        if (objMap != null) {
            try {
                LogUtils.debug(logger, "returnMap...");
                hashMapPool.returnObject(objMap);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

    }

    public StrBuilder borrowStrBuilder() {
        try {
            LogUtils.debug(logger, "borrowStrBuilder...");
            return (StrBuilder) strBuilderPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void returnStrBuilder(StrBuilder obj) {
        if (obj != null) {
            try {
                LogUtils.debug(logger, "returnStrBuilder...");
                strBuilderPool.returnObject(obj);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

    }

    public StringBuilder borrowStringBuilder() {
        try {
            LogUtils.debug(logger, "borrowStringBuilder...");
            return (StringBuilder) stringBuilderPool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public StringBuilder borrowStringBuilder(int minimumCapacity) {
        StringBuilder sb = borrowStringBuilder();
        if (sb != null) {
            sb.ensureCapacity(minimumCapacity);
        }
        return sb;
    }

    public void returnStringBuilder(StringBuilder sb) {
        try {
            if (sb != null) {
                LogUtils.debug(logger, "returnStringBuilder...");
                stringBuilderPool.returnObject(sb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private class StringBuilderFactory extends BasePoolableObjectFactory {
        public Object makeObject() {
            return new StringBuilder();
        }

        public void passivateObject(Object obj) {
            StringBuilder buf = (StringBuilder) obj;
            buf.setLength(0);
        }

    }

    private class StrBuilderFactory extends BasePoolableObjectFactory {
        public Object makeObject() {
            return new StrBuilder();
        }

        public void passivateObject(Object obj) {
            StrBuilder buf = (StrBuilder) obj;
            buf.clear();
        }

    }

    private class HashMapFactory extends BasePoolableObjectFactory {
        public Object makeObject() {
            return new LinkedHashMap<Object, Object>();
        }

        public void passivateObject(Object obj) {
            HashMap buf = (HashMap) obj;
            buf.clear();
        }
    }

    private class ListFactory extends BasePoolableObjectFactory {
        public Object makeObject() {
            return new ArrayList<String>();
        }

        public void passivateObject(Object obj) {
            ArrayList buf = (ArrayList) obj;
            buf.clear();
        }

    }

    private class StringMapFactory extends BasePoolableObjectFactory {
        public Object makeObject() {
            return new LinkedHashMap<String, String>();
        }

        public void passivateObject(Object stringMap) {
            HashMap buf = (HashMap) stringMap;
            buf.clear();
        }
    }

}
