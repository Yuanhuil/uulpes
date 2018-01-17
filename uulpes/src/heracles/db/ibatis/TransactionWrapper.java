package heracles.db.ibatis;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ibatis.sqlmap.client.event.RowHandler;

import heracles.util.WRLockService;

public class TransactionWrapper extends WRLockService {
    private boolean on = true;
    private boolean isStart = false;

    public TransactionWrapper() {
    }

    public boolean isOn() {
        return on;
    }

    public void on() {
        this.on = true;
    }

    public void off() {
        this.on = false;
    }

    public DataSource getDataSource() {
        return DefaultSqlMapClient.getInstance().getDataSource();
    }

    public Connection getConnection() throws PersistableException {
        return DefaultSqlMapClient.getInstance().getCurrentConnection();
    }

    public void start() throws PersistableException {
        if (!isStart && on) {
            off();
            isStart = true;
            this.writeLock();
            DefaultSqlMapClient.getInstance().startTransaction();
        }
    }

    public void commit() throws PersistableException {
        if (isStart && !on) {
            DefaultSqlMapClient.getInstance().commitTransaction();
        }
    }

    public void end() {
        if (isStart && !on) {
            DefaultSqlMapClient.getInstance().endTransaction();
            isStart = false;
            this.writeUnLock();
            on();
        }
    }

    public void startBatch() throws PersistableException {
        if (isStart && !on)
            DefaultSqlMapClient.getInstance().startBatch();
    }

    public int executeBatch() throws PersistableException {
        if (isStart && !on)
            return DefaultSqlMapClient.getInstance().executeBatch();
        else
            return -1;
    }

    public <T> List<T> queryForList(String id) throws PersistableException {
        try {
            start_();
            return DefaultSqlMapClient.getInstance().queryForList(id);
        } finally {
            end_();
        }
    }

    private void commit_() throws PersistableException {
        if (on) {
            DefaultSqlMapClient.getInstance().commitTransaction();
        }
    }

    private void end_() {
        if (on) {
            DefaultSqlMapClient.getInstance().endTransaction();
        }
    }

    private void start_() throws PersistableException {
        if (on) {
            DefaultSqlMapClient.getInstance().startTransaction();
        }
    }

    public <T> List<T> queryForList(String id, Object param) throws PersistableException {
        try {
            start_();
            return DefaultSqlMapClient.getInstance().queryForList(id, param);
        } finally {
            end_();
        }
    }

    public Object queryForObject(String id) throws PersistableException {
        try {
            start_();
            return DefaultSqlMapClient.getInstance().queryForObject(id);
        } finally {
            end_();
        }
    }

    public <T> T queryForObject(String id, Object param) throws PersistableException {
        try {
            start_();
            return DefaultSqlMapClient.getInstance().queryForObject(id, param);
        } finally {
            end_();
        }
    }

    @SuppressWarnings("unchecked")
    public Map queryForMap(String id, Object parameterObject, String keyProp) throws PersistableException {
        try {
            start_();
            return DefaultSqlMapClient.getInstance().queryForMap(id, parameterObject, keyProp);
        } finally {
            end_();
        }
    }

    @SuppressWarnings("unchecked")
    public Map queryForMap(String id, Object parameterObject, String keyProp, String valueProp)
            throws PersistableException {
        try {
            start_();
            return DefaultSqlMapClient.getInstance().queryForMap(id, parameterObject, keyProp, valueProp);
        } finally {
            end_();
        }
    }

    public void queryWithRowHandler(String id, Object parameterObject, RowHandler rowHandler)
            throws PersistableException {
        try {
            start_();
            DefaultSqlMapClient.getInstance().queryWithRowHandler(id, parameterObject, rowHandler);
        } finally {
            end_();
        }
    }

    public int update(String id) throws PersistableException {
        try {
            start_();
            int effect = DefaultSqlMapClient.getInstance().update(id);
            commit_();
            return effect;
        } finally {
            end_();
        }
    }

    public int update(String id, Object param) throws PersistableException {
        try {
            start_();
            int effect = DefaultSqlMapClient.getInstance().update(id, param);
            commit_();
            return effect;
        } finally {
            end_();
        }
    }

    public void insert(String id) throws PersistableException {
        try {
            start_();
            DefaultSqlMapClient.getInstance().insert(id);
            commit_();
        } finally {
            end_();
        }
    }

    public void insert(String id, Object obj) throws PersistableException {
        try {
            start_();
            DefaultSqlMapClient.getInstance().insert(id, obj);
            commit_();
        } finally {
            end_();
        }
    }

    public int delete(String id) throws PersistableException {
        try {
            start_();
            int effect = DefaultSqlMapClient.getInstance().delete(id);
            commit_();
            return effect;
        } finally {
            end_();
        }
    }

    public int delete(String id, Object obj) throws PersistableException {
        try {
            start_();
            int effect = DefaultSqlMapClient.getInstance().delete(id, obj);
            commit_();
            return effect;
        } finally {
            end_();
        }
    }

}