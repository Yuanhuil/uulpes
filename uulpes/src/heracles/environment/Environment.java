package heracles.environment;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Environment {
    private final static Log logger = LogFactory.getLog(Environment.class);
    private static Environment instance = new Environment();
    private Map<Class<?>, Object> instances;

    public static Environment getInstance() {
        return instance;
    }

    public static Environment $() {
        return getInstance();
    }

    @SuppressWarnings("unchecked")
    public <T> T instantiate(Class<T> clazz) {
        Object o = instances.get(clazz);
        if (o == null) {
            logger.error("RegistException");
        }
        return (T) o;
    }

    public Map<Class<?>, Object> getInstances() {
        return instances;
    }

    public void setInstances(Map<Class<?>, Object> instances) {
        this.instances = instances;
    }
}
