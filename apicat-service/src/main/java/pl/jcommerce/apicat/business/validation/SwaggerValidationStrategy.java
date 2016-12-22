package pl.jcommerce.apicat.business.validation;


import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinitionBuilder;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;

/**
 * Created by jada on 13.12.2016.
 */
public class SwaggerValidationStrategy extends ValidationStrategy {

    @Override
    public ApiSpecification getApiSpecificationFromContent(String content) {
        return SwaggerApiSpecification.fromContent(content);
    }

    @Override
    public ApiDefinition getApiDefinitionFromContent(String content) {
        return SwaggerApiDefinitionBuilder.fromContent(content).build();
    }
}