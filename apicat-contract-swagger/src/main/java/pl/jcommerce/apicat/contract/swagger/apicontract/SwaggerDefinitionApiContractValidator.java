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
import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(ApiContractValidator.class)
public class SwaggerDefinitionApiContractValidator extends SwaggerApiContractValidator {

    private Map<String, Model> apiSpecificationDefinitions;
    private ValidationResult result = new ValidationResult();

    @Override
    public ValidationResult validate(ApiDefinition apiDefinition, ApiSpecification apiSpecification) {
        result = new ValidationResult();
        apiSpecificationDefinitions = cloneDefinitions(apiSpecification);
        Map<String, Model> apiDefinitionDefinitions = ((SwaggerApiDefinition) apiDefinition).getSwaggerDefinition().getDefinitions();

        for (Map.Entry<String, Model> definition : apiDefinitionDefinitions.entrySet()) {
            checkDefinitionExistence(definition.getKey(), definition.getValue());
        }

        if (!apiSpecificationDefinitions.isEmpty()) {
            createErrors();
        }

        return result;
    }

    private void checkDefinitionExistence(String definitionName, Model providerModel) {
        if (!apiSpecificationDefinitions.containsKey(definitionName)) {
            result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.DEFINITION_NOT_USED, definitionName), ProblemLevel.ERROR));
        } else {
            for (Map.Entry<String, Property> property : providerModel.getProperties().entrySet()) {
                checkPropertyExistence(property.getKey(), property.getValue(), definitionName);
            }
        }
        if (apiSpecificationDefinitions.get(definitionName) != null && apiSpecificationDefinitions.get(definitionName).getProperties().isEmpty()) {
            apiSpecificationDefinitions.remove(definitionName);
        }
    }

    private void checkPropertyExistence(String propertyKey, Property property, String definitionName) {
        if (!apiSpecificationDefinitions.get(definitionName).getProperties().containsKey(propertyKey)) {
            if (property.getRequired()) {
                result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, propertyKey, definitionName), ProblemLevel.ERROR));
            } else {
                result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, propertyKey, definitionName), ProblemLevel.WARN));
            }
        } else {
            apiSpecificationDefinitions.get(definitionName).getProperties().remove(propertyKey);
        }
    }

    private void createErrors() {
        for (Map.Entry<String, Model> definition : apiSpecificationDefinitions.entrySet()) {
            for (String property : definition.getValue().getProperties().keySet()) {
                result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_EXISTS, property, definition.getKey()), ProblemLevel.ERROR));
            }
        }
    }

    private Map<String, Model> cloneDefinitions(ApiSpecification apiSpecification) {
        Map<String, Model> definitions = ((SwaggerApiSpecification) apiSpecification).getSwaggerDefinition().getDefinitions();
        Map<String, Model> clone = new LinkedHashMap<>(definitions);
        for (Map.Entry<String, Model> entry : definitions.entrySet()) {
            clone.replace(entry.getKey(), (Model) entry.getValue().clone());
        }

        return clone;
    }
}
