package pl.jcommerce.apicat.contract.swagger.apicontract;

import pl.jcommerce.apicat.contract.*;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Created by krka on 31.10.2016.
 */
public abstract class SwaggerApiContractValidator implements ApiContractValidator {

    @Override
    public boolean support(ApiDefinition apiDefinition, ApiSpecification apiSpecification) {
        return apiDefinition instanceof SwaggerApiDefinition && apiSpecification instanceof SwaggerApiSpecification;
    }

    @Override
    public boolean support(ApiContract apiContract) {
        return apiContract.getApiSpecification() instanceof SwaggerApiSpecification &&
                ((SwaggerApiSpecification) apiContract.getApiSpecification()).getSwaggerDefinition() != null;
    }

    @Override
    public ValidationResult validate(ApiContract apiContract) {
        return validate(apiContract.getApiDefinition(), apiContract.getApiSpecification());
    }
}
