package heracles.db.ibatis;

import java.sql.SQLException;

import org.apache.commons.dbutils.ResultSetHandler;

public class DbQuery extends DbOperater {
    public Object query(String sql, Object param, ResultSetHandler rsh) throws SQLException {
        return runner.query(sql, param, rsh);
    }

    public Object query(String sql, Object[] params, ResultSetHandler rsh) throws SQLException {
        return runner.query(sql, params, rsh);
    }

    public Object query(String sql, ResultSetHandler rsh) throws SQLException {
        return runner.query(sql, rsh);
    }
}
