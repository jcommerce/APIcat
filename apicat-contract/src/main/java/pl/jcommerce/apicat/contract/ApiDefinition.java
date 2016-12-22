package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
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
    @Setter
    private ValidationResult validationResult = new ValidationResult();
    private boolean apiValidated = false;

    /**
     * Information if all contracts using this definition are valid with it.
     * This flag is set after successful validation and need to be nullified when ApiDefinition changes.
     */
    @Getter
    private Optional<Boolean> contractsAreValid = Optional.empty();
    private List<ApiContract> apiContracts = new ArrayList<>();

    public void addValidator(ApiDefinitionValidator apiDefinitionValidator) {
        if (!apiDefinitionValidator.support(this)) {
            throw new ApicatSystemException("Provided apiDefinitionValidator doesn't support this specification. validator object: " + apiDefinitionValidator);
        }
        if (validators == null) {
            initValidators();
        }
        if (!validatorAlreadyAdded(apiDefinitionValidator)) {
            validators.add(apiDefinitionValidator);
        }

        apiValidated = false;
        contractsAreValid = Optional.empty();
    }

    private boolean validatorAlreadyAdded(ApiDefinitionValidator apiDefinitionValidator) {
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
        contractsAreValid = Optional.empty();
    }

    //TODO after ApiContract refactoring
//    public void validateAgainstApiSpecifications(ApiSpecification... apiSpecifications) {
//        for (ApiSpecification apiSpecification : apiSpecifications) {
//            ApiContract temporaryContract = new ApiContract();
//            temporaryContract.setApiDefinition(this);
//            temporaryContract.setApiSpecification(apiSpecification);
//            temporaryContract.validate();
//        }
//    }

    public ValidationResult validate() {
        log.info("About to validate ApiDefinition: " + this);
        if (validators == null) {
            initValidators();
        }

        for (ApiDefinitionValidator apiDefinitionValidator : validators) {
            validationResult.merge(apiDefinitionValidator.validate(this));
        }

        apiValidated = true;
        return validationResult;
    }

    //TODO after ApiContract refactoring
//    public void validateAllContracts() {
//        boolean contractsValid = true;
//        for (ApiContract apiContract : apiContracts) {
//            apiContract.validate();
//            boolean contractValid = apiContract.getValid().orElse(false);
//            contractsValid = contractValid && contractsValid;
//        }
//        contractsAreValid = Optional.of(contractsValid);
//    }

    public boolean isApiValidated() {
        return apiValidated;
    }

    public boolean areContractsValid() {
        if (contractsAreValid.isPresent()) {
            return contractsAreValid.get();
        }
        throw new IllegalStateException("Api contracts haven't been validated");
    }

    public boolean isValid() {
        if (apiValidated) {
            return validationResult.getProblemList().isEmpty();
        }
        throw new IllegalStateException("Api definition hasn't been validated");
    }

    private void initValidators() {
        log.info("ApiDefinition - about to init validators. autodiscover validators: " + autodiscoverValidators);
        validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiDefinitionValidator.class).forEach(apiDefinitionValidator -> {
                if (apiDefinitionValidator.support(this)) {
                    log.info("Adding definition validator: " + apiDefinitionValidator);
                    validators.add(apiDefinitionValidator);
                    apiValidated = false;
                }
            });
        }
    }

    //public abstract void validateDefinition();

    //public abstract void validateSpecifications(ApiDefinition apiDefinition, ApiSpecification apiSpecification);

}
