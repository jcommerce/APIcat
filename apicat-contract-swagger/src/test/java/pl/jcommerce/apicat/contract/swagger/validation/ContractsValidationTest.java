package pl.jcommerce.apicat.contract.swagger.validation;

import com.google.common.collect.Lists;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.models.HttpMethod;
import org.junit.After;
import org.junit.Test;
import pl.jcommerce.apicat.contract.swagger.TestUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by krka on 28.10.2016.
 */
public class ContractsValidationTest {

    private List<String> differences;
    private Contract contract;

    @After
    public void cleanUp() {
        if (differences != null)
            System.out.println(Arrays.toString(differences.toArray()));
    }

    @Test
    public void shouldConsumerAndProviderContractsBeEqual() {
        validateContracts("/yaml/sameConsumerContract.yaml", "/yaml/providerContract.yaml");
        assertTrue(contract.isValid());
        assertTrue(contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.isEmpty());
    }

    @Test
    public void shouldProviderContractInconsistentWithSwaggerSpecificationBeDetected() {
        validateContracts("/yaml/sameConsumerContract.yaml", "/yaml/inconsistentProviderContract.yaml");
        assertTrue(!contract.isProviderSwaggerDefinitionConsistant());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.INCONSISTENT_PROVIDER_CONTRACT, "")));
    }

    @Test
    public void shouldConsumerContractInconsistentWithSwaggerSpecificationBeDetecte() {
        validateContracts("/yaml/inconsistentConsumerContract.yaml", "/yaml/providerContract.yaml");
        assertTrue(!contract.isConsumerSwaggerDefinitionConsistant());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.INCONSISTENT_CONSUMER_CONTRACT, "")));
    }

    @Test
    public void shouldUnusedEndpointBeDetected() {
        validateContracts("/yaml/consumerContractWithoutEndpoint.yaml", "/yaml/providerContract.yaml");
        assertTrue(contract.isValid());
        assertTrue(!contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.ENDPOINT_NOT_USED, "/pets/findByStatus")));
    }

    @Test
    public void shouldUnusedPutMethodBeDetected() {
        validateContracts("/yaml/consumerContractWithoutPutMethod.yaml", "/yaml/providerContract.yaml");
        assertTrue(contract.isValid());
        assertTrue(!contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.OPERATION_NOT_USED, HttpMethod.PUT, "updatePet", "/pets")));
    }

    @Test
    public void shouldWrongInformationAboutProviderBeDetected() {
        validateContracts("/yaml/consumerContractWithWrongMetadata.yaml", "/yaml/providerContract.yaml");
        assertTrue(!contract.isValid());
        assertTrue(!contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.WRONG_HOST_ADDRESS, "petstore.swagger.eu", "petstore.swagger.io")));
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.WRONG_HOST_PATH, "/v1", "/v2")));
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.WRONG_HOST_SCHEMES, Lists.newArrayList(SwaggerDefinition.Scheme.HTTPS), Lists.newArrayList(SwaggerDefinition.Scheme.HTTP))));
    }

    @Test
    public void shouldUnusedNotRequiredParameterBeDetected() {
        validateContracts("/yaml/consumerContractWithoutNotRequiredParameter.yaml", "/yaml/providerContract.yaml");
        assertTrue(contract.isValid());
        assertTrue(!contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.PARAMETER_NOT_USED, "password", HttpMethod.GET, "loginUser", "/users/login")));
    }

    @Test
    public void shouldUnusedRequiredParameterBeDetected() {
        validateContracts("/yaml/consumerContractWithoutRequiredParameter.yaml", "/yaml/providerContract.yaml");
        assertTrue(!contract.isValid());
        assertTrue(!contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.PARAMETER_NOT_USED, "petId", HttpMethod.GET, "getPetById", "/pets/{petId}")));
    }

    @Test
    public void shouldUnusedResponseBeDetected() {
        validateContracts("/yaml/consumerContractWithoutResponse.yaml", "/yaml/providerContract.yaml");
        assertTrue(!contract.isValid());
        assertTrue(!contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.RESPONSE_NOT_USED, "400", "Invalid ID supplied", HttpMethod.PUT, "updatePet", "/pets")));
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.RESPONSE_NOT_USED, "404", "Pet not found", HttpMethod.PUT, "updatePet", "/pets")));
    }

    @Test
    public void shouldUnusedDefinitionBeDetected() {
        validateContracts("/yaml/consumerContractWithoutDefinition.yaml", "/yaml/providerContract.yaml");
        assertTrue(!contract.isValid());
        assertTrue(!contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.DEFINITION_NOT_USED, "User")));
    }

    @Test
    public void shouldUnusedNotRequiredPropertiesBeDetected() {
        validateContracts("/yaml/consumerContractWithoutNotRequiredDefinitionProperties.yaml", "/yaml/providerContract.yaml");
        assertTrue(contract.isValid());
        assertTrue(!contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "status", "Pet")));
    }

    @Test
    public void shouldUnusedRequiredPropertiesBeDetected() {
        validateContracts("/yaml/consumerContractWithoutRequiredDefinitionProperties.yaml", "/yaml/providerContract.yaml");
        assertTrue(!contract.isValid());
        assertTrue(!contract.isSwaggerDefinitionsEqual());
        assertTrue(differences.contains(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "photoUrls", "Pet")));
    }

    private void validateContracts(String consumerContractLocation, String providerContractLocation) {
        String consumerSwaggerLocation = TestUtils.getTestConstractsPath() + consumerContractLocation;
        String providerSwaggerLocation = TestUtils.getTestConstractsPath() + providerContractLocation;
        ContractsValidator contractsValidator = new ContractsValidatorImpl();
        contract = contractsValidator.validateContract(consumerSwaggerLocation, providerSwaggerLocation);
        differences = contract.getDiffDetails().getDifferences();
    }
}
