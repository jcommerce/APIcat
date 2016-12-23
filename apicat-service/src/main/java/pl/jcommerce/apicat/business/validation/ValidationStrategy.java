package pl.jcommerce.apicat.business.validation;


import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;

/**
 * Created by jada on 13.12.2016.
 */
public abstract class ValidationStrategy {
    abstract ApiSpecification getApiSpecificationFromContent(String content);

    abstract ApiDefinition getApiDefinitionFromContent(String content);

    public ValidationResult validateDefinition(ApiDefinitionEntity definitionEntity) {
        ApiDefinition definition = getApiDefinitionFromContent(definitionEntity.getContent());

        return definition.validate().orElse(null);
    }

    //TODO: should return ValidationResult
    public boolean isContractValid(ApiContractEntity contractEntity) {
        String definitionContent = contractEntity.getDefinition().getContent();
        String specificationContent = contractEntity.getSpecification().getContent();

        ApiDefinition definition = getApiDefinitionFromContent(definitionContent);
        ApiSpecification specification = getApiSpecificationFromContent(specificationContent);

        //ApiContract contract = new ApiContract();
        //contract.setApiDefinition(definition);
        //contract.setApiSpecification(specification);

        //contract.validate();

        //return contract.getValid().orElse(false);

        return false;
    }
}
