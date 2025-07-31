package develop.circlek.base.application.exception;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}