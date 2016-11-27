package pl.jcommerce.apicat.contract;


import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.ApiDefinitionValidator;

import java.util.List;
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

    protected String name;

    @Getter @Setter
    protected Boolean autodiscoveryValidators = Boolean.TRUE;


    protected List<ApiContractValidator> apiContractValidators = Lists.newArrayList();

    protected List<ApiDefinitionValidator> apiDefinitionValidators = Lists.newArrayList();

    protected List<ApiContract> apiContracts = Lists.newArrayList();

    public void addContractValidator(ApiContractValidator apiContractValidator) {
        apiContractValidators.add(apiContractValidator);
    }

    public void addDefinitionValidator(ApiDefinitionValidator apiDefinitionValidator) {
        apiDefinitionValidators.add(apiDefinitionValidator);
    }

    public void addContract(ApiContract apiContract) {
        apiContracts.add(apiContract);
    }

    public void loadValidators() {
        if (autodiscoveryValidators) {
            ServiceLoader.load(ApiContractValidator.class).forEach(this::addContractValidator);
            ServiceLoader.load(ApiDefinitionValidator.class).forEach(this::addDefinitionValidator);
        }
    }

    public abstract boolean isValid();

    public abstract void validate();

    public abstract void validateAllContracts();

    public abstract void validateDefinition();

    public abstract void validateSpecifications(ApiDefinition apiDefinition, ApiSpecification apiSpecification);

}

