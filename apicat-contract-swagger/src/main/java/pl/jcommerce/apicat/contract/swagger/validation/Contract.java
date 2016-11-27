package pl.jcommerce.apicat.contract.swagger.validation;

import io.swagger.models.Swagger;
import lombok.Data;

/**
 * Created by krka on 23.10.2016.
 */
@Data
public class Contract {

    private DiffDetails diffDetails = new DiffDetails();

    private Swagger customerSwaggerDefinition;

    private Swagger providerSwaggerDefinition;

    private boolean valid = true;

    private boolean swaggerDefinitionsEqual = true;

    // indicates compliance of the swagger definitions to the Swagger Specification
    private boolean isConsumerSwaggerDefinitionConsistant = true;
    private boolean isProviderSwaggerDefinitionConsistant = true;
}
