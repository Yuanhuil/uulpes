package edutec.scale.descriptor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

public class DescnDataHelper {
    static public final String SCORE = "score"; // 数据在xml中配置
    static public final String RANG_TO = "rangeTo";
    static public final String NORM_SGL = "normSgl";
    static public final String NORM_SGLW = "normSglw";
    static public final String NORM_STD = "normStd";

    static public final String CRITICAL = "criticalVal";
    static public final String INTER_VAL = "intervalVal";

    public final static String LOW_NORM = "显著低于常模";
    public final static String UPPER_NORM = "显著高于常模";
    public final static String NEAR_NORM = "与常模没有差异";

    static private final String CTE_RANG_TO = "CREATE TABLE %s (w varchar(10) NOT NULL , lo INTEGER, hi INTEGER, descn varchar(255) NOT NULL)";
    static private final String INS_RANG_TO = "INSERT INTO %s (w,lo,hi,descn) VALUES (?,?,?,?)";
    static private final String SEL_RANG_TO = "SELECT descn FROM %s WHERE w=? AND (lo<=? AND hi>=?)";

    static private final String CTE_NORM_SGL = "CREATE TABLE %s (f INT NOT NULL, m DOUBLE NOT NULL, md DOUBLE NOT NULL, sd DOUBLE NOT NULL)";
    static private final String INS_NORM_SGL = "INSERT INTO  %s (f,m,md,sd) VALUES (?,?,?,?)";
    static private final String SEL_NORM_SGL = "SELECT %s FROM %s WHERE f=?";

    static private final String CTE_NORM_SGLW = "CREATE TABLE %s (f INT NOT NULL, w varchar(10) NOT NULL, m DOUBLE NOT NULL, md DOUBLE NOT NULL, sd DOUBLE NOT NULL)";
    static private final String INS_NORM_SGLW = "INSERT INTO  %s (f,w,m,md,sd) VALUES (?,?,?,?,?)";
    static private final String SEL_NORM_SGLW = "SELECT %s FROM %s WHERE f=? AND w=?";

    static private final String CTE_CRIT_SGL = "CREATE TABLE %s (v INT NOT NULL, descn varchar(255) NOT NULL)";
    static private final String INS_CRIT_SGL = "INSERT INTO  %s (v,descn) VALUES (?,?)";
    static private final String SEL_CRIT_SGL = "SELECT descn FROM %s WHERE v=?";

    static private final String CTE_INTER_VAL = "CREATE TABLE %s (lo INTEGER, hi INTEGER, descn varchar(255) NOT NULL)";
    static private final String INS_INTER_VAL = "INSERT INTO %s (lo,hi,descn) VALUES (?,?,?)";
    static private final String SEL_INTER_VAL = "SELECT descn FROM %s WHERE lo<=? AND hi>=?";

    static final Map<String, DbSql> sqlMap = new HashMap<String, DbSql>();
    static final Map<String, ResultSetHandler> hdlMap = new HashMap<String, ResultSetHandler>();

    static {
        sqlMap.put(RANG_TO.toLowerCase(), new DbSql(CTE_RANG_TO, INS_RANG_TO, SEL_RANG_TO));
        sqlMap.put(NORM_SGL.toLowerCase(), new DbSql(CTE_NORM_SGL, INS_NORM_SGL, SEL_NORM_SGL));
        sqlMap.put(NORM_SGLW.toLowerCase(), new DbSql(CTE_NORM_SGLW, INS_NORM_SGLW, SEL_NORM_SGLW));
        sqlMap.put(CRITICAL.toLowerCase(), new DbSql(CTE_CRIT_SGL, INS_CRIT_SGL, SEL_CRIT_SGL));
        sqlMap.put(INTER_VAL.toLowerCase(), new DbSql(CTE_INTER_VAL, INS_INTER_VAL, SEL_INTER_VAL));

        hdlMap.put(RANG_TO.toLowerCase(), new ScalarHandler());
        hdlMap.put(CRITICAL.toLowerCase(), new ScalarHandler());
        hdlMap.put(NORM_SGL.toLowerCase(), new NormHadler());
        hdlMap.put(NORM_SGLW.toLowerCase(), new NormHadler());
        hdlMap.put(INTER_VAL.toLowerCase(), new ScalarHandler());
    }

    static class NormHadler implements ResultSetHandler {
        public Object handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                if (cols == 2) {
                    final double min = rs.getDouble(1);
                    final double max = rs.getDouble(2);
                    if (min == 0.0 && max == 0.0) {
                        return null;
                    }
                    DoubleRange dr = new DoubleRange(min, max);
                    return dr;
                } else if (cols == 1) {
                    return Double.valueOf(rs.getDouble(1));
                }
            }
            return null;
        }
    }

    static class DoubleRangeHadler implements ResultSetHandler {
        public Object handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                final double min = rs.getDouble(1);
                final double max = rs.getDouble(2);
                if (min == 0.0 && max == 0.0) {
                    return null;
                }
                DoubleRange dr = new DoubleRange(min, max);
                return dr;
            }
            return null;
        }
    }

    static class IntRangeHadler implements ResultSetHandler {
        public Object handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                final int min = rs.getInt(1);
                final int max = rs.getInt(2);
                if (min == 0 && max == 0) {
                    return null;
                }
                IntRange dr = new IntRange(min, max);
                return dr;
            }
            return null;
        }
    }

    static class DbSql {
        private final String creatSql;
        private final String insSql;
        private final String selSql;

        public DbSql(final String creatSql, final String insSql, final String selSql) {
            this.creatSql = creatSql;
            this.insSql = insSql;
            this.selSql = selSql;
        }

        public String getCreatSql(String name) {
            return String.format(creatSql, name);
        }

        public String getInsSql(String name) {
            return String.format(insSql, name);
        }

        public String getSelSql(Object[] name) {
            return String.format(selSql, name);
        }
    }

}
