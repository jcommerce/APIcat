package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

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
     * @param apiContractValidator
     */
    public void addValidator(ApiContractValidator apiContractValidator) {
        if (!apiContractValidator.support(this)) {
            throw new ApicatSystemException("Provided apiContractValidator doesn't support this contract");
        }
        if (validators == null) {
            initValidators();
        }
        if (!validatorAlreadyAdded(apiContractValidator)) {
            validators.add(apiContractValidator);
        }

        validationResult = Optional.empty();
    }

    private boolean validatorAlreadyAdded(ApiContractValidator apiContractValidator) {
        for (ApiContractValidator validator : validators) {
            if (validator.getClass().equals(apiContractValidator.getClass())) {
                return true;
            }
        }
        return false;
    }

    public Optional<ValidationResult> validate() {
        log.info("About to validate ApiContract: " + this);
        if (!apiSpecification.isApiValidated()) {
            apiSpecification.validate();
        }
        if (!apiDefinition.isApiValidated()) {
            apiDefinition.validate();
        }
        if (validators == null) {
            initValidators();
        }

        validationResult = Optional.of(new ValidationResult());
        for (ApiContractValidator apiContractValidator : validators) {
            validationResult.get().merge(apiContractValidator.validate(this));
        }

        return validationResult;
    }

    private void initValidators() {
        log.info("ApiContract - about to init validators. autodiscover validators: " + autodiscoverValidators);
        validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiContractValidator.class).forEach(apiContractValidator -> {
                if (apiContractValidator.support(this)) {
                    log.info("Adding contract validator: " + apiContractValidator);
                    validators.add(apiContractValidator);
                    validationResult = null;
                }
            });
        }
    }

    public boolean isValid() {
        if (validationResult.isPresent()) {
            return validationResult.get().getProblemList().isEmpty();
        }
        throw new IllegalStateException("Api contract hasn't been validated");
    }
}
