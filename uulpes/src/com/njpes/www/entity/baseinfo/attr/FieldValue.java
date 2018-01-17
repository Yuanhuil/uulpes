package com.njpes.www.entity.baseinfo.attr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class FieldValue {
    public Field getField() {
        return field;
    }

    public String getDataSource() {
        return field.getOptDataSource();
    }

    public String getFname() {
        return field.getFname();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    private final Field field;
    private String value;

    public FieldValue(Field field) {
        this.field = field;
    }

    public String getValue() {
        return StringUtils.defaultString(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return field.getType();
    }

    public String getId() {
        return field.getId();
    }

    public String getLabel() {
        return field.getLabel();
    }

    public String getName() {
        return field.getName();
    }

    public List<String> getDependList() {
        return field.getDependList();
    }

    public KeyValue getIdAndValue() {
        return new DefaultKeyValue(this.getId(), this.getValue());
    }

    public static Map<String, String> listToMap(List<FieldValue> fvList) {
        Map<String, String> map = new LinkedHashMap<String, String>(fvList.size());
        for (FieldValue fv : fvList) {
            map.put(fv.getId(), fv.getValue());
        }
        return map;
    }

    public static List<String> extractTitles(List<FieldValue> fvList) {
        List<String> list = new ArrayList<String>(fvList.size());
        for (FieldValue fv : fvList) {
            list.add(fv.getLabel());
        }
        return list;
    }

    public List<SelectOption> getOptionList() {
        return field.getOptionList();
    }

    public boolean isRquest() {
        return field.isRquest();
    }
}
