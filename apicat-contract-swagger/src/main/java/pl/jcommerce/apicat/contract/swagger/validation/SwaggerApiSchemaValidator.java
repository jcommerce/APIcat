package pl.jcommerce.apicat.contract.swagger.validation;

import io.swagger.validate.SwaggerSchemaValidator;

/**
 * Created by luwa on 21.12.16.
 */
public final class SwaggerApiSchemaValidator extends SwaggerSchemaValidator {

    public SwaggerApiSchemaValidator() {
        super("/schemas/json/swaggerApi20Schema.json");
    }
}
