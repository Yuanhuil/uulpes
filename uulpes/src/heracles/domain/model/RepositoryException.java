package heracles.domain.model;

import heracles.exception.BaseNestableRuntimeException;
import heracles.exception.SeverityEnum;

public class RepositoryException extends BaseNestableRuntimeException {
    private static final long serialVersionUID = 6549397952748624490L;

    public RepositoryException(Class<?> source, String message) {
        super(source, message);
    }

    public RepositoryException(Class<?> source, String message, Throwable cause) {
        super(source, message, cause);
    }

    @Override
    public SeverityEnum getSeverity() {
        return SeverityEnum.ERROR;
    }
}
