package pl.jcommerce.apicat.service.apidefinition.dto;

import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

import java.util.List;

/**
 * Created by luwa on 18.01.17.
 */
@Getter
@Setter
public class ApiDefinitionDto {

    private Long id;
    private String type;
    private String name;
    private String stage;
    private String data;
    private List<Long> contractIds;
    private ValidationResult validationResult;
}
