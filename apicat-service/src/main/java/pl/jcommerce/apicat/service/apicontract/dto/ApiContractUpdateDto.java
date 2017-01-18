package pl.jcommerce.apicat.service.apicontract.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by luwa on 18.01.17.
 */
public class ApiContractUpdateDto {

    @Getter
    @Setter
    Long Id;

    @Getter
    @Setter
    Long definitionId;

    @Getter
    @Setter
    Long specificationId;
}
