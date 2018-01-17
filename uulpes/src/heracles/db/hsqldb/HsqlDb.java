package heracles.db.hsqldb;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HsqlDb {
    private Connection conn;
    private String rootPath;
    private String dbname;
    private QueryRunner run = new QueryRunner();
    private final Log logger = LogFactory.getLog(getClass());

    public HsqlDb(String rootPath, String dbname) throws Exception {
        this.dbname = dbname;
        this.rootPath = rootPath;
        File f = new File(rootPath);
        if (!f.exists()) {
            f.mkdirs();
            logger.info("create the dir:" + rootPath + ", processing...");
        }
        newConnect();
    }

    public void close() throws SQLException {
        HsqlDbUtils.shutdownAndCloseConnection(conn);
    }

    public void closeQuietly() {
        try {
            close();
        } catch (SQLException e) {
        }
    }

    public void shutdownAndClose() throws SQLException {
        HsqlDbUtils.shutdownAndCloseConnection(conn);
    }

    public void shutdownAndCompact() throws SQLException {
        HsqlDbUtils.shutdownCompactAndCloseConnection(conn);
    }

    public void shutdownAndScript() throws SQLException {
        HsqlDbUtils.shutdownScriptAndCloseConnection(conn);
    }

    public void refresh(ShutDownType downType) throws Exception {
        HsqlDbUtils.shutdownAndCloseConnection(conn, downType);
        newConnect();
    }

    private void newConnect() throws Exception {
        conn = new HsqlConnectProvider().getFileConnection(rootPath, dbname);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(autoCommit);
        }
    }

    public int update(String sql, Object[] params) throws Exception {
        Connection conn = getConnection();
        return run.update(conn, sql, params);
    }

    public int update(String sql) throws Exception {
        Connection conn = getConnection();
        return run.update(conn, sql);
    }

    public ArrayList<?> query(String sql, Object[] params) throws Exception {
        Connection conn = getConnection();
        return (ArrayList<?>) run.query(conn, sql, params, new ArrayListHandler());
    }

    public ArrayList<?> query(String sql) throws Exception {
        Connection conn = getConnection();
        return (ArrayList<?>) run.query(conn, sql, new ArrayListHandler());
    }

    public Object query(String sql, Object[] params, ResultSetHandler rsh) throws Exception {
        Connection conn = getConnection();
        return run.query(conn, sql, params, rsh);
    }

    public Object query(String sql, Object param, ResultSetHandler rsh) throws Exception {
        Connection conn = getConnection();
        return run.query(conn, sql, param, rsh);
    }

    public Object query(String sql, ResultSetHandler rsh) throws Exception {
        Connection conn = getConnection();
        return run.query(conn, sql, rsh);
    }

    public Connection getConnection() throws Exception {
        return this.conn;
    }
}
