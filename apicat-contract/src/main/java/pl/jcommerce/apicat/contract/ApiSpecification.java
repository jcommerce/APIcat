package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.validation.ApiSpecificationValidator;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

import java.util.*;

/**
 * Defines which components of our system API are required by customer system.
 * Those requirements are assigned to specified ApiDefinition via ApiContract.
 *
 * This class creates an abstraction over specific implementations.
 *
 * @author Daniel Charczyński
 */
public abstract class ApiSpecification {

    private final Logger logger = LoggerFactory.getLogger(ApiSpecification.class);

    /**
     * ApiContract
     */
    @Getter
    @Setter
    private ApiContract apiContract;

    /**
     * ApiContract validators
     */
    private List<ApiSpecificationValidator> validators;

    private ValidationResult validationResult = new ValidationResult();

    private boolean apiValidated = false;

    /**
     * Api specification name
     */
    @Getter
    @Setter
    private String name;
    /**
     * Api specification version
     */
    @Getter
    @Setter
    private String version;
    /**
     * Api specification stage
     */
    @Getter
    @Setter
    private ApiSpecificationStage stage;
    /**
     * Api specification author
     */
    @Getter
    @Setter
    private String author;
    /**
     * Api specification text content
     */
    @Getter
    @Setter
    private String content;
    /**
     * Information if validators should be loaded automatically
     */
    @Getter
    @Setter
    private boolean autodiscoverValidators = true;
    /**
     * Information if ApiSpecification is valid
     * This flag is set after successful validation
     */
    @Getter
    private Optional<Boolean> valid = Optional.empty();

    /**
     * Retrieves ApiSpecification types from available implementations
     *
     * @return all available types
     */
    public Set<String> getAvailableTypes() {
        return ApiSpecificationFactory.getTypes();
    }

    /**
     * This value have to be unique according to all implementations
     *
     * @return specific implementation type
     */
    public abstract String getType();

    /**
     * Add {@code apiContractValidator}
     *
     * @param apiSpecificationValidator
     */
    public void addValidator(ApiSpecificationValidator apiSpecificationValidator) {
        if (!apiSpecificationValidator.support(this))
            throw new ApicatSystemException("Provided apiSpecificationValidator doesn't support this specification");
        if (validators == null){
            initValidators();
        }
        validators.add(apiSpecificationValidator);
        apiValidated = false;
    }

    /**
     * Validate specification
     */
    public ValidationResult validate() {
        logger.info("About to validate ApiSpecification: " + this);
        if (validators == null) {
            initValidators();
        }

        for (ApiSpecificationValidator apiSpecificationValidator : validators) {
            validationResult.merge(apiSpecificationValidator.validate(this));
        }

        apiValidated = true;
        return validationResult;
    }

    public boolean isValidated() {
        return apiValidated;
    }

    /**
     * Validate ApiContract
     */
    public void validateContract() {
        apiContract.validate();
    }

    /**
     * Makes temporary ApiContract and do validation
     *
     * @param apiDefinition second part of contract
     */
    public void validateAgainstApiDefinition(ApiDefinition apiDefinition) {
        ApiContract temporaryContract = new ApiContract();
        temporaryContract.setApiDefinition(apiDefinition);
        temporaryContract.setApiSpecification(this);
        temporaryContract.validate();
    }

    public boolean isValid() {
        if(apiValidated){
            return validationResult.getProblemList().isEmpty();
        }
        throw new IllegalStateException("Api specificatioin hasn't been validated");
    }

    /**
     * Init validators
     *
     * @return validators
     */
    private void initValidators() {
        logger.info("ApiSpecification - about to init validators. autodiscover validators: " + autodiscoverValidators);
        validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiSpecificationValidator.class).forEach(apiSpecificationValidator -> {
                if (apiSpecificationValidator.support(this)) {
                    logger.info("Adding specification validator: " + apiSpecificationValidator);
                    validators.add(apiSpecificationValidator);

                    apiValidated = false;
                }
            });
        }
    }
}
