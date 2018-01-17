/**
 * $RCSfile: SequenceGenerator.java,v $
 * $Revision: 1.1 $
 * $Date: 2008/11/03 03:08:46 $
 *
 *
 * This software is the proprietary information of CoolServlets, Inc.
 * Use is subject to license terms.
 */

package heracles.db.ibatis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import heracles.util.LogUtils;

@Component("SequenceGenerator")
public class SequenceGenerator {
    private static final Log logger = LogFactory.getLog(SequenceGenerator.class);

    private static final String LOAD_ID_BY_TYPE = "SELECT nextId FROM identifier WHERE type=?";
    private static final String LOAD_ID_BY_TBLNAME = "SELECT nextId FROM identifier WHERE tablename=?";
    private static final String UPDATE_ID_BY_TYPE = "UPDATE identifier SET nextId=? WHERE type=? AND nextId=?";
    private static final String UPDATE_ID_BY_TBLNAME = "UPDATE identifier SET nextId=? WHERE tablename=? AND nextId=?";

    private static final String LOAD_DATA = "SELECT type,tablename,nextId FROM identifier";

    static final Map<Integer, SequenceGenerator> typeMap = new HashMap<Integer, SequenceGenerator>();
    static final Map<String, SequenceGenerator> tblnameMap = new HashMap<String, SequenceGenerator>();
    @Autowired
    public SqlSessionFactoryBean sqlSessionFactory;
    @Autowired
    public DataSource dataSource;

    public SqlSession session = null;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SqlSession getSession() {
        return session;
    }

    public void setSession(SqlSession session) {
        this.session = session;
    }

    /**
     * The number of ID's to checkout at a time. 15 should provide a good
     * balance between speed and not wasing too many ID's on appserver restarts.
     * Feel free to change this number if you believe your Jive setup warrants
     * it.
     */
    private static final int INCREMENT = 15;

    // Statically startup a sequence manager for each of the five sequence
    // counters.
    /**
     * Returns the next ID of the specified type.
     * 
     * @param type
     *            the type of unique ID.
     * @return the next unique ID of the specified type.
     */
    public long nextID(int type) {
        try {
            setup();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return typeMap.get(type).nextUniqueID(true);
    }

    public long nextID(String tablename) {
        try {
            return tblnameMap.get(tablename).nextUniqueID(false);
        } catch (Exception e) {
            throw new RuntimeException("SequenceGenerator has not：" + tablename);
        }
    }

    private int type;
    private String tablename;
    private long currentID;
    private long maxID;

    /*
     * private DefaultSqlMapClient sqlMapClient;
     * 
     * 
     * public DefaultSqlMapClient getSqlMapClient() { return sqlMapClient; }
     * 
     * public void setSqlMapClient(DefaultSqlMapClient sqlMapClient) {
     * this.sqlMapClient = sqlMapClient; }
     */

    /**
     * Creates a new DbSequenceManager.
     */
    public SequenceGenerator() {
        tablename = null;
        currentID = 0l;
        maxID = 0l;
    }

    /**
     * Returns the next available unique ID. Essentially this provides for the
     * functionality of an auto-increment database field.
     */
    private synchronized long nextUniqueID(boolean isType) {
        if (!(currentID < maxID)) {
            // Get next block -- make 5 attempts at maximum.
            getNextBlock(5, isType);
        }
        long id = currentID;
        currentID++;
        return id;
    }

    /**
     * Performs a lookup to get the next availabe ID block. The algorithm is as
     * follows:
     * <ol>
     * <li>Select currentID from appropriate db row.
     * <li>Increment id returned from db.
     * <li>Update db row with new id where id=old_id.
     * <li>If update fails another process checked out the block first; go back
     * to step 1. Otherwise, done.
     * </ol>
     */
    private void getNextBlock(int count, boolean isType) {
        if (count == 0) {
            logger.error("Failed at last attempt to obtain an ID, aborting...");
            return;
        }
        boolean success = false;
        Connection con = null;
        try {
            con = session.getConnection();
            // Get the current ID from the database.
            String loadSql, updateSql;
            Object param;
            if (isType) {
                loadSql = LOAD_ID_BY_TYPE;
                updateSql = UPDATE_ID_BY_TYPE;
                param = new Long(this.type);
            } else {
                loadSql = LOAD_ID_BY_TBLNAME;
                updateSql = UPDATE_ID_BY_TBLNAME;
                param = this.tablename;
            }
            QueryRunner runner = new QueryRunner();
            currentID = (Long) runner.query(con, loadSql, param, new ResultSetHandler() {
                public Object handle(ResultSet rs) throws SQLException {
                    if (!rs.next()) {
                        String errMsg = "Loading the current ID failed. The SequenceId table may not be correctly populated.";
                        logger.error(errMsg);
                        throw new SQLException(errMsg);
                    }
                    long currentID = rs.getLong(1);
                    return new Long(currentID);
                }
            });

            // Increment the id to define our block.
            long newID = currentID + INCREMENT;
            // The WHERE clause includes the last value of the id. This ensures
            // that an update will occur only if nobody else has performed an
            // update first.
            int occurs = runner.update(con, updateSql, new Object[] { newID, param, currentID });
            session.commit();
            // Check to see if the row was affected. If not, some other process
            // already changed the original id that we read. Therefore, this
            // round failed and we'll have to try again.
            success = occurs == 1;
            if (success) {
                this.maxID = newID;
            }
        } catch (Exception sqle) {
            sqle.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
            // 不能关闭
            // session.close();
        }
        if (!success) {
            logger.error("WARNING: failed to obtain next ID block due to " + "thread contention. Trying again...");
            // Call this method again, but sleep briefly to try to avoid thread
            // contention.
            try {
                Thread.sleep(75);
            } catch (InterruptedException ie) {
            }
            getNextBlock(count - 1, isType);
        }
    }

    public void setup() throws Exception {
        try {
            session = sqlSessionFactory.getObject().openSession();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            QueryRunner runner = new QueryRunner(dataSource);
            runner.query(LOAD_DATA, new ResultSetHandler() {
                public Object handle(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                        SequenceGenerator generator = new SequenceGenerator();
                        generator.setDataSource(dataSource);
                        generator.setSession(session);
                        generator.type = rs.getInt(1);
                        generator.tablename = rs.getString(2);
                        typeMap.put(generator.type, generator);
                        tblnameMap.put(generator.tablename, generator);
                    }
                    return null;
                }
            });
            LogUtils.debug(logger, typeMap.keySet());
        } finally {
            // huangc 不能关闭
            // session.close();
        }
    }

    public void destory() {
        typeMap.clear();
        tblnameMap.clear();
    }
}