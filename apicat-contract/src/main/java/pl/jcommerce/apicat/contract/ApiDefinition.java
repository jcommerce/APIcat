package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.validation.ApiDefinitionValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * Defines an API
 *
 * @author Daniel Charczyński
 */
public abstract class ApiDefinition {

    //TODO: adjust to model
    //TODO: move ApiContractValidator to APIContract
    //TODO: all methods should be implemented

    private final Logger logger = LoggerFactory.getLogger(ApiDefinition.class);

    protected String name;

    @Getter
    @Setter
    protected Boolean autodiscoverValidators = Boolean.TRUE;

    protected List<ApiDefinitionValidator> validators = null; //TODO verify: validators==null means that initValidators method will be run in validate method. In the previous version validators list was always empty.

    protected List<ApiContract> apiContracts = new ArrayList<>();


    /**
     * Information if ApiDefinition is valid
     * This flag is set after successful validation
     */
    @Getter
    private Optional<Boolean> valid = Optional.empty();  //TODO verify - this field has been added for consistency with other ApiSpecification and ApiContract 

    @Getter
    private Optional<Boolean> contractsAreValid = Optional.empty();

    public void addValidator(ApiDefinitionValidator apiDefinitionValidator) {
        if (!apiDefinitionValidator.support(this)) {
            throw new ApicatSystemException("Provided apiDefinitionValidator doesn't support this specification. validator object: " + apiDefinitionValidator);
        }
        if (validators == null) {
            validators = initValidators();
        }
        validators.add(apiDefinitionValidator);
        valid = Optional.empty();
    }

    public void addContract(ApiContract apiContract) {
        apiContracts.add(apiContract);
        apiContract.setApiDefinition(this); //TODO verify - the relation is bidirectional because ApiContract contains apiDefinition property
    }


    public void validateAgainstApiSpecifications(ApiSpecification... apiSpecifications) {
        for (ApiSpecification apiSpecification : apiSpecifications) {
            ApiContract temporaryContract = new ApiContract();
            temporaryContract.setApiDefinition(this);
            temporaryContract.setApiSpecification(apiSpecification);
            temporaryContract.validate();
        }

    }

    public void validate() {
        logger.info("About to validate ApiDefinition: " + this);
        if (validators == null)
            validators = initValidators();
        validators.forEach(apiDefinitionValidator -> apiDefinitionValidator.validate(this));
        valid = Optional.of(true);
    }

    public void validateAllContracts() {
        boolean contractsValid = true;
        for (ApiContract apiContract : apiContracts) {
            apiContract.validate();
            boolean contractValid = apiContract.getValid().orElse(false);
            contractsValid = contractValid && contractsValid;
        }
        contractsAreValid = Optional.of(contractsValid);
    }

    public boolean isValidated() {
        return valid.isPresent();
    }

    public boolean areContractsValid() {
        if (contractsAreValid.isPresent()) {
            return contractsAreValid.get();
        }
        throw new IllegalStateException("Api contracts haven't been validated");
    }

    public boolean isValid() {
        if (valid.isPresent()) {
            return valid.get();
        }
        throw new IllegalStateException("Api definition hasn't been validated");
    }


    private List<ApiDefinitionValidator> initValidators() {
        logger.info("ApiDefinition - about to init validators. autodiscover validators: " + autodiscoverValidators);
        List<ApiDefinitionValidator> validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiDefinitionValidator.class).forEach(apiDefinitionValidator -> {
                if (apiDefinitionValidator.support(this)) {
                    logger.info("Adding definition validator: " + apiDefinitionValidator);
                    //addValidator(apiDefinitionValidator); TODO verify - stack overflow (addValidator invokes initValidators, so initValidators cannot invoke addValidator)
                    validators.add(apiDefinitionValidator);
                    valid = Optional.empty();
                }
            });
        }

        return validators;
    }

    //public abstract void validateDefinition();

    //public abstract void validateSpecifications(ApiDefinition apiDefinition, ApiSpecification apiSpecification);

}
