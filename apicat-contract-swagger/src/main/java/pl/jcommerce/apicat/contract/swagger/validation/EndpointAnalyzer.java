package pl.jcommerce.apicat.contract.swagger.validation;

import io.swagger.models.*;
import io.swagger.models.parameters.Parameter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by krka on 23.10.2016.
 */
class EndpointAnalyzer implements ContractAnalyzer {

    private Contract contract;

    private Map<String, Path> providerPaths;

    private Map<String, Path> customerPaths;

    private List<Operation> customerOperations;

    private OperationDetails operationDetails;

    @Override
    public void analyzeContract(Contract contract) {
        this.contract = contract;
        providerPaths = contract.getProviderSwaggerDefinition().getPaths();
        customerPaths = contract.getCustomerSwaggerDefinition().getPaths();
        checkEndpointsExistence();
    }

    private void checkEndpointsExistence () {
        createCustomerOperations(customerPaths);
        providerPaths.forEach(this::checkSingleEndpointExistence);
    }

    private void createCustomerOperations (Map<String, Path> customerPaths) {
        customerOperations = new ArrayList<>();
        customerPaths.forEach((k, v) ->
                customerOperations.addAll(v.getOperations()));
    }

    private void checkSingleEndpointExistence (String endpoint, Path providerPath) {
        boolean containKey = customerPaths.containsKey(endpoint);
        if (!containKey) {
            setContractToNotEqual(contract);
            addContractDifference(contract, MessageConstants.ENDPOINT_NOT_USED, endpoint);
        } else {
            providerPath.getOperations().forEach(
                    o -> checkSingleOperationExistence(endpoint, providerPath, customerOperations.stream().filter(t -> t.getOperationId().equals(o.getOperationId())).findAny(), o));
        }
    }

    private void checkSingleOperationExistence(String endpoint, Path providerPath, Optional<Operation> foundOperation, Operation providerOperation) {
        createOperationDetails(endpoint, providerPath, providerOperation);
        if(foundOperation.isPresent()) {
            providerOperation.getParameters().forEach(
                    p -> checkSingleParametersExistence(p, foundOperation.get().getParameters()));
            providerOperation.getResponses().forEach((s, response) -> checkResponsesExistence(s, response, foundOperation.get().getResponses()));

        } else {
            setContractToNotEqual(contract);
            addContractDifference(contract, MessageConstants.OPERATION_NOT_USED, operationDetails.getMethodName(), operationDetails.getOperationId(), operationDetails.getEndpoint());
        }
    }


    private void createOperationDetails(String endpoint, Path providerPath, Operation providerOperation) {
        operationDetails = new OperationDetails();
        operationDetails.setEndpoint(endpoint);
        operationDetails.setMethodName(providerPath.getOperationMap().entrySet().stream().filter(e -> e.getValue().equals(providerOperation)).map(Map.Entry::getKey).findAny().get());
        operationDetails.setOperationId(providerOperation.getOperationId());
    }

    private void checkSingleParametersExistence(Parameter providerParameter, List<Parameter> consumerParameters) {
        if(!consumerParameters.stream().filter(p -> p.equals(providerParameter)).findAny().isPresent()) {
            if (providerParameter.getRequired()) {
                setContractToInvalid(contract);
            }
            setContractToNotEqual(contract);
            addContractDifference(contract, MessageConstants.PARAMETER_NOT_USED, providerParameter.getName(), operationDetails.getMethodName(), operationDetails.getOperationId(), operationDetails.getEndpoint());
        }
    }

    private void checkResponsesExistence(String responseCode, Response response, Map<String, Response> responses) {
        if(!responses.containsKey(responseCode)) {
            setContractToNotEqual(contract);
            setContractToInvalid(contract);
            addContractDifference(contract, MessageConstants.RESPONSE_NOT_USED, responseCode, response.getDescription(), operationDetails.getMethodName(), operationDetails.getOperationId(), operationDetails.getEndpoint());
        }
    }


    @Data
    private class OperationDetails {

        private String endpoint;

        private HttpMethod methodName;

        private String operationId;

    }
}
