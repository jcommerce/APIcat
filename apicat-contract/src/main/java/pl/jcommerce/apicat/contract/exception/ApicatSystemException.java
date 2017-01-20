package pl.jcommerce.apicat.contract.exception;

public class ApicatSystemException extends RuntimeException {

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

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
