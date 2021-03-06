package pl.jcommerce.apicat.service.apidefinition.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by luwa on 17.01.17.
 */
@Getter
@Setter
public class ApiDefinitionUpdateDto {

    private String type;
    private String name;
    private List<Long> contractIds;
}
