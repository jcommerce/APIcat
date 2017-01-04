package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.ApiDefinitionValidator;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * Defines API exposed by our system.
 * Contains list of all contracts using it.
 *
 * @author Daniel Charczyński
 */
@Slf4j
public abstract class ApiDefinition {

    //TODO: adjust to model
    //TODO: move ApiContractValidator to APIContract
    //TODO: all methods should be implemented

    protected String name;

    @Getter
    @Setter
    protected Boolean autodiscoverValidators = Boolean.TRUE;

    private List<ApiDefinitionValidator> validators = null;

    @Getter
    private Optional<ValidationResult> validationResult = Optional.empty();

    @Getter
    private Optional<ValidationResult> contractsValidationResults = Optional.empty();
    private List<ApiContract> apiContracts = new ArrayList<>();

    public void addValidator(ApiDefinitionValidator apiDefinitionValidator) {
        if (!apiDefinitionValidator.support(this)) {
            throw new ApicatSystemException("Provided apiDefinitionValidator doesn't support this specification. Validator object: " + apiDefinitionValidator);
        }
        if (validators == null) {
            initValidators();
        }
        if (!isValidatorAlreadyAdded(apiDefinitionValidator)) {
            validators.add(apiDefinitionValidator);
        }

        validationResult = Optional.empty();
        contractsValidationResults = Optional.empty();
    }

    private boolean isValidatorAlreadyAdded(ApiDefinitionValidator apiDefinitionValidator) {
        for (ApiDefinitionValidator validator : validators) {
            if (validator.getClass().equals(apiDefinitionValidator.getClass())) {
                return true;
            }
        }
        return false;
    }

    public void addContract(ApiContract apiContract) {
        apiContracts.add(apiContract);
        apiContract.setApiDefinition(this); //TODO verify - the relation is bidirectional because ApiContract contains apiDefinition property
        contractsValidationResults = Optional.empty();
    }

    public Optional<ValidationResult> validate() {
        log.info("About to validate ApiDefinition: " + this);
        if (validators == null) {
            initValidators();
        }

        validationResult = Optional.of(new ValidationResult());
        for (ApiDefinitionValidator apiDefinitionValidator : validators) {
            validationResult.get().merge(apiDefinitionValidator.validate(this));
        }

        return validationResult;
    }

    public ValidationResult validateAgainstApiSpecifications(ApiSpecification... apiSpecifications) {
        ValidationResult result = new ValidationResult();

        for (ApiSpecification apiSpecification : apiSpecifications) {
            ApiContract temporaryContract = new ApiContract();
            temporaryContract.setApiDefinition(this);
            temporaryContract.setApiSpecification(apiSpecification);
            temporaryContract.validate().ifPresent(result::merge);
        }
        return result;
    }

    public Optional<ValidationResult> validateAllContracts() {
        contractsValidationResults = Optional.of(new ValidationResult());
        for (ApiContract apiContract : apiContracts) {
            Optional<ValidationResult> contractValidationResult = apiContract.validate();
            contractValidationResult.ifPresent(validationResult -> contractsValidationResults.get().merge(validationResult));
        }

        return contractsValidationResults;
    }

    public boolean isApiValidated() {
        return validationResult.isPresent();
    }

    public boolean isValid() {
        if (validationResult.isPresent()) {
            return validationResult.get().getProblemList().isEmpty();
        }
        throw new ApicatSystemException(ErrorCode.API_NOT_VALIDATED);
    }

    public boolean areContractsValided() {
        return contractsValidationResults.isPresent();
    }

    public boolean areContractsValid() {
        if (contractsValidationResults.isPresent()) {
            return contractsValidationResults.get().getProblemList().isEmpty();
        }
        throw new ApicatSystemException(ErrorCode.API_NOT_VALIDATED);
    }

    private void initValidators() {
        log.info("ApiDefinition - about to init validators. autodiscover validators: " + autodiscoverValidators);
        validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiDefinitionValidator.class).forEach(apiDefinitionValidator -> {
                if (apiDefinitionValidator.support(this)) {
                    log.info("Adding definition validator: " + apiDefinitionValidator);
                    validators.add(apiDefinitionValidator);
                    validationResult = Optional.empty();
                }
            });
        }
    }

    public void addContractValidator(ApiContractValidator apiContractValidator) {
        for (ApiContract contract : apiContracts) {
            contract.setAutodiscoverValidators(autodiscoverValidators);
            contract.addValidator(apiContractValidator);
        }
    }

    //public ValidationResult validateSpecifications(ApiSpecification apiSpecification);

}
