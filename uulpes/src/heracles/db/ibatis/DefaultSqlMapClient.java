/**
 * 
 */
package heracles.db.ibatis;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Administrator
 */
@SuppressWarnings("unchecked")
public class DefaultSqlMapClient {

    private final static Log logger = LogFactory.getLog(DefaultSqlMapClient.class);

    private static final String DATABASE_DRIVER = "database.driver";
    private static final String DATABASE_URL = "database.url";
    private static final String DATABASE_USENAME = "database.usename";
    private static final String DATABASE_PASSWORD = "database.password";
    private static final String RES_MAP_PATH = "resource.map.path";
    private static final String RES_MAP_XML = "resource.map.xml";

    private SqlMapClient sqlMapper = null;
    private Configuration configuration;

    static private DefaultSqlMapClient instance = new DefaultSqlMapClient();
    static private boolean alreadyInit = false;

    private DefaultSqlMapClient() {

    }

    static public DefaultSqlMapClient getInstance() {

        return instance;
    }

    public SqlMapClient getSqlMapClient() {
        return sqlMapper;
    }

    public void startBatch() throws PersistableException {
        try {
            sqlMapper.startBatch();
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public int executeBatch() throws PersistableException {
        try {
            return sqlMapper.executeBatch();
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public DataSource getDataSource() {
        return sqlMapper.getDataSource();
    }

    public SqlMapSession openSession() {
        return sqlMapper.openSession();
    }

    public void startTransaction() throws PersistableException {
        try {
            sqlMapper.startTransaction();
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public void endTransaction() {
        try {
            sqlMapper.endTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getCurrentConnection() throws PersistableException {
        try {
            return sqlMapper.getCurrentConnection();
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public void commitTransaction() throws PersistableException {
        try {
            sqlMapper.commitTransaction();
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public <T> List<T> queryForList(String id) throws PersistableException {
        List<T> list = null;
        try {
            list = sqlMapper.queryForList(id);
            return list;
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public <T> List<T> queryForList(String id, Object param) throws PersistableException {
        List<T> list = null;
        try {
            list = (List<T>) sqlMapper.queryForList(id, param);
            if (list == null) {
                return Collections.emptyList();
            }
            return list;
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public <T> List<T> queryForList(String id, Object param, int skip, int max) throws PersistableException {
        List<T> list = null;
        try {
            list = (List<T>) sqlMapper.queryForList(id, param, skip, max);
            return list;
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public Object queryForObject(String id) throws PersistableException {
        try {
            return sqlMapper.queryForObject(id);

        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public Object queryForObject(String id, Object param, Object resultObject) throws PersistableException {
        try {
            return sqlMapper.queryForObject(id, param, resultObject);

        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public <T> T queryForObject(String id, Object param) throws PersistableException {
        try {
            return (T) sqlMapper.queryForObject(id, param);
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public Map<?, ?> queryForMap(String id, Object parameterObject, String keyProp) throws PersistableException {
        try {
            return sqlMapper.queryForMap(id, parameterObject, keyProp);

        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public Map<?, ?> queryForMap(String id, Object parameterObject, String keyProp, String valueProp)
            throws PersistableException {
        try {
            return sqlMapper.queryForMap(id, parameterObject, keyProp, valueProp);

        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public int update(String id) throws PersistableException {
        try {
            return sqlMapper.update(id);
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public int update(String id, Object param) throws PersistableException {
        try {
            return sqlMapper.update(id, param);
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public void insert(String id) throws PersistableException {
        try {
            sqlMapper.insert(id);
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public void insert(String id, Object obj) throws PersistableException {
        try {
            sqlMapper.insert(id, obj);
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public int delete(String id) throws PersistableException {
        try {
            return sqlMapper.delete(id);
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public int delete(String id, Object obj) throws PersistableException {
        try {
            return sqlMapper.delete(id, obj);
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public void queryWithRowHandler(String id, Object parameterObject, RowHandler rowHandler)
            throws PersistableException {
        try {
            sqlMapper.queryWithRowHandler(id, parameterObject, rowHandler);
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    public void queryWithRowHandler(String id, RowHandler rowHandler) throws PersistableException {
        try {
            sqlMapper.queryWithRowHandler(id, rowHandler);
        } catch (SQLException e) {
            throw new PersistableException(e.getMessage());
        }
    }

    private Properties buildProps() {
        if (configuration == null) {
            return null;
        }
        Properties props = new Properties();
        props.setProperty(DATABASE_DRIVER, configuration.getString(DATABASE_DRIVER));
        props.setProperty(DATABASE_URL, configuration.getString(DATABASE_URL));
        props.setProperty(DATABASE_USENAME, configuration.getString(DATABASE_USENAME));
        props.setProperty(DATABASE_PASSWORD, configuration.getString(DATABASE_PASSWORD));
        props.setProperty(RES_MAP_PATH, configuration.getString(RES_MAP_PATH));

        return props;
    }

    private void buildSqlMapClient() {
        if (alreadyInit) {
            return;
        }
        synchronized (instance) {
            if (alreadyInit) {
                return;
            }
            Reader reader = null;
            try {
                Properties props = buildProps();
                reader = Resources.getResourceAsReader(configuration.getString(RES_MAP_XML));
                if (props != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(props.get(DATABASE_DRIVER));
                        logger.debug(props.get(DATABASE_URL));
                    }
                    sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader, props);
                } else {
                    sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader);
                }
                logger.info("create SqlMapClient ok!");
                alreadyInit = true;
            } catch (IOException e) {
                logger.error("read SqlMapClient config", e);
                throw new RuntimeException("Something bad happened while building the SqlMapClient instance." + e);
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        buildSqlMapClient();
    }

}
