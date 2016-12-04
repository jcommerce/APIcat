package pl.jcommerce.apicat.contract.validation;


import pl.jcommerce.apicat.contract.ApiDefinition;

/**
 * Validates ApiSpecification
 *
 * @author Daniel Charczyński
 */
public interface ApiDefinitionValidator {

    /**
     * Verify if validator supports {@code apiDefinition}
     *
     * @param apiDefinition object to validate
     * @return
     */
    boolean support(ApiDefinition apiDefinition);

    /**
     * Validate {@code apiDefinition}
     *
     * @param apiDefinition - object to validate
     */
    //TODO: change to void
    boolean validate(ApiDefinition apiDefinition);
}

