package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public abstract class ApiSpecification {

    @Getter
    @Setter
    private ApiContract apiContract;

    private List<ApiSpecificationValidator> validators;

    @Getter
    @Setter
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
        if(!validatorAlreadyAdded(apiSpecificationValidator)){
            validators.add(apiSpecificationValidator);
        }
        apiValidated = false;
    }

    private boolean validatorAlreadyAdded(ApiSpecificationValidator apiDefinitionValidator) {
        for (ApiSpecificationValidator validator: validators) {
            if (validator.getClass().equals(apiDefinitionValidator.getClass())) {
                return true;
            }
        }
        return false;
    }

    public ValidationResult validate() {
        log.info("About to validate ApiSpecification: " + this);
        if (validators == null) {
            initValidators();
        }

        for (ApiSpecificationValidator apiSpecificationValidator : validators) {
            validationResult.merge(apiSpecificationValidator.validate(this));
        }

        apiValidated = true;
        return validationResult;
    }

    public boolean isApiValidated() {
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
    //TODO after ApiContract refactoring
//    public void validateAgainstApiDefinition(ApiDefinition apiDefinition) {
//        ApiContract temporaryContract = new ApiContract();
//        temporaryContract.setApiDefinition(apiDefinition);
//        temporaryContract.setApiSpecification(this);
//        temporaryContract.validate();
//    }

    public boolean isValid() {
        if(apiValidated) {
            return validationResult.getProblemList().isEmpty();
        }
        throw new IllegalStateException("Api specification hasn't been validated");
    }

    /**
     * Init validators
     *
     * @return validators
     */
    private void initValidators() {
        log.info("ApiSpecification - about to init validators. autodiscover validators: " + autodiscoverValidators);
        validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiSpecificationValidator.class).forEach(apiSpecificationValidator -> {
                if (apiSpecificationValidator.support(this)) {
                    log.info("Adding specification validator: " + apiSpecificationValidator);
                    validators.add(apiSpecificationValidator);

                    apiValidated = false;
                }
            });
        }
    }
}
