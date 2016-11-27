package pl.jcommerce.apicat.contract.swagger.validation;

import io.swagger.models.Swagger;

/**
 * Created by krka on 14.10.2016.
 */
public interface ContractsValidator {

    Contract validateContract(String customerContractLocation, String providerContractLocation);

    Contract validateContract(Swagger customerSwagger, Swagger providerSwagger);

}
