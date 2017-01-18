package pl.jcommerce.apicat.service.apidefinition.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by luwa on 17.01.17.
 */
public class ApiDefinitionCreateDto {

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
    Long contractId;
}
