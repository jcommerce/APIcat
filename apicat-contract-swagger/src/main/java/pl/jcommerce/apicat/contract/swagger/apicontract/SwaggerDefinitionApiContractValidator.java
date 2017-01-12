package pl.jcommerce.apicat.contract.swagger.apicontract;

import com.google.auto.service.AutoService;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.swagger.validation.MessageConstants;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.problem.ProblemLevel;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by krka on 23.10.2016.
 */
@AutoService(ApiContractValidator.class)
public class SwaggerDefinitionApiContractValidator extends SwaggerApiContractValidator {

    private Map<String, Model> apiSpecificationDefinitions;
    private ValidationResult result = new ValidationResult();

    @Override
    public ValidationResult validate(ApiDefinition apiDefinition, ApiSpecification apiSpecification) {
        result = new ValidationResult();
        apiSpecificationDefinitions = ((SwaggerApiSpecification) apiSpecification).getSwaggerDefinition().getDefinitions();
        Map<String, Model> apiDefinitionDefinitions = ((SwaggerApiDefinition) apiDefinition).getSwaggerDefinition().getDefinitions();

        apiDefinitionDefinitions.forEach(this::checkDefinitionExistence);

        return result;
    }

    private void checkDefinitionExistence(String definitionName, Model providerModel) {
        if(!apiSpecificationDefinitions.containsKey(definitionName)) {
            result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.DEFINITION_NOT_USED, definitionName), ProblemLevel.ERROR));
        } else {
            providerModel.getProperties().forEach((s, property) -> checkPropertyExistence(s, property, definitionName));
        }
    }

    private void checkPropertyExistence(String propertyKey, Property property, String definitionName) {
        if(!apiSpecificationDefinitions.get(definitionName).getProperties().containsKey(propertyKey)) {
            if(property.getRequired()) {
                result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, propertyKey, definitionName), ProblemLevel.ERROR));
            } else {
                result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, propertyKey, definitionName), ProblemLevel.WARN));
            }
        }
    }
}
