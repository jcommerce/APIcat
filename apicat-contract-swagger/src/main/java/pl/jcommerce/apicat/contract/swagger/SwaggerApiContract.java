package pl.jcommerce.apicat.contract.swagger;

import pl.jcommerce.apicat.contract.ApiContract;

/**
 * Created by luwa on 22.12.16.
 */
public class SwaggerApiContract extends ApiContract {

    SwaggerApiContract(SwaggerApiDefinition apiDefinition, SwaggerApiSpecification apiSpecification) {
        this.setApiDefinition(apiDefinition);
        this.setApiSpecification(apiSpecification);
    }
}
