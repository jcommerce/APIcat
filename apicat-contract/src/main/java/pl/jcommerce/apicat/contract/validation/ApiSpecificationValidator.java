package pl.jcommerce.apicat.contract.validation;

import pl.jcommerce.apicat.contract.ApiSpecification;

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
     * @return
     */
    boolean support(ApiSpecification apiSpecification);

    /**
     * Validate {@code apiSpecification}
     *
     * @param apiSpecification object to validate
     */
    //TODO: change to void
    boolean validate(ApiSpecification apiSpecification);

}

