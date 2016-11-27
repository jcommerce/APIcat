package pl.jcommerce.apicat.contract.swagger;

import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.validation.ApiDefinitionValidator;

/**
 * Created by krka on 31.10.2016.
 */
public class SwaggerApiDefinitionValidator implements ApiDefinitionValidator {

    @Override
    public boolean support(ApiDefinition apiDefinition) {
        boolean result = false;
        if(apiDefinition instanceof SwaggerApiDefinition &&
                ((SwaggerApiDefinition) apiDefinition).getSwaggerDefinition() != null)
            result = true;
        return result;
    }

    @Override
    public boolean validate(ApiDefinition apiDefinition) {
        boolean isValid = true;
        SwaggerApiDefinition swaggerApiDefinition = (SwaggerApiDefinition) apiDefinition;
        if (swaggerApiDefinition.getSwaggerDefinition() == null)
            isValid = false;
        return isValid;
    }
}
