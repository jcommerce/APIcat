package pl.jcommerce.apicat.contract;


import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;

import java.util.ArrayList;
import java.util.List;
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

    @Getter
    private boolean valid = false;


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
    }

    /**
     * Validate contract
     */
    public void validate() {
        if (validators == null)
            validators = initValidators();
        validators.forEach(apiContractValidator -> apiContractValidator.validate(this));
        valid = true;
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



