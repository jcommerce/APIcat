package pl.jcommerce.apicat.contract.exception;

public class ApicatSystemException extends RuntimeException {

    public ApicatSystemException(String message) {
        super(message);
    }

    public ApicatSystemException(String message, Throwable cause) {
        super(message, cause);
    }

}
