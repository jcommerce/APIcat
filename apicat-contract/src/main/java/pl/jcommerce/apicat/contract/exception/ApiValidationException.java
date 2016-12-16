package pl.jcommerce.apicat.contract.exception;


public class ApiValidationException extends RuntimeException {

    public ApiValidationException(String message) {
        super(message);
    }

    public ApiValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
