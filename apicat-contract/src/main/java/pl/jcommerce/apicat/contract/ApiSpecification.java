package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;
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
@Slf4j
public abstract class ApiSpecification {

    @Getter
    @Setter
    private ApiContract apiContract;

    private List<ApiSpecificationValidator> validators;

    @Getter
    @Setter
    private Optional<ValidationResult> validationResult = Optional.empty();

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
     * Add {@code apiSpecificationValidator}
     *
     * @param apiSpecificationValidator specification validator to be used
     */
    public void addValidator(ApiSpecificationValidator apiSpecificationValidator) {
        if (!apiSpecificationValidator.support(this))
            throw new ApicatSystemException("Provided apiSpecificationValidator doesn't support this specification");
        if (validators == null) {
            initValidators();
        }
        if (!isValidatorAlreadyAdded(apiSpecificationValidator)) {
            validators.add(apiSpecificationValidator);
        }
        validationResult = Optional.empty();
    }

    private boolean isValidatorAlreadyAdded(ApiSpecificationValidator apiDefinitionValidator) {
        for (ApiSpecificationValidator validator : validators) {
            if (validator.getClass().equals(apiDefinitionValidator.getClass())) {
                return true;
            }
        }
        return false;
    }

    public Optional<ValidationResult> validate() {
        log.info("About to validate ApiSpecification: " + this);
        if (validators == null) {
            initValidators();
        }

        validationResult = Optional.of(new ValidationResult());
        for (ApiSpecificationValidator apiSpecificationValidator : validators) {
            validationResult.get().merge(apiSpecificationValidator.validate(this));
        }

        return validationResult;
    }

    public boolean isApiValidated() {
        return validationResult.isPresent();
    }

    /**
     * Validate ApiContract assigned to this specification
     */
    public ValidationResult validateContract() {
        return validateContract(apiContract);
    }

    /**
     * Makes temporary ApiContract and do validation
     *
     * @param apiDefinition second part of contract
     */
    public ValidationResult validateAgainstApiDefinition(ApiDefinition apiDefinition) {
        ApiContract temporaryContract = new ApiContract();
        temporaryContract.setApiDefinition(apiDefinition);
        temporaryContract.setApiSpecification(this);

        return validateContract(temporaryContract);
    }

    private ValidationResult validateContract(ApiContract contract) {
        Optional<ValidationResult> result = contract.validate();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ApicatSystemException(ErrorCode.API_NOT_VALIDATED);
        }
    }

    public boolean isValid() {
        if (validationResult.isPresent()) {
            return validationResult.get().getProblemList().isEmpty();
        }
        throw new ApicatSystemException(ErrorCode.API_NOT_VALIDATED);
    }

    private void initValidators() {
        log.info("ApiSpecification - about to init validators. Autodiscover validators: " + autodiscoverValidators);
        validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiSpecificationValidator.class).forEach(apiSpecificationValidator -> {
                if (apiSpecificationValidator.support(this)) {
                    log.info("Adding specification validator: " + apiSpecificationValidator);
                    validators.add(apiSpecificationValidator);
                    validationResult = Optional.empty();
                }
            });
        }
    }
}
