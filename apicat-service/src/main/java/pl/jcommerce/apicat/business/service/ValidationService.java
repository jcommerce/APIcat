package pl.jcommerce.apicat.business.service;

import org.springframework.stereotype.Service;
import pl.jcommerce.apicat.business.validation.SwaggerValidationStrategy;
import pl.jcommerce.apicat.business.validation.ValidationStrategy;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.contract.validation.result.ValidationResultCategory;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.model.ApiFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jada on 12.12.2016.
 */
@Service
public class ValidationService {

    private static Map<ApiFormat, ValidationStrategy> validationStrategies = new HashMap<>();

    {
        validationStrategies.put(ApiFormat.SWAGGER, new SwaggerValidationStrategy());
    }

    public boolean isContractValid(ApiContractEntity contractEntity) {
        ValidationStrategy strategy = getValidationStrategy(contractEntity.getDefinition().getFormat());

        return strategy.isContractValid(contractEntity);
    }

    public ValidationResult validateDefinition(ApiDefinitionEntity definitionEntity) {
        ValidationStrategy strategy = getValidationStrategy(definitionEntity.getFormat());

        return strategy.validateDefinition(definitionEntity);
    }

    public boolean isDefinitionValid(ApiDefinitionEntity definitionEntity) {
        ValidationResult result = validateDefinition(definitionEntity);


        return result.getValidationResultCategory() == ValidationResultCategory.CORRECT;
    }

    private ValidationStrategy getValidationStrategy(ApiFormat format) {
        ValidationStrategy strategy = validationStrategies.get(format);

        if (strategy == null) {
            throw new UnsupportedOperationException();
        }

        return strategy;
    }

}
