package pl.jcommerce.apicat.contract;


import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 *
 *
 * @author Daniel Charczyński
 */
public class ApiContract {

    /**
     * ApiContract validators
     */
    private List<ApiContractValidator> validators;

    /**
     * Contract target Api
     */
    @Getter @Setter
    private ApiDefinition apiDefinition;


    /**
     * Contract specification
     */
    @Getter @Setter
    private ApiSpecification apiSpecification;


    /**
     * Information if validators should be loaded automatically
     */
    @Getter @Setter
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
            throw new RuntimeException("Provided apiContractValidator doesn't support this contract");
        if (validators == null)
            validators = initValidators();
        validators.add(apiContractValidator);
        valid = Optional.empty();
    }

    /**
     * Validate contract
     */
    public void validate() {
        if (!apiSpecification.getValid().isPresent())
            apiSpecification.validate();

        //TODO: refactor after ApiDefinition refactoring
        if (!apiDefinition.isValid())
            apiDefinition.validate();

        if (validators == null)
            validators = initValidators();
        validators.forEach(apiContractValidator -> apiContractValidator.validate(this));
        valid = Optional.of(true);
    }

    /**
     * Init validators
     *
     * @return validators
     */
    private List<ApiContractValidator> initValidators() {
        List<ApiContractValidator> validators = new ArrayList<>();
        if (autodiscoverValidators) {
            ServiceLoader.load(ApiContractValidator.class).forEach(apiContractValidator -> {
                if (apiContractValidator.support(this))
                    addValidator(apiContractValidator);
            });
        }
        return validators;
    }

}



