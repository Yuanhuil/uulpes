package com.njpes.www.entity.baseinfo.attr;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import heracles.util.UtilCollection;

/**
 * 
 * 
 */
public class PropBean {
    protected static final String ATTR_OBJECT_SOURCE = "object-source";
    protected static final String ATTR_FIELDS = "fields";
    protected static final String ATTR_REQUIRED = "required";

    private Map<String, Object> props;

    public PropBean() {
    }

    public PropBean(String iniStr) throws IllegalAccessException, InvocationTargetException {
        if (StringUtils.isEmpty(iniStr))
            return;
        Map<String, String> ss = UtilCollection.compositConvertMap(iniStr, UtilCollection.SEMICOLON, UtilCollection.EQ);
        BeanUtils.populate(this, ss);
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public void setProps(Map<String, ? extends Object> attrs) {
        if (MapUtils.isNotEmpty(attrs)) {
            getPropMap_().putAll(attrs);
        }
    }

    public Object getProp(String key) {
        Object obj = null;
        if (props != null) {
            obj = props.get(key);
        }
        if (obj == null) {
            try {
                obj = BeanUtils.getProperty(this, key);
            } catch (Exception e) {
            }
        }
        return obj;
    }

    public String getPropString(String key) {
        return StringUtils.defaultString((String) getProp(key));
    }

    public int getPropInt(String key) {
        Integer iv = (Integer) getProp(key);
        if (iv == null) {
            return 0;
        }
        return iv.intValue();
    }

    public void setProp(String key, Object value) {
        getPropMap_().put(key, value);
    }

    public void setPropsByBean(PropBean props) {
        getPropMap_().putAll(props.getProps());
    }

    public void setPropsByStr(String str) {
        if (StringUtils.isEmpty(str)) {
            return;
        }
        Map<String, String> ss = UtilCollection.compositConvertMap(str, UtilCollection.SEMICOLON, UtilCollection.EQ);
        getPropMap_().putAll(ss);
    }

    /**
     * setPropsByStr的别名
     * 
     * @param str
     */
    public void setPageVars(String str) {
        setPropsByStr(str);
    }

    /**
     * getProp(key)的别名
     * 
     * @param key
     * @return
     */
    public Object getPageVar(String key) {
        return this.getProp(key);
    }

    /**
     * getPropString(key)的别名
     * 
     * @param key
     * @return
     */
    public Object getPageVarStr(String key) {
        return this.getPropString(key);
    }

    public void clear() {
        if (!MapUtils.isEmpty(props)) {
            props.clear();
        }
    }

    private Map<String, Object> getPropMap_() {
        if (props == null)
            props = new HashMap<String, Object>();
        return props;

    }
}
