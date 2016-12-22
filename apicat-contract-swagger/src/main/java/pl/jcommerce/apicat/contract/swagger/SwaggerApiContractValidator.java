package pl.jcommerce.apicat.contract.swagger;

import com.google.auto.service.AutoService;
import io.swagger.models.Swagger;
import pl.jcommerce.apicat.contract.*;
import pl.jcommerce.apicat.contract.swagger.validation.ContractsValidator;
import pl.jcommerce.apicat.contract.swagger.validation.ContractsValidatorImpl;
import pl.jcommerce.apicat.contract.swagger.validation.Contract;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Created by krka on 31.10.2016.
 */
@AutoService(ApiContractValidator.class)
public class SwaggerApiContractValidator implements ApiContractValidator {

    private ContractsValidator contractsValidator = new ContractsValidatorImpl();


    @Override
    public boolean support(ApiDefinition apiDefinition, ApiSpecification apiSpecification) {
        boolean result = false;
        if (apiDefinition instanceof SwaggerApiDefinition  && apiSpecification instanceof SwaggerApiSpecification )
            result = true;
        return result;
    }

    @Override
    public boolean support(ApiContract apiContract) {
        boolean result = false;
        if (apiContract.getApiSpecification() instanceof SwaggerApiSpecification &&
                ((SwaggerApiSpecification) apiContract.getApiSpecification()).getSwaggerDefinition() != null)
            result = true;
        return result;
    }

    @Override
    public ValidationResult validate(ApiDefinition apiDefinition, ApiSpecification apiSpecification) {
        SwaggerApiDefinition swaggerApiDefinition = (SwaggerApiDefinition) apiDefinition;
        SwaggerApiSpecification swaggerApiSpecification = (SwaggerApiSpecification) apiSpecification;
        Contract contract = contractsValidator.validateContract(swaggerApiSpecification.getSwaggerDefinition(), swaggerApiDefinition.getSwaggerDefinition());
        //return contract.isValid();

        //TODO
        return new ValidationResult();
    }

    @Override
    public ValidationResult validate(ApiContract apiContract) {
        Swagger swaggerApiDefinition = ((SwaggerApiDefinition) apiContract.getApiDefinition()).getSwaggerDefinition();
        Swagger swaggerApiSpecification = ((SwaggerApiSpecification) apiContract.getApiSpecification()).getSwaggerDefinition();
        Contract contract = contractsValidator.validateContract(swaggerApiSpecification, swaggerApiDefinition);
        //return contract.isValid();

        //TODO
        return new ValidationResult();
    }
}
