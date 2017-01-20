package pl.jcommerce.apicat.contract.exception;

import lombok.Getter;

public class ApicatSystemException extends RuntimeException {

    @Getter
    private ErrorCode errorCode;

    public ApicatSystemException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ApicatSystemException(String message) {
        super(message);
    }

    public ApicatSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApicatSystemException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
