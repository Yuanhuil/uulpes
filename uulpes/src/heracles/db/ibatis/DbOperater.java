package heracles.db.ibatis;

import org.apache.commons.dbutils.QueryRunner;

import com.ibatis.sqlmap.client.SqlMapClient;

public class DbOperater {
    protected DefaultSqlMapClient sqlClient = DefaultSqlMapClient.getInstance();
    protected QueryRunner runner = new QueryRunner(sqlClient.getDataSource());

    public QueryRunner getRunner() {
        return runner;
    }

    public DefaultSqlMapClient getSqlClient() {
        return sqlClient;
    }

    public QueryRunner newQueryRunner() {
        return new QueryRunner();
    }

    public SqlMapClient getSqlMapClient() {
        return sqlClient.getSqlMapClient();
    }

}
