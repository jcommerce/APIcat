package pl.jcommerce.apicat.contract.swagger.validation;

import io.github.robwin.swagger.test.SwaggerAssertions;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import pl.jcommerce.apicat.contract.swagger.TestUtils;

/**
 * Created by krka on 28.10.2016.
 */
public class ContractsValidationViaAssertjSwaggerTest {

    private String consumerSwaggerLocation;
    private String providerSwaggerLocation;

    @Test
    public void shouldConsumerAndProviderContractsBeEqual() {
        localizeSwaggerDefinitions("/yaml/sameConsumerContract.yaml");
        Validate.notNull(consumerSwaggerLocation, "actualLocation must not be null!");
        Validate.notNull(providerSwaggerLocation, "actualLocation must not be null!");
        SwaggerAssertions.assertThat(consumerSwaggerLocation).satisfiesContract(providerSwaggerLocation);
        SwaggerAssertions.assertThat(consumerSwaggerLocation).isEqualTo(providerSwaggerLocation);
    }

    @Test
    public void shouldUnusedEndpointBeDetected() {
        localizeSwaggerDefinitions("/yaml/consumerContractWithoutEndpoint.yaml");
        Validate.notNull(consumerSwaggerLocation, "actualLocation must not be null!");
        Validate.notNull(providerSwaggerLocation, "actualLocation must not be null!");
        // To support the difference on the level of whole endpoints - the order of invocation of the assertion is reversed
        SwaggerAssertions.assertThat(providerSwaggerLocation).satisfiesContract(consumerSwaggerLocation);
    }

    // it doesn't support the difference on the level of the granulity smaller than the whole endpoint e.g. any of the methods (in this case "put" method)
    @Test(expected = AssertionError.class)
    public void shouldUnusedPutMethodBeDetected() {
        localizeSwaggerDefinitions("/yaml/consumerContractWithoutPutMethod.yaml");
        SwaggerAssertions.assertThat(consumerSwaggerLocation).satisfiesContract(providerSwaggerLocation);
        Validate.notNull(consumerSwaggerLocation, "actualLocation must not be null!");
        Validate.notNull(providerSwaggerLocation, "actualLocation must not be null!");
    }

    // it doesn't support the difference on the level of the granulity smaller than the whole endpoint e.g. parameters (in this case wrong parameter type)
    @Test(expected = AssertionError.class)
    public void shouldUnusedParameterBeDetected() {
        localizeSwaggerDefinitions("/yaml/consumerContractWithoutRequiredParameter.yaml");
        SwaggerAssertions.assertThat(consumerSwaggerLocation).satisfiesContract(providerSwaggerLocation);
        Validate.notNull(consumerSwaggerLocation, "actualLocation must not be null!");
        Validate.notNull(providerSwaggerLocation, "actualLocation must not be null!");
    }

    private void localizeSwaggerDefinitions(String consumerContractLocation) {
        consumerSwaggerLocation = TestUtils.getTestConstractsPath() + consumerContractLocation;
        providerSwaggerLocation = TestUtils.getTestConstractsPath() + "/yaml/providerContract.yaml";
    }


}
