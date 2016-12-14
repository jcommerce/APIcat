package pl.jcommerce.apicat.contract.swagger;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.ApiDefinition;


/**
 * Created by krka on 31.10.2016.
 */

public class SwaggerApiDefinition extends ApiDefinition {

    @Getter
    @Setter
    private Swagger swaggerDefinition;

    public static SwaggerApiDefinition empty() {
        return new SwaggerApiDefinition();
    }

    public static SwaggerApiDefinition fromContent(String content) {
        Swagger swaggerDefinition = new SwaggerParser().parse(content);
        if (swaggerDefinition == null)
            throw new SwaggerOpenAPISpecificationException();
        SwaggerApiDefinition swaggerApiDefinition = new SwaggerApiDefinition();
        swaggerApiDefinition.setSwaggerDefinition(swaggerDefinition);
        return swaggerApiDefinition;
    }

    public static SwaggerApiDefinition fromPath(String path) {
        Swagger swaggerDefinition = new SwaggerParser().read(path);
        if (swaggerDefinition == null) {
            throw new SwaggerOpenAPISpecificationException();
        }
        SwaggerApiDefinition swaggerApiDefinition = new SwaggerApiDefinition();
        swaggerApiDefinition.setSwaggerDefinition(swaggerDefinition);
        return swaggerApiDefinition;
    }


//    @Override
//    public void validateAllContracts()  {
//        for (ApiContract apiContract : apiContracts) {
//            if (!validateContract(apiContract)) {
//                valid = false;
//                break;
//            }
//        }
//    }

//    private boolean validateContract(ApiContract apiContract) {
//        Optional<ApiContractValidator> apiContractValidator = apiContractValidators.stream().filter(a -> a.support(apiContract)).findAny();
//        return !(apiContractValidator.isPresent() && !apiContractValidator.get().validate(apiContract));
//    }

//    @Override
//    public void validateDefinition() {
//        Optional<ApiDefinitionValidator> apiDefinitionValidator = validators.stream().filter(a -> a.support(this)).findAny();
//        if (apiDefinitionValidator.isPresent()) {
//            if(!apiDefinitionValidator.get().validate(this))
//                valid = false;
//        } else
//            valid = false;
//    }

//    @Override
//    public void validateSpecifications(ApiDefinition apiDefinition, ApiSpecification apiSpecification) {
//        Optional<ApiContractValidator> apiContractValidator = apiContractValidators.stream().filter(a -> a.support(apiDefinition, apiSpecification)).findAny();
//        if (apiContractValidator.isPresent()) {
//            if(!apiContractValidator.get().validate(apiDefinition, apiSpecification))
//                valid = false;
//        } else
//            valid = false;
//    }
}
