package pl.jcommerce.apicat.contract.validation;


import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Created by jada on 13.12.2016.
 */
public abstract class ValidationStrategy {
    protected abstract ApiSpecification getApiSpecificationFromContent(String content);

    protected abstract ApiDefinition getApiDefinitionFromContent(String content);

    public ValidationResult validateDefinition(String definitionContent) {
        ApiDefinition definition = getApiDefinitionFromContent(definitionContent);

        return definition.validate();
    }

    //TODO: should return ValidationResult
    public boolean isContractValid(String definitionContent, String specificationContent) {
        ApiDefinition definition = getApiDefinitionFromContent(definitionContent);
        ApiSpecification specification = getApiSpecificationFromContent(specificationContent);

        ApiContract contract = new ApiContract();
        contract.setApiDefinition(definition);
        contract.setApiSpecification(specification);

        contract.validate();

        return contract.getValid().orElse(false);
    }
}
