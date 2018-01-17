package heracles.exception;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base runtime exception: extendes RuntimeException providing logging and
 * exception nesting functionalities.
 */
public abstract class BaseNestableRuntimeException extends RuntimeException {

    /** 
     * 
     */
    private static final long serialVersionUID = 1L;
    final static String NestableException_msg = "NestableException.msg {0}.{1}";
    final static String NestableException_msgcause = "NestableException.msgcause.{0}.{1}.{2}";
    /**
     * Class where the exception has been generated.
     */
    private final Class<?> sourceClass;

    /**
     * previous exception.
     */
    private Throwable nestedException;

    /**
     * Instantiate a new BaseNestableRuntimeException.
     * 
     * @param source
     *            Class where the exception is generated
     * @param message
     *            message
     */
    public BaseNestableRuntimeException(Class<?> source, String message) {
        super(message);
        this.sourceClass = source;
        // log exception
        Log log = LogFactory.getLog(source);
        // choose appropriate logging method
        if (getSeverity() == SeverityEnum.DEBUG) {
            log.debug(toString());
        } else if (getSeverity() == SeverityEnum.INFO) {
            log.info(toString());
        } else if (getSeverity() == SeverityEnum.WARN) {
            log.warn(toString());
        } else {
            // error - default
            log.error(toString());
        }

    }

    /**
     * Instantiate a new BaseNestableRuntimeException.
     * 
     * @param source
     *            Class where the exception is generated
     * @param message
     *            message
     * @param cause
     *            previous Exception
     */
    public BaseNestableRuntimeException(Class<?> source, String message, Throwable cause) {
        super(message);
        this.sourceClass = source;
        this.nestedException = cause;

        // log exception
        Log log = LogFactory.getLog(source);

        // choose appropriate logging method
        if (getSeverity() == SeverityEnum.DEBUG) {
            log.debug(toString(), cause);
        } else if (getSeverity() == SeverityEnum.INFO) {
            log.info(toString(), cause);
        } else if (getSeverity() == SeverityEnum.WARN) {
            log.warn(toString(), cause);
        } else {
            // error - default
            log.error(toString(), cause);
        }

    }

    /**
     * returns the previous exception.
     * 
     * @return Throwable previous exception
     */
    public Throwable getCause() {
        return this.nestedException;
    }

    /**
     * basic toString. Returns the message plus the previous exception (if a
     * previous exception exists).
     * 
     * @return String
     */
    public String toString() {
        String className = this.sourceClass.getName();
        className = className.substring(className.lastIndexOf(".")); //$NON-NLS-1$

        if (this.nestedException == null) {
            return MessageFormat.format(NestableException_msg, new Object[] { className, getMessage() });
        }

        return MessageFormat.format(NestableException_msgcause,
                new Object[] { className, getMessage(), this.nestedException.getMessage() });
    }

    /**
     * subclasses need to define the getSeverity method to provide correct
     * severity for logging.
     * 
     * @return SeverityEnum exception severity
     * @see org.displaytag.exception.SeverityEnum
     */
    public abstract SeverityEnum getSeverity();

}