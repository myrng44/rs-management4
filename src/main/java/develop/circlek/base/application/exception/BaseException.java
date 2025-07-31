package develop.circlek.base.application.exception;

public abstract class BaseException extends RuntimeException {
    private final int code;
    
    public BaseException(String message) {
        super(message);
        this.code = 400;
    }
    
    public BaseException(String message, int code) {
        super(message);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}