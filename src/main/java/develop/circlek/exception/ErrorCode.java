package develop.circlek.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    USERNAME(1000, "UserName must be at leat 3 characters"),
    PASSWORD(1001, "PassWord must be at leat 5 characters"),
    INVALID_KEY(1000, "Invalid message key"),
    ;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    int code;
    String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

