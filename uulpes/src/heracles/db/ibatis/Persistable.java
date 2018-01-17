package heracles.db.ibatis;

public interface Persistable {
    void update() throws PersistableException;

    void insert() throws PersistableException;

    void delete() throws PersistableException;

    void loadMetas() throws PersistableException;
}
