package pl.jcommerce.apicat.contract.swagger;

import org.junit.Assert;
import org.junit.Test;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.ApiSpecificationFactory;

/**
 * Created by Daniel Charczyński on 2016-11-30.
 */
public class ServiceLoaderApiTest {

    @Test
    public void shouldInstantiateSwaggerApiSpecification() {
        ApiSpecification apiSpecification = ApiSpecificationFactory.newInstance(SwaggerApiSpecification.TYPE);
        Assert.assertNotNull(apiSpecification);
        Assert.assertTrue(apiSpecification instanceof SwaggerApiSpecification);
        Assert.assertEquals(SwaggerApiSpecification.TYPE, apiSpecification.getType());
    }
}
