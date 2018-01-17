package edutec.scale.descriptor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edutec.scale.db.ScaleEvaluate;
import edutec.scale.descriptor.DescnDataHelper.DbSql;
import heracles.db.sql.SimpleSQL;
import heracles.util.UtilCollection;

public class DescnData {
    public static void main(String[] args) {
        DescnData descnData;
        try {
            descnData = new DescnData();
            descnData.testSql();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final Log logger = LogFactory.getLog(DescnData.class);

    private String name;
    private String type;
    private String factor;
    private String fields;
    private String descn;
    private String where;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.toLowerCase();
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public DescnData() throws Exception {
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void createSchema() throws Exception {
        String schema = getSchema();
        ScaleEvaluate.$().update(schema);
    }

    public static void closeQuietly() {
        ScaleEvaluate.$().closeQuietly();
    }

    public Object getObject(Object[] params) {
        try {
            ResultSetHandler hdl = DescnDataHelper.hdlMap.get(type);
            if (hdl == null) {
                throw new RuntimeException("不存在此类型的hdl对象");
            }
            Object[] newParams = reBuildParams(params);
            String sql = getSelSql();
            Object o = ScaleEvaluate.$().query(sql, newParams, hdl);
            if (logger.isDebugEnabled()) {
                logger.debug(o);
            }
            return o;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return StringUtils.EMPTY;
        }
    }

    @SuppressWarnings("unchecked")
    public Map getObject(Map params) {
        try {
            String sql = buildSelectSQL(params);
            ;
            Object o = ScaleEvaluate.$().query(sql, new MapHandler());
            if (logger.isDebugEnabled()) {
                logger.debug(o);
            }
            return (Map) o;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Collections.emptyMap();
        }
    }

    public void insertData(Object[] params) throws Exception {
        String ins = getInsSql();
        ScaleEvaluate.$().update(ins, params);
    }

    static public Object executeQuery(String sql, ResultSetHandler rsh) {
        try {
            Object o = ScaleEvaluate.$().query(sql, rsh);
            if (logger.isDebugEnabled()) {
                logger.debug(sql);
                logger.debug(o);
            }
            return o;

        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    static public int executeUpdate(String sql) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }
            return ScaleEvaluate.$().update(sql);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    public void testSql() {
        try {
            ScaleEvaluate.$().query(String.format("select * from DATA28"), new ResultSetHandler() {
                public Object handle(ResultSet rs) throws SQLException {
                    @SuppressWarnings("unused")
                    ResultSetMetaData meta = rs.getMetaData();

                    while (rs.next()) {
                        // System.out.print(rs.getString(1));
                        // System.out.print("-");
                        // System.out.print(rs.getString(2));
                        // System.out.print("-");
                        // System.out.print(rs.getString(3));
                        // System.out.print("-");
                        // System.out.print(rs.getString(4));
                        // System.out.print("-");
                        // System.out.println(rs.getString(5));
                        // System.out.println(UtilCollection.toIntRangeList(rs.getString(4),
                        // ExamConsts.RANGE_SPARATOR, '-'));
                    }
                    return null;
                }

            });
            ScaleEvaluate.$().closeQuietly();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object[] reBuildParams(Object[] params) {
        Validate.notNull(params, "params参数不能为空");
        if (logger.isDebugEnabled()) {
            logger.debug("params=" + ArrayUtils.toString(params));
        }
        Object newParams[] = null;
        if (DescnDataHelper.RANG_TO.equalsIgnoreCase(type)) {
            newParams = rebuildRangTo(params);
        } else if (DescnDataHelper.NORM_SGL.equalsIgnoreCase(type)) {
            newParams = rebuildNormSgl(params);
        } else if (DescnDataHelper.INTER_VAL.equalsIgnoreCase(type)) {
            newParams = rebuildInterVal(params);
        } else {
            newParams = params;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("params=" + ArrayUtils.toString(newParams));
        }
        return newParams;
    }

    private Object[] rebuildInterVal(Object[] params) {
        Object[] newParams;
        if (params.length > 2) {
            throw new IllegalArgumentException("params参数的个数太多");
        }
        if (params.length == 1) {
            newParams = new Object[2];
            newParams[0] = params[0];
            newParams[1] = params[0];
        } else {
            newParams = params;
        }
        return newParams;
    }

    private Object[] rebuildNormSgl(Object[] params) {
        Object[] newParams;
        if (params.length != 1) {
            throw new IllegalArgumentException("params参数的个数应该是1");
        }
        newParams = params;
        return newParams;
    }

    private Object[] rebuildRangTo(Object[] params) {
        Object[] newParams;
        if (params.length < 2 || params.length > 3) {
            throw new IllegalArgumentException("params参数的个数不对");
        }
        if (params.length == 2) {
            newParams = new Object[3];
            newParams[0] = params[0];
            newParams[1] = params[1];
            newParams[2] = params[1];
        } else {
            newParams = params;
        }
        return newParams;
    }

    private String getSelSql() {
        DbSql sql = getSQL();
        Object objs[] = null;
        if (StringUtils.isNotBlank(this.fields)) {
            objs = new Object[2];
            objs[0] = this.fields;
            objs[1] = this.name;
        } else {
            objs = new Object[1];
            objs[0] = this.name;
        }
        String sel = sql.getSelSql(objs);
        if (logger.isDebugEnabled()) {
            logger.debug(sel);
        }
        return sel;
    }

    private String getSchema() {
        DbSql sql = getSQL();
        String schema = sql.getCreatSql(name);
        return schema;
    }

    private String getInsSql() {
        DbSql sql = getSQL();
        String ins = sql.getInsSql(name);
        return ins;
    }

    private DbSql getSQL() {
        DbSql sql = DescnDataHelper.sqlMap.get(type);
        if (sql == null) {
            throw new RuntimeException("不存在此类型的SQL对象");
        }
        return sql;
    }

    public String getDescn() {
        return StringUtils.trimToEmpty(descn);
    }

    public void setDescn(String description) {
        this.descn = description;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    @SuppressWarnings("unchecked")
    public String buildSelectSQL(Map params) {
        String wherestr = buildWhere(params);
        String result = SimpleSQL.selectSql(getName(), buildFldStr(), wherestr);
        if (logger.isDebugEnabled()) {
            logger.debug(result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private String buildWhere(Map params) {
        String wherestr = UtilCollection.substitutStr(where, params);
        return wherestr;
    }

    private String buildFldStr() {
        StrBuilder sb = new StrBuilder(64);
        StrBuilder sbChar = new StrBuilder(8);
        StrBuilder sbNumb = new StrBuilder(4);
        StrBuilder sbnext = new StrBuilder(4);
        // System.out.println(fields.length());
        for (int index = 0; index < fields.length(); ++index) {
            char ch = fields.charAt(index);
            if (Character.isWhitespace(ch))
                continue;
            if (ch == UtilCollection.COMMA) {
                sb.append(sbChar).append(sbNumb).append(UtilCollection.COMMA);
                sbChar.clear();
                sbNumb.clear();
                continue;
            }
            if (Character.isLetter(ch)) {
                sbChar.append(ch);
            } else if (Character.isDigit(ch)) {
                sbNumb.append(ch);
            } else if (ch == '-') {
                sbnext.clear();
                int nextpos = index + 1;
                for (; nextpos < fields.length(); ++nextpos) {
                    char c = fields.charAt(nextpos);
                    if (c == UtilCollection.COMMA) {
                        break;
                    }
                    sbnext.append(c);
                }
                index = nextpos;
                int left = NumberUtils.toInt(sbNumb.toString());
                int right = NumberUtils.toInt(sbnext.toString());
                for (int i = left; i <= right; ++i) {
                    sb.append(sbChar).append(i);
                    if (i != right) {
                        sb.append(UtilCollection.COMMA);
                    }
                }
                if (nextpos != fields.length()) {
                    sb.append(UtilCollection.COMMA);
                }
                sbChar.clear();
                sbNumb.clear();
            }
        }
        if (!sbChar.isEmpty())
            sb.append(sbChar).append(sbNumb);

        return sb.toString();
    }

}
