package pl.jcommerce.apicat.contract.swagger;

import org.junit.Test;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.swagger.apicontract.SwaggerApiContract;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinitionBuilder;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinitionValidator;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecificationValidator;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

import static org.junit.Assert.*;

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
/*        ValidationResult result = apiDefinition.validate();
        assertFalse(apiDefinition.isValid());
        assertEquals(2, result.getProblemList().size());*/
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
        //TODO
        //apiDefinition.validateAllContracts();
        assertTrue(apiDefinition.isValid());
    }

    @Test
    public void shouldValidateAllContractsWithAutodiscoveryValidatorsPass() {
        SwaggerApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        SwaggerApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml"))
                //.withContractedApiSpecification(apiSpecification)
                //.withApiContractValidator(new SwaggerApiContractValidator())
                .build();
        //TODO
        //apiDefinition.validateAllContracts();
        SwaggerApiContract apiContract = new SwaggerApiContract(apiDefinition, apiSpecification);
//        ValidationResult validationResult = apiContract.validate();
//        assertFalse(apiContract.isValid());
//        assertEquals(1, validationResult.getProblemList().size());
    }

    @Test
    public void shouldValidateAllContractsWithoutAutodiscoveryValidatorsFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withoutAutodiscoveryValidators().withContractedApiSpecification(apiSpecification).
                //withApiContractValidator(new SwaggerApiContractValidator()).
                        build();
        //TODO
        //apiDefinition.validateAllContracts();
        assertFalse(apiDefinition.areContractsValid());
    }

    @Test
    public void shouldValidateAllContractsWithAutodiscoveryValidatorsFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).withContractedApiSpecification(apiSpecification).build();
        //TODO
        //apiDefinition.validateAllContracts();
        assertFalse(apiDefinition.areContractsValid());
    }

    @Test
    public void shouldValidateSpecificationsPass() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).
                //withApiContractValidator(new SwaggerApiContractValidator()).
                        build();
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutEndpoint.yaml"));
        //TODO
        //apiDefinition.validateAgainstApiSpecifications(apiSpecification);
        assertTrue(apiDefinition.isValid());
    }

    @Test
    public void shouldValidateSpecificationsFail() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath(localizeSwaggerDefinitions("providerContract.yaml")).
                //withApiContractValidator(new SwaggerApiContractValidator()).
                        build();
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("consumerContractWithoutRequiredParameter.yaml"));
        //TODO
        //apiDefinition.validateAgainstApiSpecifications(apiSpecification);
        assertFalse(apiDefinition.isValid());
    }

    @Test(expected = ApicatSystemException.class)
    public void shouldSwaggerOpenAPISpecificationExceptionBeDetected() {
        SwaggerApiSpecification.fromPath(localizeSwaggerDefinitions("inconsistentConsumerContract.yaml"));
    }

    private String localizeSwaggerDefinitions(String consumerContractLocation) {
        return "contracts/yaml/" + consumerContractLocation;
    }
}
