package pl.jcommerce.apicat.web.exception;

import lombok.Getter;
import pl.jcommerce.apicat.contract.exception.ErrorCode;

import java.io.Serializable;

class ErrorResponse implements Serializable {

    @Getter
    private final String message;

    @Getter
    private ErrorCode errorCode;

    ErrorResponse(String message) {
        this.message = message;
    }

    ErrorResponse(String message, ErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
