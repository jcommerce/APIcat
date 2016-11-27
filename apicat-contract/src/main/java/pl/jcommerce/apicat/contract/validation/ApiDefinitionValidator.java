package pl.jcommerce.apicat.contract.validation;


import pl.jcommerce.apicat.contract.ApiDefinition;

public interface ApiDefinitionValidator {

    boolean support(ApiDefinition apiDefinition);

    boolean validate(ApiDefinition apiDefinition);

}

