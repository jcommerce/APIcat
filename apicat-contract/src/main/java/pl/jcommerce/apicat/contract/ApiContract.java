package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.contract.validation.result.ValidationResultCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * Connects ApiSpecification delivered by customer with ApiDefinition used by it.
 *
 * @author Daniel Charczyński
 */
@Slf4j
public class ApiContract {

    /**
     * ApiContract validators
     */
    private List<ApiContractValidator> validators;

    /**
     * Contract target Api
     */
    @Getter
    @Setter
    private ApiDefinition apiDefinition;

    /**
     * Contract specification
     */
    @Getter
    @Setter
    private ApiSpecification apiSpecification;

    /**
     * Information if validators should be loaded automatically
     */
    @Getter
    @Setter
    private boolean autodiscoverValidators = true;

    @Getter
    @Setter
    private Optional<ValidationResult> validationResult = Optional.empty();

    /**
     * Add {@code apiContractValidator}
     *
     * @param apiContractValidator contract validator to be used
     */
    public void addValidator(ApiContractValidator apiContractValidator) {
        if (!apiContractValidator.support(this)) {
            throw new ApicatSystemException("Provided apiContractValidator doesn't support this contract");
        }
        if (validators == null) {
            initValidators();
        }
        if (!isValidatorAlreadyAdded(apiContractValidator)) {
            validators.add(apiContractValidator);
        }

        validationResult = Optional.empty();
    }

    private boolean isValidatorAlreadyAdded(ApiContractValidator apiContractValidator) {
        for (ApiContractValidator validator : validators) {
            if (validator.getClass().equals(apiContractValidator.getClass())) {
                return true;
            }
        }
        return false;
    }

    public Optional<ValidationResult> validate() {
        log.info("About to validate ApiContract: " + this);

        Optional<ValidationResult> checkDefinitionAndSpecificationResult = checkDefinitionAndSpecification();

        if (checkDefinitionAndSpecificationResult.isPresent()) {
            if (checkDefinitionAndSpecificationResult.get().getValidationResultCategory().equals(ValidationResultCategory.ERROR)) {
                validationResult = checkDefinitionAndSpecificationResult;
                return validationResult;
            }
        }

        if (validators == null) {
            initValidators();
        }

        validationResult = checkDefinitionAndSpecificationResult;
        for (ApiContractValidator apiContractValidator : validators) {
            validationResult.get().merge(apiContractValidator.validate(this));
        }

        return validationResult;
    }

    private Optional<ValidationResult> checkDefinitionAndSpecification() {
        Optional<ValidationResult> result = Optional.of(new ValidationResult());
        if (!apiSpecification.isApiValidated()) {
            result.get().merge(apiSpecification.validate().get());
        } else {
            result.get().merge(apiSpecification.getValidationResult().get());
        }

        if (!apiDefinition.isApiValidated()) {
            result.get().merge(apiDefinition.validate().get());
        } else {
            result.get().merge(apiDefinition.getValidationResult().get());
        }

        return result;
    }

    private void initValidators() {
        log.info("ApiContract - about to init validators. autodiscover validators: " + autodiscoverValidators);
        validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiContractValidator.class).forEach(apiContractValidator -> {
                if (apiContractValidator.support(this)) {
                    log.info("Adding contract validator: " + apiContractValidator);
                    validators.add(apiContractValidator);
                    validationResult = Optional.empty();
                }
            });
        }
    }

    public boolean isValid() {
        if (validationResult.isPresent()) {
            return validationResult.get().getProblemList().isEmpty();
        }
        throw new ApicatSystemException(ErrorCode.API_NOT_VALIDATED);
    }
}
