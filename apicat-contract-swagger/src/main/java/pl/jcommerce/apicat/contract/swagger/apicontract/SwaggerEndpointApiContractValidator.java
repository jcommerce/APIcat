package pl.jcommerce.apicat.contract.swagger.apicontract;

import com.google.auto.service.AutoService;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.parameters.Parameter;
import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.swagger.validation.MessageConstants;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.problem.ProblemLevel;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AutoService(ApiContractValidator.class)
public class SwaggerEndpointApiContractValidator extends SwaggerApiContractValidator {

    private Map<String, Path> providerPaths;
    private Map<String, Path> customerPaths;
    private List<Operation> customerOperations;
    private OperationDetails operationDetails;
    private ValidationResult validationResult = new ValidationResult();

    @Override
    public ValidationResult validate(ApiDefinition apiDefinition, ApiSpecification apiSpecification) {
        validationResult = new ValidationResult();

        providerPaths = ((SwaggerApiDefinition) apiDefinition).getSwaggerDefinition().getPaths();
        customerPaths = ((SwaggerApiSpecification) apiSpecification).getSwaggerDefinition().getPaths();
        checkEndpointsExistence();
        if (!customerOperations.isEmpty()) {
            createErrorsWithNotExistingOperations();
        }

        return validationResult;
    }

    private void checkEndpointsExistence() {
        createCustomerOperations(customerPaths);
        providerPaths.forEach(this::checkSingleEndpointExistence);
    }

    private void createCustomerOperations(Map<String, Path> customerPaths) {
        customerOperations = new ArrayList<>();
        customerPaths.forEach((k, v) ->
                customerOperations.addAll(v.getOperations()));
    }

    private void checkSingleEndpointExistence(String endpoint, Path providerPath) {
        boolean containKey = customerPaths.containsKey(endpoint);
        if (!containKey) {
            validationResult.addProblem(
                    new ValidationProblem(
                            MessageFormat.format(
                                    MessageConstants.ENDPOINT_NOT_USED, endpoint),
                            ProblemLevel.WARN));
        } else {
            for (Operation operation : providerPath.getOperations()) {
                checkSingleOperationExistence(endpoint, providerPath, findOperation(customerOperations, operation.getOperationId()), operation);
            }
        }
    }

    private Optional<Operation> findOperation(List<Operation> customerOperations, String operationId) {
        for (Operation customerOperation : new ArrayList<>(customerOperations)) {
            if (customerOperation.getOperationId().equals(operationId)) {
                customerOperations.remove(customerOperation);
                return Optional.of(customerOperation);
            }
        }

        return Optional.empty();
    }

    private void checkSingleOperationExistence(String endpoint, Path providerPath, Optional<Operation> foundOperation, Operation providerOperation) {
        createOperationDetails(endpoint, providerPath, providerOperation);
        if (foundOperation.isPresent()) {
            providerOperation.getParameters().forEach(
                    p -> checkSingleParametersExistence(p, foundOperation.get().getParameters()));
            providerOperation.getResponses().forEach(
                    (s, response) -> checkResponsesExistence(s, response, foundOperation.get().getResponses()));
        } else {
            validationResult.addProblem(
                    new ValidationProblem(
                            MessageFormat.format(
                                    MessageConstants.OPERATION_NOT_USED,
                                    operationDetails.getMethodName(),
                                    operationDetails.getOperationId(),
                                    operationDetails.getEndpoint()),
                            ProblemLevel.WARN));
        }
    }

    private void createOperationDetails(String endpoint, Path providerPath, Operation providerOperation) {
        operationDetails = new OperationDetails();
        operationDetails.setEndpoint(endpoint);
        operationDetails.setMethodName(providerPath.getOperationMap().entrySet().stream().
                filter(e -> e.getValue().equals(providerOperation)).
                map(Map.Entry::getKey).findAny().get());
        operationDetails.setOperationId(providerOperation.getOperationId());
    }

    private void checkSingleParametersExistence(Parameter providerParameter, List<Parameter> consumerParameters) {
        if (consumerParameters.stream().noneMatch(p -> p.equals(providerParameter))) {
            validationResult.addProblem(
                    new ValidationProblem(
                            MessageFormat.format(
                                    MessageConstants.PARAMETER_NOT_USED,
                                    providerParameter.getName(),
                                    operationDetails.getMethodName(),
                                    operationDetails.getOperationId(),
                                    operationDetails.getEndpoint()),
                            ProblemLevel.WARN));
        }
    }

    private void checkResponsesExistence(String responseCode, Response response, Map<String, Response> responses) {
        if (!responses.containsKey(responseCode)) {
            validationResult.addProblem(
                    new ValidationProblem(
                            MessageFormat.format(
                                    MessageConstants.RESPONSE_NOT_USED,
                                    responseCode,
                                    response.getDescription(),
                                    operationDetails.getMethodName(),
                                    operationDetails.getOperationId(),
                                    operationDetails.getEndpoint()),
                            ProblemLevel.ERROR));
        }
    }

    private void createErrorsWithNotExistingOperations() {
        for (Operation customerOperation : customerOperations) {
            validationResult.addProblem(
                    new ValidationProblem(
                            MessageFormat.format(
                                    MessageConstants.OPERATION_NOT_EXISTS,
                                    customerOperation.getOperationId()),
                            ProblemLevel.ERROR));
        }
    }

    @Getter
    @Setter
    private class OperationDetails {
        private String endpoint;
        private HttpMethod methodName;
        private String operationId;
    }
}
