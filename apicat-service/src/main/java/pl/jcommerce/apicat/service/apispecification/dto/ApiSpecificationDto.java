package pl.jcommerce.apicat.service.apispecification.dto;

import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Created by luwa on 18.01.17.
 */
@Getter
@Setter
public class ApiSpecificationDto {

    private Long id;
    private String type;
    private String name;
    private String data;
    private Long contractId;
    private ValidationResult validationResult;
}
