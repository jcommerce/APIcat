package pl.jcommerce.apicat.contract.swagger;

import org.junit.Test;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by krka on 28.10.2016.
 */
public class SwaggerApiTest {

    @Test
    public void shouldValidateYamlDefinitionPass() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).build();
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());
    }

    @Test
    public void shouldValidateJsonDefinitionPass() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath("contracts/json/providerContract.json").withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).build();
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());
    }

    @Test
    public void shouldValidateDefinitionWithErrorsPass() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath("contracts/json/testErrorContract.json").withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).build();
        apiDefinition.validate();
        assertFalse(apiDefinition.isValid());
        assertEquals(2, apiDefinition.getValidationResult().getProblemList().size());
    }

    @Test
    public void shouldValidateSpecificationWithErrorsPass() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath("contracts/json/testErrorContract.json");
        apiSpecification.addValidator(new SwaggerApiSpecificationValidator());
        apiSpecification.validate();
        assertFalse(apiSpecification.isValid());
    }
    @Test(expected = ApicatSystemException.class)
    public void shouldValidateDefinitionFailNotSupportedValidator() {
        SwaggerApiDefinition stubApiDefinition = SwaggerApiDefinition.empty();
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromStub(stubApiDefinition).withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).build();
        apiDefinition.validate();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldApiDefinitionValidationCheckFail() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).build();
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        apiDefinition.addValidator(new SwaggerApiDefinitionValidator());
        apiDefinition.isValid();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldApiSpecificationValidationCheckFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        apiSpecification.validate();
        assertTrue(apiSpecification.isValid());

        apiSpecification.addValidator(new SwaggerApiSpecificationValidator());
        apiSpecification.isValid();
    }

    @Test
    public void shouldValidatePass() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                //withApiContractValidator(new SwaggerApiContractValidator()).
                        withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).
                        build();
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());
    }

    @Test
    public void shouldValidateFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                //withApiContractValidator(new SwaggerApiContractValidator()).withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).
                        build();
        apiDefinition.validate();
        assertFalse(apiDefinition.isValid());
    }

    @Test
    public void shouldValidateAllContractsWithoutAutodiscoveryValidatorsPass() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                //withApiContractValidator(new SwaggerApiContractValidator()).
                        build();
        apiDefinition.validateAllContracts();
        assertTrue(apiDefinition.isValid());
    }

    @Test
    public void shouldValidateAllContractsWithAutodiscoveryValidatorsPass() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withContractedApiSpecification(apiSpecification).
                //withApiContractValidator(new SwaggerApiContractValidator()).
                        build();
        apiDefinition.validateAllContracts();
        boolean isContractValid = apiDefinition.isValid();
        assertTrue(isContractValid);
    }

    @Test
    public void shouldValidateAllContractsWithoutAutodiscoveryValidatorsFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                //withApiContractValidator(new SwaggerApiContractValidator()).
                        build();
        apiDefinition.validateAllContracts();
        assertFalse(apiDefinition.areContractsValid());
    }

    @Test
    public void shouldValidateAllContractsWithAutodiscoveryValidatorsFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withContractedApiSpecification(apiSpecification).build();
        apiDefinition.validateAllContracts();
        assertFalse(apiDefinition.areContractsValid());
    }

    @Test
    public void shouldValidateSpecificationsPass() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).
                //withApiContractValidator(new SwaggerApiContractValidator()).
                        build();
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        apiDefinition.validateAgainstApiSpecifications(apiSpecification);
        assertTrue(apiDefinition.isValid());
    }

    @Test
    public void shouldValidateSpecificationsFail() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).
                //withApiContractValidator(new SwaggerApiContractValidator()).
                        build();
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        apiDefinition.validateAgainstApiSpecifications(apiSpecification);
        assertFalse(apiDefinition.isValid());
    }

    @Test(expected = SwaggerOpenAPISpecificationException.class)
    public void shouldSwaggerOpenAPISpecificationExceptionBeDetected() {
        SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("inconsistentConsumerContract.yaml"));
    }

    private String localizeSwaggerDefinitions(String consumerContractLocation) {
        return "contracts/yaml/" + consumerContractLocation;
    }
}
