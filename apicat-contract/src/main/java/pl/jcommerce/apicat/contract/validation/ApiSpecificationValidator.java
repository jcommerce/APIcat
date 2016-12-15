package pl.jcommerce.apicat.contract.validation;

import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Validates ApiSpecification
 *
 * @author Daniel Charczyński
 */
public interface ApiSpecificationValidator {

    /**
     * Verify if validator supports {@code apiSpecification}
     *
     * @param apiSpecification object to validate
     * @return validator support check result
     */
    boolean support(ApiSpecification apiSpecification);

    /**
     * Validate {@code apiSpecification}
     *
     * @param apiSpecification object to validate
     * @return list with all validation problems
     */
    ValidationResult validate(ApiSpecification apiSpecification);
}

