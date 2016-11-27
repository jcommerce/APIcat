package pl.jcommerce.apicat.contract.swagger;

import org.junit.Test;
import pl.jcommerce.apicat.contract.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by krka on 28.10.2016.
 */
public class SwaggerApiTest {


    @Test
    public void shouldValidateDefinitionPass() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).build();
        apiDefinition.validateDefinition();
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(isContractValid);
    }

    @Test
    public void shouldValidateDefinitionFail() {
        SwaggerApiDefinition stubApiDefinition = SwaggerApiDefinition.empty();
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromStub(stubApiDefinition).withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).build();
        apiDefinition.validateDefinition();
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(!isContractValid);
    }

    @Test
    public void shouldValidatePass() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                withApiContractValidator(new SwaggerApiContractValidator()).withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).build();
        apiDefinition.validate();
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(isContractValid);
    }

    @Test
    public void shouldValidateFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                withApiContractValidator(new SwaggerApiContractValidator()).withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).build();
        apiDefinition.validate();
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(!isContractValid);
    }

    @Test
    public void shouldValidateAllContractsWithoutAutodiscoveryValidatorsPass() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                withApiContractValidator(new SwaggerApiContractValidator()).build();
        apiDefinition.validateAllContracts();
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(isContractValid);
    }

    @Test
    public void shouldValidateAllContractsWithAutodiscoveryValidatorsPass() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withContractedApiSpecification(apiSpecification).
                withApiContractValidator(new SwaggerApiContractValidator()).build();
        apiDefinition.validateAllContracts();
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(isContractValid);
    }

    @Test
    public void shouldValidateAllContractsWithoutAutodiscoveryValidatorsFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                withApiContractValidator(new SwaggerApiContractValidator()).build();
        apiDefinition.validateAllContracts();
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(!isContractValid);
    }

    @Test
    public void shouldValidateAllContractsWithAutodiscoveryValidatorsFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withContractedApiSpecification(apiSpecification).build();
        apiDefinition.validateAllContracts();
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(!isContractValid);
    }

    @Test
    public void shouldValidateSpecificationsPass() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withApiContractValidator(new SwaggerApiContractValidator()).build();
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        apiDefinition.validateSpecifications(apiDefinition, apiSpecification);
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(isContractValid);
    }

    @Test
    public void shouldValidateSpecificationsFail() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withApiContractValidator(new SwaggerApiContractValidator()).build();
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        apiDefinition.validateSpecifications(apiDefinition, apiSpecification);
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(!isContractValid);
    }

    @Test(expected = SwaggerOpenAPISpecificationException.class)
    public void shouldSwaggerOpenAPISpecificationExceptionBeDetected() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("inconsistentConsumerContract.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                withApiContractValidator(new SwaggerApiContractValidator()).build();
    }

    private String localizeSwaggerDefinitions(String consumerContractLocation) {
            return TestUtils.getTestConstractsPath() + "/yaml/" + consumerContractLocation;


    }

}
