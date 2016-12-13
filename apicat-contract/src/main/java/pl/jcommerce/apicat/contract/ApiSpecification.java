package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.validation.ApiSpecificationValidator;

import java.util.*;

/**
 * ApiSpecification defines API requirements using specific implementation/format
 * This class creates an abstraction over specific implementations.
 * <p>
 * Assignment those requirements to specific ApiDefinition is done via ApiContract
 *
 * @author Daniel Charczyński
 */
public abstract class ApiSpecification {

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
        if (validators == null)
            validators = initValidators();
        validators.add(apiSpecificationValidator);
        valid = Optional.empty();
    }

    /**
     * Validate specification
     */
    public void validate() {
        System.out.println("About to validate ApiSpecification: " + this);
        if (validators == null)
            validators = initValidators();
        validators.forEach(apiSpecificationValidator -> apiSpecificationValidator.validate(this));
        valid = Optional.of(true);
    }

    public boolean isValidated() {
        return valid.isPresent();
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
    public void validateAgainsApiDefinition(ApiDefinition apiDefinition) {
        ApiContract temporaryContract = new ApiContract();
        temporaryContract.setApiDefinition(apiDefinition);
        temporaryContract.setApiSpecification(this);
        temporaryContract.validate();
    }


    /**
     * Init validators
     *
     * @return validators
     */
    private List<ApiSpecificationValidator> initValidators() {
        System.out.println("ApiSpecification - about to init validators. autodiscover validators: " + autodiscoverValidators);
        List<ApiSpecificationValidator> validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiSpecificationValidator.class).forEach(apiSpecificationValidator -> {
                if (apiSpecificationValidator.support(this)) {
                    System.out.println("Adding validator: " + apiSpecificationValidator);
                    //addValidator(apiSpecificationValidator); TODO verify - stack overflow (addValidator invokes initValidators, so initValidators cannot invoke addValidator)
                    validators.add(apiSpecificationValidator);
                    valid = Optional.empty();
                }
            });
        }
        return validators;
    }
}
