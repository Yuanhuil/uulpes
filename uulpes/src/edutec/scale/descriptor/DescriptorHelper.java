package edutec.scale.descriptor;

import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import heracles.util.Resources;
import heracles.util.UtilCollection;

public class DescriptorHelper {
    private static final Log logger = LogFactory.getLog(DescriptorHelper.class);

    public static void main(String[] args) {
        // System.out.println(DescriptorHelper.queryMap("select descn from text4
        // where I1<=62 AND I2>=62",null));
    }

    public static Descriptor createDescriptor(String className) {
        Descriptor desc = (Descriptor) createObject(className);
        return desc;
    }

    public static EffectSupport createDimEffect(String className) {
        EffectSupport effect = (EffectSupport) createObject(className);
        return effect;
    }

    public static ResultSetHandler createResultSetHandler(String className) {
        ResultSetHandler hdl = (ResultSetHandler) createObject(className);
        return hdl;
    }

    @SuppressWarnings("unchecked")
    public static Map queryMap(String sql, Map map) {
        ResultSetHandler rshdl = new MapHandler();
        String sqlstr = UtilCollection.substitutStr(sql, map);
        Object o = DescnData.executeQuery(sqlstr, rshdl);
        return (Map) o;
    }

    @SuppressWarnings("unchecked")
    public static List queryList(String sql, Map map) {
        ResultSetHandler rshdl = new ArrayListHandler();
        String sqlstr = UtilCollection.substitutStr(sql, map);
        Object o = DescnData.executeQuery(sqlstr, rshdl);
        return (List) o;
    }

    private static Object createObject(String className) {
        Object o = null;
        try {
            o = Resources.instantiate(className);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (InstantiationException e) {
            logger.error("不能实例化对象:" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return o;
    }
}
