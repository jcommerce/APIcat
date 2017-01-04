package pl.jcommerce.apicat.contract.swagger;

import org.junit.Test;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.ApiSpecificationFactory;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Daniel Charczyński on 2016-11-30.
 */
public class ServiceLoaderApiTest {

    @Test
    public void shouldInstantiateSwaggerApiSpecification() {
        ApiSpecification apiSpecification = ApiSpecificationFactory.newInstance(SwaggerApiSpecification.TYPE);
        assertNotNull(apiSpecification);
        assertTrue(apiSpecification instanceof SwaggerApiSpecification);
        assertEquals(SwaggerApiSpecification.TYPE, apiSpecification.getType());
    }
}
