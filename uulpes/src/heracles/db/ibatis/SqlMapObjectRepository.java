package heracles.db.ibatis;

import java.text.MessageFormat;
import java.util.List;

import heracles.domain.model.Identifiable;
import heracles.domain.model.ObjectRepository;
import heracles.domain.model.RepositoryException;

public class SqlMapObjectRepository extends DbOperater implements ObjectRepository {
    static final String DELETE_ERROR = "在{0}中删除\"id={1}\"的记录时，出现异常";
    static final String INSERT_ERROR = "插入数据\"id={1}\"，出现异常";
    static final String LOAD_ERROR = "载入\"id={1}\"的记录时，出现异常";
    static final String UPDATE_ERROR = "更新\"id={1}\"的记录时，出现异常";
    static final String QUERY_ERROR = "查询表时，出现错误";

    private String deleteStmt;
    private String updateStmt;
    private String insertStmt;
    private String loadObjStmt;

    public String getDeleteStmt() {
        return deleteStmt;
    }

    public void setDeleteStmt(String deleteStmt) {
        this.deleteStmt = deleteStmt;
    }

    public String getUpdateStmt() {
        return updateStmt;
    }

    public void setUpdateStmt(String updateStmt) {
        this.updateStmt = updateStmt;
    }

    public String getInsertStmt() {
        return insertStmt;
    }

    public void setInsertStmt(String insertStmt) {
        this.insertStmt = insertStmt;
    }

    public String getLoadObjStmt() {
        return loadObjStmt;
    }

    public void setLoadObjStmt(String loadObjStmt) {
        this.loadObjStmt = loadObjStmt;
    }

    public int delete(long objectIdentifier) throws RepositoryException {
        try {
            return sqlClient.delete(deleteStmt, objectIdentifier);
        } catch (PersistableException e) {
            throw new RepositoryException(SqlMapObjectRepository.class,
                    MessageFormat.format("delete_error", "SqlMapObjectRepository", objectIdentifier), e);
        }
    }

    public void insert(Identifiable object) throws RepositoryException {
        try {
            sqlClient.insert(insertStmt, object);
        } catch (PersistableException e) {
            throw new RepositoryException(SqlMapObjectRepository.class,
                    MessageFormat.format("insert_error", object.getObjectIdentifier()), e);
        }
    }

    public Object load(long objectIdentifier) throws RepositoryException {
        try {
            return sqlClient.queryForObject(loadObjStmt, objectIdentifier);
        } catch (PersistableException e) {
            throw new RepositoryException(SqlMapObjectRepository.class,
                    MessageFormat.format("load_error", objectIdentifier), e);
        }
    }

    public int update(Identifiable object) throws RepositoryException {
        return update(updateStmt, object);
    }

    public int update(String updateId, Object object) throws RepositoryException {
        try {
            return sqlClient.update(updateId, object);
        } catch (PersistableException e) {
            throw new RepositoryException(SqlMapObjectRepository.class, MessageFormat.format("update_error", ""), e);
        }
    }

    public void insert(String insertId, Object object) throws RepositoryException {
        try {
            sqlClient.insert(insertId, object);
        } catch (PersistableException e) {
            throw new RepositoryException(SqlMapObjectRepository.class, MessageFormat.format("update_error", ""), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String stmtId, Object param, Class<?> clazz) {
        List<T> result = null;
        try {
            result = (List<T>) sqlClient.queryForList(stmtId, param);
        } catch (PersistableException e) {
            throw new RepositoryException(clazz.getClass(), MessageFormat.format(QUERY_ERROR, ""), e);
        }
        return result;
    }
}
