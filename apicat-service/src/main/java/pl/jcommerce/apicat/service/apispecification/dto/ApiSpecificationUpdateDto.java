package pl.jcommerce.apicat.service.apispecification.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by luwa on 18.01.17.
 */

@Getter
@Setter
public class ApiSpecificationUpdateDto {

    String type;
    String name;
    Long contractId;
}
