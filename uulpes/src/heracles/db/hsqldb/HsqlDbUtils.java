package heracles.db.hsqldb;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

public class HsqlDbUtils {
    public static QueryRunner STATIC_QUERY_RUN = new QueryRunner();

    static public void shutdownAndCloseConnection(Connection conn, ShutDownType shutDownType) throws SQLException {
        if (shutDownType == ShutDownType.SHUTDOWN_COMPACT) {
            shutdownCompactAndCloseConnection(conn);
        } else if (shutDownType == ShutDownType.SHUTDOWN_SCRIPT) {
            shutdownScriptAndCloseConnection(conn);
        } else {
            shutdownAndCloseConnection(conn);
        }
    }

    static public void shutdownAndCloseConnection(Connection conn) throws SQLException {
        if (conn != null) {
            STATIC_QUERY_RUN.update(conn, "SHUTDOWN");
            conn.close();
        }
    }

    static public void shutdownScriptAndCloseConnection(Connection conn) throws SQLException {
        if (conn != null) {
            STATIC_QUERY_RUN.update(conn, "SHUTDOWN SCRIPT");
            conn.close();
        }
    }

    static public void shutdownCompactAndCloseConnection(Connection conn) throws SQLException {
        if (conn != null) {
            STATIC_QUERY_RUN.update(conn, "SHUTDOWN COMPACT");
            conn.close();
        }
    }

    static public void setAutoCommit(Connection conn, boolean autoCommit) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(autoCommit);
        }
    }

}
