package pl.jcommerce.apicat.web.exception;

import pl.jcommerce.apicat.contract.exception.ErrorCode;

import java.io.Serializable;

class ErrorResponse implements Serializable {

    private final String message;

    private ErrorCode errorCode;

    public ErrorResponse(String message) {
        this.message = message;
    }


    ErrorResponse(String message, ErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
