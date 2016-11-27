package pl.jcommerce.apicat.contract.validation;


import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;

public interface ApiContractValidator {

    boolean support(ApiDefinition apiDefinition);

    boolean support(ApiSpecification apiSpecification);

    boolean support(ApiContract apiContract);

    boolean validate(ApiDefinition apiDefinition, ApiSpecification apiSpecification);

    boolean validate(ApiContract apiContract);

}

