package edutec.group.data;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.IntRange;

import edutec.group.domain.DimDescription;
import edutec.group.domain.DimScoreGrade;
import edutec.group.domain.DimWarning;
import edutec.group.domain.DimWarningAndScoreGrade;
import edutec.group.domain.PropNorm;
import heracles.db.ibatis.DefaultSqlMapClient;

/**
 * 获得t05值，f05值
 * 
 * @author 王文
 */
@SuppressWarnings("unchecked")
public class DataQuery {
    public static Map getZPVal() {
        return DefaultSqlMapClient.getInstance().queryForMap("selectZPVal", null, "z", "p");
    }

    public static double getT05Val(int df) {
        if (df > 40 && df < 100) {
            df += (10 - df % 10);
        } else if (df > 100 && df < 200) {
            df = 200;
        } else if (df > 200 && df < 500) {
            df = 500;
        } else if (df > 500 && df < 1000) {
            df = 1000;
        } else if (df > 1000) {
            df = 1001;
        }
        Object o = DefaultSqlMapClient.getInstance().queryForObject("selectTval", df);
        return Double.valueOf(o.toString());
    }

    private static int getUpper(int df, IntRange[] rangeList) {
        for (int i = 0; i < rangeList.length; ++i) {
            IntRange range = rangeList[i];
            if (df < range.getMaximumInteger() && df > range.getMinimumInteger()) {
                return range.getMaximumInteger();
            }
        }
        return -1;

    }

    public static double getF05Val(int dfx, int dfe) {
        final IntRange[] dfxRangeList = { new IntRange(50, 60), new IntRange(60, 80), new IntRange(80, 100),
                new IntRange(100, 200), new IntRange(200, 500) };

        final IntRange[] dfeRangeList = { new IntRange(50, 60), new IntRange(60, 80), new IntRange(80, 100),
                new IntRange(100, 125), new IntRange(125, 150), new IntRange(150, 200), new IntRange(200, 300),
                new IntRange(300, 500), new IntRange(500, 1000) };

        int v = -1;
        /* 调整dfx的值 */
        if (dfx > 10 && dfx < 30 && dfx % 2 != 0) {
            dfx += 1;
        } else if (dfx > 30 && dfx < 50 && dfx % 5 != 0) {
            dfx += (5 - dfx % 5);
        } else if ((v = getUpper(dfx, dfxRangeList)) != -1) {
            dfx = v;
        } else if (dfx > 500) {
            dfx = 501;
        }

        /* 调整dfe的值 */
        if (dfe >= 30 && dfe < 50 && dfe % 2 != 0) {
            dfe += (2 - dfx % 2);
        } else if ((v = getUpper(dfe, dfeRangeList)) != -1) {
            dfe = v;
        } else if (dfe > 1000) {
            dfe = 1001;
        }
        String f = "n" + dfx;
        Object o = DefaultSqlMapClient.getInstance().queryForObject("selectFval005", dfe);
        HashMap map = (HashMap) o;
        if (map == null) {
            return Double.NaN;
        } else {
            o = map.get(f);
            return Double.valueOf(o.toString());
        }
    }

    public static Map getClassname() throws SQLException {
        return DefaultSqlMapClient.getInstance().queryForMap("selectClassname", null, "classId", "className");
    }

    public static String getClassname(String classId) throws SQLException {
        return DefaultSqlMapClient.getInstance().queryForObject("selectClassnameById", classId);
    }
    // public static PropNorm getNorm(Map param) {
    // Integer grade = MapUtils.getInteger(param,
    // Constants.GRADE_ORDER_ID_PROP);
    // if(grade>12){
    // grade -= 3;
    // param.put(Constants.GRADE_ORDER_ID_PROP, grade);
    // }
    // return DefaultSqlMapClient.getInstance().queryForObject("getNorm",
    // param);
    // }

    public static PropNorm getNorm(Map param) {

        return DefaultSqlMapClient.getInstance().queryForObject("getNorm", param);
    }

    // 获取预警级别
    public static DimWarning getWarning(Map param) throws SQLException {

        return DefaultSqlMapClient.getInstance().queryForObject("getWarning", param);
    }

    public static DimWarningAndScoreGrade getWarningAndScoreGrade(Map param) throws SQLException {

        return DefaultSqlMapClient.getInstance().queryForObject("getWarningAndScoreGrade", param);
    }

    public static DimScoreGrade getScoreGrade(Map param) throws SQLException {

        return DefaultSqlMapClient.getInstance().queryForObject("getScoreGrade", param);
    }

    public static DimDescription getDescription(Map param) {
        return DefaultSqlMapClient.getInstance().queryForObject("getDimDescription", param);
    }
}
