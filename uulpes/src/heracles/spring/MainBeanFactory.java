package heracles.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;

@SuppressWarnings("unchecked")
public class MainBeanFactory {
    protected final Log logger = LogFactory.getLog(MainBeanFactory.class);

    public MainBeanFactory(Resource resource) {
        try {
            factory = new XmlBeanFactory(resource);
        } catch (Throwable e) {
            logger.error(e);
        }
    }

    private XmlBeanFactory factory;

    public <T> T getBean(Class<?> type) {
        return (T) getBean(type.getName());
    }

    public <T> T getBean(Class<?> type, boolean logFailures) {
        return (T) getBean(type.getName(), logFailures);
    }

    public <T> T getBean(String name) {
        return (T) factory.getBean(name);
    }

    public <T> T getBean(String name, boolean logFailures) {
        try {
            return getBean(name);
        } catch (BeansException e) {
            if (logFailures)
                logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public void setBean(Class type, Object singleton) {
        ((ConfigurableBeanFactory) factory).registerSingleton(type.getName(), singleton);
    }

    public void setBean(String name, Object singleton) {
        ((ConfigurableBeanFactory) factory).registerSingleton(name, singleton);
    }

    public void initBeanProperties(Object bean) {
        ((AutowireCapableBeanFactory) factory).autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
                true);
    }

    public void registerBeanDefinition(Class typeName, Class beanClass) {
        ((DefaultListableBeanFactory) factory).registerBeanDefinition(typeName.getName(),
                new RootBeanDefinition(beanClass, RootBeanDefinition.AUTOWIRE_AUTODETECT));
    }
}
