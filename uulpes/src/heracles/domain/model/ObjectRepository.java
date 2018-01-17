package heracles.domain.model;

public interface ObjectRepository {
    /**
     * Delete an object using it's object ID (OID).
     * 
     * @param objectIdentifier
     */
    int delete(long objectIdentifier) throws RepositoryException;

    /**
     * Load an instance of an object
     * 
     * @param objectIdentifier
     */
    Object load(long objectIdentifier) throws RepositoryException;

    /**
     * Create a new instance in the repository
     * 
     * @param object
     *            the object to insert
     * @return the object identifier
     */
    void insert(Identifiable object) throws RepositoryException;

    /**
     * Updates an object in the repository. Note: This is a no-op for Hibernate.
     * 
     * @param object
     *            - the object to update
     */
    int update(Identifiable object) throws RepositoryException;
}
