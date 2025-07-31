package develop.circlek.base.application.exception;

public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message, 400);
    }
}