package pl.jcommerce.apicat.contract.swagger.apidefinition;

import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.ApiDefinitionValidator;

/**
 * Created by krka on 02.11.2016.
 */
public class SwaggerApiDefinitionBuilder {

    private static SwaggerApiDefinitionBuilder swaggerApiDefinitionBuilder = new SwaggerApiDefinitionBuilder();
    private SwaggerApiDefinition swaggerApiDefinition;

    public static SwaggerApiDefinitionBuilder fromPath(String path) {
        swaggerApiDefinitionBuilder.swaggerApiDefinition = SwaggerApiDefinition.fromPath(path);
        return swaggerApiDefinitionBuilder;
    }

    public static SwaggerApiDefinitionBuilder fromContent(String content) {
        swaggerApiDefinitionBuilder.swaggerApiDefinition = SwaggerApiDefinition.fromContent(content);
        return swaggerApiDefinitionBuilder;
    }

    public static SwaggerApiDefinitionBuilder fromStub(SwaggerApiDefinition swaggerApiDefinition) {
        swaggerApiDefinitionBuilder.swaggerApiDefinition = swaggerApiDefinition;
        return swaggerApiDefinitionBuilder;
    }

    public SwaggerApiDefinitionBuilder withoutAutodiscoveryValidators() {
        swaggerApiDefinitionBuilder.swaggerApiDefinition.setAutodiscoverValidators(Boolean.FALSE);
        return swaggerApiDefinitionBuilder;
    }

    public SwaggerApiDefinitionBuilder withContractedApiSpecification(ApiSpecification apiSpecification) {
        ApiContract apiContract = new ApiContract();
        apiContract.setApiSpecification(apiSpecification);
        apiContract.setApiDefinition(swaggerApiDefinitionBuilder.swaggerApiDefinition);
        swaggerApiDefinitionBuilder.swaggerApiDefinition.addContract(apiContract);
        return swaggerApiDefinitionBuilder;
    }

    public SwaggerApiDefinitionBuilder withApiDefinitionValidator(ApiDefinitionValidator apiDefinitionValidator) {
        swaggerApiDefinitionBuilder.swaggerApiDefinition.addValidator(apiDefinitionValidator);
        return swaggerApiDefinitionBuilder;
    }

    public SwaggerApiDefinitionBuilder withApiContractValidator(ApiContractValidator apiContractValidator) {
        swaggerApiDefinitionBuilder.swaggerApiDefinition.addContractValidator(apiContractValidator);
        return swaggerApiDefinitionBuilder;
    }

    public SwaggerApiDefinition build() {
        return swaggerApiDefinition;
    }

}
