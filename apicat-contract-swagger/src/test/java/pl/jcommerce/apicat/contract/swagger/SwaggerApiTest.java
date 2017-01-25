package pl.jcommerce.apicat.contract.swagger;

import org.junit.Test;
import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.swagger.apicontract.SwaggerDefinitionApiContractValidator;
import pl.jcommerce.apicat.contract.swagger.apicontract.SwaggerEndpointApiContractValidator;
import pl.jcommerce.apicat.contract.swagger.apicontract.SwaggerMetadataApiContractValidator;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinitionBuilder;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinitionValidator;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecificationValidator;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SwaggerApiTest {

    @Test
    public void shouldValidateDefinitionPass() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.
                fromPath("contracts/json/providerContract.json").
                withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).
                build();
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());
    }

    @Test
    public void shouldValidateSpecificationPass() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath("contracts/yaml/consumerContract.yaml");
        apiSpecification.validate();
        assertTrue(apiSpecification.isValid());
    }

    @Test
    public void shouldValidateDefinitionWithErrors() {
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.
                fromPath("contracts/yaml/inconsistentProviderContract.yaml").
                withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).
                build();
        Optional<ValidationResult> result = apiDefinition.validate();
        assertFalse(apiDefinition.isValid());
        if (result.isPresent()) {
            assertEquals(1, result.get().getProblemList().size());
        } else {
            fail();
        }
    }

    @Test
    public void shouldValidateSpecificationWithErrors() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath("contracts/json/testErrorContract.json");
        Optional<ValidationResult> result = apiSpecification.validate();
        assertFalse(apiSpecification.isValid());
        if (result.isPresent()) {
            assertEquals(2, result.get().getProblemList().size());
        } else {
            fail();
        }
    }

    @Test(expected = ApicatSystemException.class)
    public void shouldValidateDefinitionFailNotSupportedValidator() {
        SwaggerApiDefinition stubApiDefinition = SwaggerApiDefinition.empty();
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.
                fromStub(stubApiDefinition).
                withApiDefinitionValidator(new SwaggerApiDefinitionValidator()).
                build();
        apiDefinition.validate();
    }

    @Test(expected = ApicatSystemException.class)
    public void shouldApiSpecificationValidationCheckFail() {
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath("contracts/yaml/consumerContract.yaml");
        apiSpecification.validate();
        assertTrue(apiSpecification.isValid());

        apiSpecification.addValidator(new SwaggerApiSpecificationValidator());
        apiSpecification.isValid();
    }

    @Test
    public void shouldValidateContractPass() {

        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath("contracts/yaml/consumerContract.yaml");
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath("contracts/yaml/providerContract.yaml").build();

        ApiContract apiContract = new ApiContract();
        apiContract.setApiSpecification(apiSpecification);
        apiContract.setApiDefinition(apiDefinition);

        apiContract.validate();
        assertTrue(apiSpecification.isValid());
        assertTrue(apiDefinition.isValid());
        assertTrue(apiContract.isValid());
    }

    @Test
    public void shouldValidateContractWithErrors() {

        ApiSpecification apiSpecification = SwaggerApiSpecification.fromPath("contracts/yaml/consumerContractWithoutEndpoint.yaml");
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath("contracts/yaml/providerContract.yaml").build();

        ApiContract apiContract = new ApiContract(apiDefinition, apiSpecification);

        Optional<ValidationResult> result = apiContract.validate();
        assertTrue(apiSpecification.isValid());
        assertTrue(apiDefinition.isValid());
        assertFalse(apiContract.isValid());
        if (result.isPresent()) {
            assertEquals(1, result.get().getProblemList().size());
        } else {
            fail();
        }
    }

    @Test
    public void shouldValidateAllContractsWithoutAutodiscoveryValidatorsWithErrors() {
        ApiSpecification apiSpecificationCorrect = SwaggerApiSpecification.fromPath("contracts/yaml/consumerContract.yaml");
        ApiSpecification apiSpecificationIncorrectParameter = SwaggerApiSpecification.fromPath("contracts/yaml/consumerContractWithoutNotRequiredParameter.yaml");
        ApiSpecification apiSpecificationIncorrectEndpoint = SwaggerApiSpecification.fromPath("contracts/yaml/consumerContractWithoutEndpoint.yaml");

        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.
                fromPath("contracts/yaml/providerContract.yaml").
                withoutAutodiscoveryValidators().
                withApiContractValidator(new SwaggerDefinitionApiContractValidator()).
                withApiContractValidator(new SwaggerEndpointApiContractValidator()).
                withApiContractValidator(new SwaggerMetadataApiContractValidator()).
                withContractedApiSpecification(apiSpecificationCorrect).
                withContractedApiSpecification(apiSpecificationIncorrectParameter).
                withContractedApiSpecification(apiSpecificationIncorrectEndpoint).
                build();
        ValidationResult result = apiDefinition.validateAllContracts();
        assertTrue(apiDefinition.areContractsValidated());
        assertFalse(apiDefinition.areContractsValid());
        assertEquals(2, result.getProblemList().size());
    }

    @Test
    public void shouldValidateAgainstSpecificationsWithErrors() {
        List<ApiSpecification> apiSpecifications = new ArrayList<>();
        apiSpecifications.add(SwaggerApiSpecification.fromPath("contracts/yaml/consumerContractWithoutNotRequiredParameter.yaml"));
        apiSpecifications.add(SwaggerApiSpecification.fromPath("contracts/yaml/consumerContractWithoutEndpoint.yaml"));

        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromPath("contracts/yaml/providerContract.yaml").build();

        assertEquals(2,
                apiDefinition.validateAgainstApiSpecifications(apiSpecifications).
                        getProblemList().size());
    }

    @Test(expected = ApicatSystemException.class)
    public void shouldSwaggerOpenAPISpecificationExceptionBeDetected() {
        SwaggerApiSpecification.fromPath("contracts/yaml/incorrectYamlFormat.yaml");
    }
}
