package pl.jcommerce.apicat.web.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.jcommerce.apicat.business.exception.IdDoesNotMatchException;
import pl.jcommerce.apicat.business.exception.ResourceNotFoundException;
import pl.jcommerce.apicat.contract.exception.ApiValidationException;


/**
 * Created by jada on 06.12.2016.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Path and Body id does not match")
    @ExceptionHandler(IdDoesNotMatchException.class)
    public void handleIdConflict() {
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No such resource")
    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException() {
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid api definition")
    @ExceptionHandler(ApiValidationException.class)
    public void handleApiValidationException() {
    }
}
