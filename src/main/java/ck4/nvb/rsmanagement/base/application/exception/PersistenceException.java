package ck4.nvb.rsmanagement.base.application.exception;

import java.io.Serial;

public class PersistenceException extends AppException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PersistenceException() {
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Object... arguments) {
        super(message, arguments);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
