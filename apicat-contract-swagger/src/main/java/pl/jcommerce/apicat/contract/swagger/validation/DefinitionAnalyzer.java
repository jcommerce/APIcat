package pl.jcommerce.apicat.contract.swagger.validation;

import io.swagger.models.Model;
import io.swagger.models.properties.Property;

import java.util.Map;

/**
 * Created by krka on 23.10.2016.
 */
class DefinitionAnalyzer implements ContractAnalyzer {

    private Map<String, Model> customerDefinitions;
    private Map<String, Model> providerDefinitions;
    private Contract contract;

    @Override
    public void analyzeContract(Contract contract) {
        this.contract = contract;
        customerDefinitions = contract.getCustomerSwaggerDefinition().getDefinitions();
        providerDefinitions = contract.getProviderSwaggerDefinition().getDefinitions();
        providerDefinitions.forEach(this::checkDefinitionExistence);

    }

    private void checkDefinitionExistence(String definitionName, Model providerModel) {
        if(!customerDefinitions.containsKey(definitionName)) {
            setContractToNotEqual(contract);
            setContractToInvalid(contract);
            addContractDifference(contract, MessageConstants.DEFINITION_NOT_USED, definitionName);
        } else {
            providerModel.getProperties().forEach((s, property) -> checkPropertyExistence(s, property, definitionName));

        }

    }

    private void checkPropertyExistence(String propertyKey, Property property, String definitionName) {
        if(!customerDefinitions.get(definitionName).getProperties().containsKey(propertyKey)) {
            if(property.getRequired()) {
                setContractToInvalid(contract);
            }
            setContractToNotEqual(contract);
            addContractDifference(contract, MessageConstants.PROPERTY_NOT_USED, propertyKey, definitionName);
        }

    }

}
