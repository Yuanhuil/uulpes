package heracles.digester;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

public final class DigesterUtils {
    /**
     * <x> <a></a> <b></b> </x> 设置栈顶对象的a和b的属性
     * 
     * @param digester
     * @param patterns
     */
    public static void addBeanPropertySetters(Digester digester, String[] patterns) {
        for (int i = 0; i < patterns.length; ++i) {
            digester.addBeanPropertySetter(patterns[i]);
        }
    }

    /**
     * 创建栈顶对象，并将其加入到原栈顶对象的属性中，使用methodName方法； 用了三次pattern
     * 
     * @param digester
     * @param pattern
     *            匹配的模式
     * @param clazz
     *            需建立的类
     * @param methodName
     *            将所建立的类的对象，被堆栈中的第二个对象所用
     */
    public static void add3(Digester digester, String pattern, Class<?> clazz, String methodName) {
        /* 创建对象，并加入栈顶 */
        digester.addObjectCreate(pattern, clazz);
        /*
         * 设置栈顶对象的属性 形如：<id x="A" y="B"> ,其中x和y就是所要访问的属性
         */
        digester.addSetProperties(pattern);
        /* 将栈顶对象加入到，栈顶对象下一个对象的所属集合中 */
        digester.addSetNext(pattern, methodName);
    }

    /**
     * 创建栈顶对象，并将其加入到原栈顶对象的属性中，使用methodName方法； 用了三次pattern
     * 
     * @param digester
     * @param pattern
     *            匹配的模式
     * @param clazz
     *            需建立的类
     * @param methodName
     *            将所建立的类的对象，被堆栈中的第二个对象所用
     */
    public static void add3(Digester digester, String pattern, Rule createRule, String methodName) {
        /* 创建对象，并加入栈顶 */
        digester.addRule(pattern, createRule);
        /*
         * 设置栈顶对象的属性 形如：<id x="A" y="B"> ,其中x和y就是所要访问的属性
         */
        digester.addSetProperties(pattern);
        /* 将栈顶对象加入到，栈顶对象下一个对象的所属集合中 */
        digester.addSetNext(pattern, methodName);
    }

    public static void populateProps(Object bean, Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); i++) {
            String lname = attributes.getLocalName(i);
            if ("".equals(lname)) {
                lname = attributes.getQName(i);
            }
            String value = attributes.getValue(i);
            try {
                BeanUtils.setProperty(bean, lname, value);
            } catch (Exception e) {
            }
        }
    }
}
