package edutec.exception;

public class QueryException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = -1922821344598878998L;

    public QueryException() {
    }

    public QueryException(Throwable cause) {
        super(cause);
    }

    public QueryException(String message) {
        super(message);
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
