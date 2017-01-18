package pl.jcommerce.apicat.service.apidefinition.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by luwa on 17.01.17.
 */
public class ApiDefinitionUpdateDto {

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
}
