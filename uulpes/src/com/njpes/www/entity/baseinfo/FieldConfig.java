package com.njpes.www.entity.baseinfo;

public class FieldConfig {
    /* 属性名 */
    private String field;
    /* 表key字段名 */
    private String fieldKey;
    /* 表value字段名 */
    private String fieldValue;
    /* 表名 */
    private String tableName;
    /* 表key字段是否是字符串 */
    private boolean isString;
    /* where条件 */
    private String where;
    /* field值拼成的字符串 */
    private String value;

    /* keyType 将key的类型转换成指定的类型 */
    private String keyType;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * @param field
     *            属性名
     * @param fieldK
     *            表key字段名
     * @param fieldV
     *            value字段名
     * @param tableName
     *            表名
     * @param isString
     *            表key字段是否是字符串
     * @param where
     *            where条件
     */
    public FieldConfig(String field, String fieldK, String fieldV, String tableName, boolean isString, String where) {
        super();
        this.field = field;
        this.fieldKey = fieldK;
        this.fieldValue = fieldV;
        this.tableName = tableName;
        this.isString = isString;
        this.where = where;
        this.value = "";
        this.keyType = null;
    }

    /**
     * @param field
     *            属性名
     * @param fieldK
     *            表key字段名
     * @param fieldV
     *            value字段名
     * @param tableName
     *            表名
     * @param isString
     *            表key字段是否是字符串
     * @param where
     *            where条件
     */
    public FieldConfig(String field, String fieldK, String fieldV, String tableName, boolean isString, String where,
            String keyType) {
        super();
        this.field = field;
        this.fieldKey = fieldK;
        this.fieldValue = fieldV;
        this.tableName = tableName;
        this.isString = isString;
        this.where = where;
        this.value = "";
        this.keyType = keyType;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isString() {
        return isString;
    }

    public void setString(boolean isString) {
        this.isString = isString;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }
}
