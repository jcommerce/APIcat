package pl.jcommerce.apicat.contract.swagger.validation;

import com.google.common.collect.Lists;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.models.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.swagger.apicontract.SwaggerApiContract;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.validation.problem.ProblemLevel;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.contract.validation.result.ValidationResultCategory;

import java.text.MessageFormat;

import static org.junit.Assert.*;

@Slf4j
public class ContractsValidationTest {

    @Test
    public void shouldContractsBeValid() {
        ApiContract contract = validateContracts("contracts/yaml/sameConsumerContract.yaml",
                "contracts/yaml/providerContract.yaml");
        assertTrue(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.CORRECT, validationResult.getValidationResultCategory());
        assertTrue(validationResult.getProblemList().isEmpty());
    }

    @Test
    public void shouldContractsInconsistentWithSwaggerSpecificationBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/inconsistentConsumerContract.yaml",
                "contracts/yaml/inconsistentProviderContract.yaml");
        assertFalse(contract.isValid());
        assertFalse(contract.getApiDefinition().isValid());
        assertFalse(contract.getApiSpecification().isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.ERROR, validationResult.getValidationResultCategory());
        assertEquals(2, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageConstants.INCONSISTENT_PROVIDER_CONTRACT + ": object has missing required properties ([\"swagger\"])",
                        ProblemLevel.ERROR)));
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageConstants.INCONSISTENT_CONSUMER_CONTRACT + ": object has missing required properties ([\"swagger\"])",
                        ProblemLevel.ERROR)));
    }

    @Test
    public void shouldUnusedEndpointBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/consumerContractWithoutEndpoint.yaml",
                "contracts/yaml/providerContract.yaml");
        assertFalse(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.WARN, validationResult.getValidationResultCategory());
        assertEquals(1, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.ENDPOINT_NOT_USED, "/pets/findByStatus"),
                        ProblemLevel.WARN)));
    }

    @Test
    public void shouldUnusedPutMethodBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/consumerContractWithoutPutMethod.yaml",
                "contracts/yaml/providerContract.yaml");
        assertFalse(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.WARN, validationResult.getValidationResultCategory());
        assertEquals(1, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.OPERATION_NOT_USED, HttpMethod.PUT, "updatePet", "/pets"),
                        ProblemLevel.WARN)));
    }

    @Test
    public void shouldWrongInformationAboutProviderBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/consumerContractWithWrongMetadata.yaml",
                "contracts/yaml/providerContract.yaml");
        assertFalse(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.ERROR, validationResult.getValidationResultCategory());
        assertEquals(3, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.WRONG_HOST_ADDRESS, "petstore.swagger.eu", "petstore.swagger.io"),
                        ProblemLevel.ERROR)));
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.WRONG_HOST_PATH, "/v1", "/v2"),
                        ProblemLevel.ERROR)));
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.WRONG_HOST_SCHEMES, Lists.newArrayList(SwaggerDefinition.Scheme.HTTPS), Lists.newArrayList(SwaggerDefinition.Scheme.HTTP)),
                        ProblemLevel.ERROR)));
    }

    @Test
    public void shouldUnusedRequiredParameterBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/consumerContractWithoutRequiredParameter.yaml",
                "contracts/yaml/providerContract.yaml");
        assertFalse(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.ERROR, validationResult.getValidationResultCategory());
        assertEquals(1, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageConstants.INCONSISTENT_CONSUMER_CONTRACT + ": instance type (null) does not match any allowed primitive type (allowed: [\"array\"])",
                        ProblemLevel.ERROR)));

    }

    @Test
    public void shouldUnusedNotRequiredParameterBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/consumerContractWithoutNotRequiredParameter.yaml",
                "contracts/yaml/providerContract.yaml");
        assertFalse(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.WARN, validationResult.getValidationResultCategory());
        assertEquals(1, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.PARAMETER_NOT_USED, "password", HttpMethod.GET, "loginUser", "/users/login"),
                        ProblemLevel.WARN)));
    }

    @Test
    public void shouldUnusedResponseBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/consumerContractWithoutResponse.yaml",
                "contracts/yaml/providerContract.yaml");
        assertFalse(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.ERROR, validationResult.getValidationResultCategory());
        assertEquals(2, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.RESPONSE_NOT_USED, "400", "Invalid ID supplied", HttpMethod.PUT, "updatePet", "/pets"),
                        ProblemLevel.ERROR)));
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.RESPONSE_NOT_USED, "404", "Pet not found", HttpMethod.PUT, "updatePet", "/pets"),
                        ProblemLevel.ERROR)));
    }

    @Test
    public void shouldUnusedDefinitionBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/consumerContractWithoutDefinition.yaml",
                "contracts/yaml/providerContract.yaml");
        assertFalse(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.ERROR, validationResult.getValidationResultCategory());
        assertEquals(1, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.DEFINITION_NOT_USED, "User"),
                        ProblemLevel.ERROR)));
    }

    @Test
    public void shouldUnusedNotRequiredPropertiesBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/consumerContractWithoutNotRequiredDefinitionProperties.yaml",
                "contracts/yaml/providerContract.yaml");
        assertFalse(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.WARN, validationResult.getValidationResultCategory());
        assertEquals(1, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "status", "Pet"),
                        ProblemLevel.WARN)));
    }

    @Test
    public void shouldUnusedRequiredPropertiesBeDetected() {
        ApiContract contract = validateContracts("contracts/yaml/consumerContractWithoutRequiredDefinitionProperties.yaml",
                "contracts/yaml/providerContract.yaml");
        assertFalse(contract.isValid());

        ValidationResult validationResult = getResults(contract);
        assertEquals(ValidationResultCategory.ERROR, validationResult.getValidationResultCategory());
        assertEquals(1, validationResult.getProblemList().size());
        assertTrue(validationResult.getProblemList().contains(
                new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "photoUrls", "Pet"),
                        ProblemLevel.ERROR)));
    }

    private ApiContract validateContracts(String consumerContractLocation, String providerContractLocation) {
        SwaggerApiContract apiContract = new SwaggerApiContract(SwaggerApiDefinition.fromPath(providerContractLocation), SwaggerApiSpecification.fromPath(consumerContractLocation));
        apiContract.validate();
        return apiContract;
    }

    private ValidationResult getResults(ApiContract contract) {
        if (contract.getValidationResult().isPresent()) {
            return contract.getValidationResult().get();
        }
        return new ValidationResult();
    }
}
