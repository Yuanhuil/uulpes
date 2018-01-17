package com.njpes.www.service.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.util.FieldMapper;
import com.njpes.www.entity.baseinfo.FieldConfig;

/**
 * @author zhangchao
 *
 */
/**
 * @author zhangchao
 */
@Service("FieldService")
public class FieldServiceImpl implements FieldServiceI {

    @Autowired
    private FieldMapper fieldMapper;

    @Override
    public List<Map> selectAllDic(String field, String table) {
        // TODO Auto-generated method stub
        return fieldMapper.selectAllDic(field, table);
    }

    @Override
    public List<Map> selectAllDicWhere(String field, String table, String where) {
        // TODO Auto-generated method stub
        return fieldMapper.selectAllDicWhere(field, table, where);
    }

    @Override
    public Map<String, Map> getFieldName(List list, List<FieldConfig> NFTW) {
        Map<String, Map> map = new HashMap<String, Map>();
        Map<String, FieldConfig> NFTWMap = new HashMap<String, FieldConfig>();
        for (String fieldConfig : NFTWMap.keySet()) {

        }
        for (FieldConfig fieldConfig : NFTW) {
            NFTWMap.put(fieldConfig.getField(), fieldConfig);
            for (Object obj : list) {

                Object v = getFieldValueByName(obj, fieldConfig.getField());
                if (v != null && v.toString().trim().length() > 0) {

                    fieldConfig.setValue(fieldConfig.getValue() + "," + v.toString().trim());

                }
            }
        }

        for (FieldConfig fieldConfig : NFTW) {
            String valuse = fieldConfig.getValue().trim();
            if (!valuse.equals("")) {
                String where = fieldConfig.getWhere();

                valuse = valuse.substring(1, valuse.length());
                if (fieldConfig.isString()) {
                    where = where + fieldConfig.getFieldKey() + " in ('" + valuse.replaceAll(",", "','") + "')";
                } else {
                    where = where + fieldConfig.getFieldKey() + " in (" + valuse + ")";
                }

                List<Map> mapO = selectAllDicWhere(fieldConfig.getFieldKey() + "," + fieldConfig.getFieldValue(),
                        fieldConfig.getTableName(), where);
                Map<Object, Object> map1 = new HashMap<Object, Object>();
                for (Map map2 : mapO) {
                    if (fieldConfig.getKeyType() != null) {
                        if (fieldConfig.getKeyType().equals("int")) {
                            map1.put(Integer.parseInt(map2.get(fieldConfig.getFieldKey()).toString()),
                                    map2.get(fieldConfig.getFieldValue()));
                        }
                        if (fieldConfig.getKeyType().equals("string")) {
                            map1.put(map2.get(fieldConfig.getFieldKey()) + "", map2.get(fieldConfig.getFieldValue()));
                        }

                    } else {
                        map1.put(map2.get(fieldConfig.getFieldKey()), map2.get(fieldConfig.getFieldValue()));
                    }

                }
                map.put(fieldConfig.getField(), map1);
            }
            ;
        }

        return map;
    }

    /**
     * 根据属性名获取属性值
     */
    private static Object getFieldValueByName(Object o, String fieldName) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {

            return null;
        }
    }

}
