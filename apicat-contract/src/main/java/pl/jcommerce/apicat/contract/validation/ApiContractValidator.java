package pl.jcommerce.apicat.contract.validation;


import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;

/**
 * Validates ApiContract
 *
 * @author Daniel Charczyński
 */
public interface ApiContractValidator {

    /**
     * Verify if validator supports {@code apiDefinition} and {@code apiSpecification}
     *
     * @param apiDefinition object to validate
     * @param apiSpecification object to validate
     * @return
     */
    boolean support(ApiDefinition apiDefinition, ApiSpecification apiSpecification);

    /**
     * Verify if validator supports {@code apiContract}
     *
     * @param apiContract object to validate
     * @return
     */
    boolean support(ApiContract apiContract);

    /**
     * Validate {@code apiDefinition} and {@code apiSpecification}
     *
     * @param apiDefinition object to validate
     * @param apiSpecification object to validate
     * @return
     */
    //TODO: change to void
    boolean validate(ApiDefinition apiDefinition, ApiSpecification apiSpecification);

    /**
     * Validate {@code apiContract}
     *
     * @param apiContract object to validate
     * @return
     */
    //TODO: change to void
    boolean validate(ApiContract apiContract);

}

