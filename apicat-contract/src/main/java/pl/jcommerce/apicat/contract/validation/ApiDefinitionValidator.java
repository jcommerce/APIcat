package pl.jcommerce.apicat.contract.validation;

import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

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
     * @return validator support check result
     */
    boolean support(ApiDefinition apiDefinition);

    /**
     * Validate {@code apiDefinition}
     *
     * @param apiDefinition - object to validate
     * @return list with all validation problems
     */
    ValidationResult validate(ApiDefinition apiDefinition);
}
