package pl.jcommerce.apicat.contract.validation;

import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Validates ApiContract
 *
 * @author Daniel Charczyński
 */
public interface ApiContractValidator {

    /**
     * Verify if validator supports {@code apiDefinition} and {@code apiSpecification}
     *
     * @param apiDefinition    object to validate
     * @param apiSpecification object to validate
     * @return validator support check result
     */
    boolean support(ApiDefinition apiDefinition, ApiSpecification apiSpecification);

    /**
     * Verify if validator supports {@code apiContract}
     *
     * @param apiContract object to validate
     * @return validator support check result
     */
    boolean support(ApiContract apiContract);

    /**
     * Validate {@code apiDefinition} and {@code apiSpecification}
     *
     * @param apiDefinition    object to validate
     * @param apiSpecification object to validate
     * @return list of all validation problems
     */
    ValidationResult validate(ApiDefinition apiDefinition, ApiSpecification apiSpecification);

    /**
     * Validate {@code apiContract}
     *
     * @param apiContract object to validate
     * @return list of all validation problems
     */
    ValidationResult validate(ApiContract apiContract);

}
