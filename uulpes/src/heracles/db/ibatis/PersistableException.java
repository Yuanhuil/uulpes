package heracles.db.ibatis;

public class PersistableException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 3762572151799970455L;

    public PersistableException() {
    }

    public PersistableException(String message) {
        super(message);
    }

    public PersistableException(Throwable cause) {
        super(cause);
    }

    public PersistableException(String message, Throwable cause) {
        super(message, cause);
    }

}
