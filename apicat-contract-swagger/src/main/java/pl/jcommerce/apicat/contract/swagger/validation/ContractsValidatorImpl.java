package pl.jcommerce.apicat.contract.swagger.validation;

import com.google.common.collect.Lists;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import java.util.*;

/**
 * Created by krka on 14.10.2016.
 */
public class ContractsValidatorImpl implements ContractsValidator {

    private List<ContractAnalyzer> contractAnalyzers = new LinkedList<>();

    @Override
    public Contract validateContract(String customerContractLocation, String providerContractLocation) {
        Swagger customerSwagger = new SwaggerParser().read(customerContractLocation);
        Swagger providerSwagger = new SwaggerParser().read(providerContractLocation);
        return validate(customerSwagger, providerSwagger);
    }

    @Override
    public Contract validateContract(Swagger customerSwagger, Swagger providerSwagger) {
        return validate(customerSwagger, providerSwagger);
    }

    private Contract validate(Swagger customerSwagger, Swagger providerSwagger) {
        Contract contract = new Contract();
        if(customerSwagger == null) {
            contract.setConsumerSwaggerDefinitionConsistant(false);
            contract.getDiffDetails().addDifference(MessageConstants.INCONSISTENT_CONSUMER_CONTRACT);
        }
        if(providerSwagger == null) {
            contract.setProviderSwaggerDefinitionConsistant(false);
            contract.getDiffDetails().addDifference(MessageConstants.INCONSISTENT_PROVIDER_CONTRACT);
        }
        if(customerSwagger == null || providerSwagger == null)
            return contract;
        else {
            contract.setCustomerSwaggerDefinition(customerSwagger);
            contract.setProviderSwaggerDefinition(providerSwagger);
            doValidate(contract);
        }
        return contract;
    }

    private void doValidate (Contract contractToBeValidated) {
        // going through analyzers
        contractAnalyzers.addAll(Lists.newArrayList(new MetadataAnalyzer(), new SwaggerEndpointApiContractValidator(), new DefinitionAnalyzer()));
        contractAnalyzers.forEach(c -> c.analyzeContract(contractToBeValidated));
    }
}
