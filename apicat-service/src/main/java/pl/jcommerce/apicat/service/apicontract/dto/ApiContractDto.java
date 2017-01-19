package pl.jcommerce.apicat.service.apicontract.dto;

import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Created by luwa on 18.01.17.
 */
@Getter
@Setter
public class ApiContractDto {

    Long id;
    Long definitionId;
    Long specificationId;
    ValidationResult validationResult;
}
