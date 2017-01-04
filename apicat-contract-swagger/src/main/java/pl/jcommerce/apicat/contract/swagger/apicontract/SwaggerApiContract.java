package pl.jcommerce.apicat.contract.swagger.apicontract;

import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;

/**
 * Created by luwa on 22.12.16.
 */
public class SwaggerApiContract extends ApiContract {

    public SwaggerApiContract(SwaggerApiDefinition apiDefinition, SwaggerApiSpecification apiSpecification) {
        this.setApiDefinition(apiDefinition);
        this.setApiSpecification(apiSpecification);
    }
}
