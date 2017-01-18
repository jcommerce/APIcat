package pl.jcommerce.apicat.service.apicontract.dto;

import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Created by luwa on 18.01.17.
 */
public class ApiContractDto {

    @Getter
    @Setter
    Long id;

    @Getter
    @Setter
    Long definitionId;

    @Getter
    @Setter
    Long specificationId;

    @Getter
    @Setter
    ValidationResult validationResult;
}
