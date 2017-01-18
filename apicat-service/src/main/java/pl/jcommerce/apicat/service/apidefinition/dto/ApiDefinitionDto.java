package pl.jcommerce.apicat.service.apidefinition.dto;

import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

import java.util.List;

/**
 * Created by luwa on 18.01.17.
 */
public class ApiDefinitionDto {

    @Getter
    @Setter
    Long id;

    @Getter
    @Setter
    String type;

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    String data;

    @Getter
    @Setter
    List<Long> contractIds;

    @Getter
    @Setter
    ValidationResult validationResult;
}
