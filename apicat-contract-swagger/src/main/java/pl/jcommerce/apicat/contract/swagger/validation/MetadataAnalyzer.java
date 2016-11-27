package pl.jcommerce.apicat.contract.swagger.validation;

import io.swagger.models.Scheme;

import java.util.List;

/**
 * Created by krka on 23.10.2016.
 */
class MetadataAnalyzer implements ContractAnalyzer {

    @Override
    public void analyzeContract(Contract contract) {
        String customerHost = contract.getCustomerSwaggerDefinition().getHost();
        String providerHost = contract.getProviderSwaggerDefinition().getHost();
        if(!customerHost.equals(providerHost)) {
            setContractToNotEqual(contract);
            setContractToInvalid(contract);
            addContractDifference(contract, MessageConstants.WRONG_HOST_ADDRESS, customerHost, providerHost);
        }

        List<Scheme> customerSchemes = contract.getCustomerSwaggerDefinition().getSchemes();
        List<Scheme> providerSchemes = contract.getProviderSwaggerDefinition().getSchemes();
        if(!customerSchemes.equals(providerSchemes)) {
            setContractToNotEqual(contract);
            setContractToInvalid(contract);
            addContractDifference(contract, MessageConstants.WRONG_HOST_SCHEMES, customerSchemes, providerSchemes);
        }

        String customerPath = contract.getCustomerSwaggerDefinition().getBasePath();
        String providerPath = contract.getProviderSwaggerDefinition().getBasePath();
        if(!customerPath.equals(providerPath)) {
            setContractToNotEqual(contract);
            setContractToInvalid(contract);
            addContractDifference(contract, MessageConstants.WRONG_HOST_PATH, customerPath, providerPath);
        }
    }
}
