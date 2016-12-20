package pl.jcommerce.apicat.contract;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * Connects ApiSpecification delivered by customer with ApiDefinition used by it.
 *
 * @author Daniel Charczyński
 */
public class ApiContract {

    private final Logger logger = LoggerFactory.getLogger(ApiContract.class);

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

    /**
     * Information if ApiContract is valid
     * This flag is set after successful validation
     */
    @Getter
    private Optional<Boolean> valid = Optional.empty();

    /**
     * Add {@code apiContractValidator}
     *
     * @param apiContractValidator
     */
    public void addValidator(ApiContractValidator apiContractValidator) {
        if (!apiContractValidator.support(this))
            throw new ApicatSystemException("Provided apiContractValidator doesn't support this contract");
        if (validators == null)
            validators = initValidators();
        validators.add(apiContractValidator);
        valid = Optional.empty();
    }

    /**
     * Validate contract
     */
    public void validate() {
        logger.info("About to validate ApiContract: " + this);
        if (!apiSpecification.isValidated()) {
            apiSpecification.validate();
        }

        //TODO: refactor after ApiDefinition refactoring
        if (!apiDefinition.isApiValidated()) {
            apiDefinition.validate();
        }

        if (validators == null) {
            validators = initValidators();
        }

        validators.forEach(apiContractValidator -> apiContractValidator.validate(this));
        valid = Optional.of(true);
    }

    /**
     * Init validators
     *
     * @return validators
     */
    private List<ApiContractValidator> initValidators() {
        logger.info("ApiContract - about to init validators. autodiscover validators: " + autodiscoverValidators);
        List<ApiContractValidator> validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiContractValidator.class).forEach(apiContractValidator -> {
                if (apiContractValidator.support(this)) {
                    logger.info("Adding contract validator: " + apiContractValidator);
                    //addValidator(apiContractValidator); TODO verify - stack overflow (addValidator invokes initValidators, so initValidators cannot invoke addValidator)
                    validators.add(apiContractValidator);
                    valid = Optional.empty();
                }
            });
        }

        return validators;
    }

}
