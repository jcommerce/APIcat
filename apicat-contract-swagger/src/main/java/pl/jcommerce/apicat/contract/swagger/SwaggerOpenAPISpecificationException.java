package pl.jcommerce.apicat.contract.swagger;

import pl.jcommerce.apicat.contract.exception.ApiValidationException;

/**
 * Created by krka on 31.10.2016.
 */
public class SwaggerOpenAPISpecificationException extends ApiValidationException {
    public SwaggerOpenAPISpecificationException() {
        super("Unable to parse Swagger definition");
    }
}
