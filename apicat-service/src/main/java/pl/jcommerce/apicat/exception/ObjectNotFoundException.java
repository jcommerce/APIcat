package pl.jcommerce.apicat.exception;

/**
 * Created by luwa on 19.01.17.
 */
public class ObjectNotFoundException extends RuntimeException {

    ObjectType objectType;

    public ObjectNotFoundException(ObjectType errorCode) {
        this.objectType = errorCode;
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(ObjectType errorCode, String message) {
        super(message);
        this.objectType = errorCode;
    }
}
