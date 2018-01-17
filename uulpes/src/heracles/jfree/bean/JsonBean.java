package heracles.jfree.bean;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonBean {

    private JSONObject Json;

    public JsonBean() {
        Json = new JSONObject();
    }

    public JsonBean(String jsonStr) {
        if (StringUtils.isNotEmpty(jsonStr)) {
            Json = JSONObject.fromObject(jsonStr);
        } else {
            Json = new JSONObject();
        }
    }

    public Object get(Object key) {
        return Json.get(key);
    }

    public Object get(String key) {
        return Json.get(key);
    }

    public boolean getBoolean(String key) {
        return Json.getBoolean(key);
    }

    public double getDouble(String key) {
        return Json.getDouble(key);
    }

    public int getInt(String key) {
        return Json.getInt(key);
    }

    public JSONArray getJSONArray(String key) {
        return Json.getJSONArray(key);
    }

    public JSONObject getJSONObject(String key) {
        return Json.getJSONObject(key);
    }

    public long getLong(String key) {
        return Json.getLong(key);
    }

    public String getString(String key) {
        return Json.getString(key);
    }

    public boolean has(String key) {
        return Json.has(key);
    }

    public Object put(Object key, Object value) {
        return Json.put(key, value);
    }

    public String toJson() {
        return Json.toString();
    }

    @SuppressWarnings("unchecked")
    public void putAll(Map map, JsonConfig jsonConfig) {
        Json.putAll(map, jsonConfig);
    }

    @SuppressWarnings("unchecked")
    public void putAll(Map map) {
        Json.putAll(map);
    }

}
