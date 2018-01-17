package heracles.db.sql;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.text.StrBuilder;

import heracles.util.UtilCollection;

public class SimpleSQL {
    private static final String SET = " SET ";
    private static final String UPDATE = "UPDATE ";
    private static final String WHERE = " WHERE ";
    private static final String FROM = " FROM ";
    private static final String SELECT = "SELECT ";

    public static String selectSql(String tablename, String filedstr) {
        StrBuilder sb = new StrBuilder(tablename.length() + filedstr.length() + 16);
        sb.append(SELECT);
        sb.append(filedstr);
        sb.append(FROM).append(tablename);
        return sb.toString();
    }

    public static String selectSql(String tablename, String filedstr, String where) {
        StrBuilder sb = new StrBuilder(selectSql(tablename, filedstr));
        sb.append(WHERE).append(where);
        return sb.toString();
    }

    public static String selectSql(String tablename, List<String> listfiled) {
        return selectSql(tablename, UtilCollection.toString(listfiled));
    }

    public static String selectSql(String tablename, String[] fldarray) {
        List<String> listfiled = Arrays.asList(fldarray);
        return selectSql(tablename, listfiled);
    }

    public static String selectSql(String tablename, List<String> listfiled, String whereClause) {
        StrBuilder sb = new StrBuilder(selectSql(tablename, listfiled));
        sb.append(WHERE).append(whereClause);
        return sb.toString();
    }

    public static String selectSql(String tablename, String[] fldarray, String whereClause) {
        return selectSql(tablename, Arrays.asList(fldarray), whereClause);
    }

    public static String updateSql(String tablename, Map<Object, Object> map, String whereClause) {
        StrBuilder sb = new StrBuilder(updateSql(tablename, map));
        sb.append(WHERE).append(whereClause);
        return sb.toString();
    }

    public static String updateSql(String tablename, Map<Object, Object> map) {
        StrBuilder sb = new StrBuilder(128);
        sb.append(UPDATE).append(tablename).append(SET);
        for (Map.Entry<Object, Object> ent : map.entrySet()) {
            sb.append(ent.getKey()).append(UtilCollection.EQ).append(format(ent.getValue()));
            sb.append(",");
        }
        sb.setCharAt(sb.length() - 1, ' ');
        return sb.toString();
    }

    /**
     * 根据map生成sql，其中map中的whereKey健为条件字段
     * 
     * @param tablename
     *            表名
     * @param map
     *            字段数据
     * @param whereKey
     *            map中的条件关键字
     * @return
     */
    public static String updateSqlWhere(String tablename, Map<Object, Object> map, Object whereKey) {
        return updateSqlWhere(tablename, map, new Object[] { whereKey });
    }

    /**
     * 根据map生成sql，其中map中的whereKeys健为条件字段
     * 
     * @param tablename
     *            表名
     * @param map
     *            字段数据
     * @param whereKeys
     *            map中的条件关键字
     * @return
     */
    public static String updateSqlWhere(String tablename, Map<Object, Object> map, Object[] whereKeys) {
        StrBuilder clause = new StrBuilder(64);
        StrBuilder sbwhere = new StrBuilder();
        sbwhere.append(WHERE);
        Map<Object, Object> filedVals = new HashMap<Object, Object>(map);
        for (int i = 0; i < whereKeys.length; ++i) {
            sbwhere.append(whereKeys[i]).append(UtilCollection.EQ).append(format(filedVals.get(whereKeys[i])));
            sbwhere.append(UtilCollection.COMMA);
            if (filedVals.remove(whereKeys[i]) == null) {
                throw new IllegalArgumentException("参数map中没有whereKeys所指定的key");
            }
        }
        sbwhere.deleteCharAt(sbwhere.length() - 1);
        clause.append(UPDATE).append(tablename).append(SET);
        for (Map.Entry<Object, Object> ent : filedVals.entrySet()) {
            clause.append(ent.getKey()).append(UtilCollection.EQ).append(format(ent.getValue()));
            clause.append(UtilCollection.COMMA);
        }
        clause.setCharAt(sbwhere.length() - 1, ' ');
        clause.append(sbwhere.toString());
        return clause.toString();
    }

    public static String updatePrepSql(String tablename, String[] filedarray) {
        StrBuilder sb = new StrBuilder(128);
        sb.append(UPDATE).append(tablename).append(SET);
        for (int i = 0; i < filedarray.length; ++i) {
            sb.append(filedarray[i]).append(UtilCollection.EQ).append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String updatePrepSql(String tablename, String[] filedarray, String wherearray[]) {
        StrBuilder sb = new StrBuilder(updatePrepSql(tablename, filedarray));
        sb.append(WHERE);
        for (int i = 0; i < wherearray.length; ++i) {
            sb.append(wherearray[i]).append(UtilCollection.EQ).append("? and");
        }
        sb.deleteCharAt(sb.length() - " and".length());
        return sb.toString();
    }

    public static String deleteSql(String tablename) {
        StrBuilder sb = new StrBuilder(64);
        sb.append("delete from ").append(tablename);
        return sb.toString();
    }

    public static String deletePrepSql(String tablename, String flds[]) {
        StrBuilder sb = new StrBuilder(64);
        sb.append("delete from ").append(tablename).append(WHERE);
        for (int i = 0; i < flds.length; ++i) {
            sb.append(flds[i]).append(UtilCollection.EQ).append("? and");
        }
        sb.setLength(sb.length() - "and".length());
        return sb.toString();
    }

    public static String truncate(String tablename) {
        StrBuilder sb = new StrBuilder(32);
        sb.append("truncate").append(" ").append(tablename);
        return sb.toString();
    }

    public static String format(Object o) {
        if (o == null) {
            return "NULL";
        }
        if (o instanceof String) {
            return formatstr(o.toString());
        }
        if (o instanceof Timestamp) {
            Timestamp tsm = (Timestamp) o;
            return tsm.getTime() + "";
        }
        if (o instanceof Date) {
            Date date = (Date) o;
            return date.getTime() + "";
        }
        return o.toString();

    }

    public static String formatstr(String s) {
        StrBuilder sb = new StrBuilder(s.length() + 2);
        sb.append("'");
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\'') {
                sb.append("\'");
            } else {
                sb.append(s.charAt(i));
            }
        }
        sb.append("'");
        return sb.toString();
    }
}
