package pl.jcommerce.apicat.service.apicontract.dto;

import lombok.Getter;
import lombok.Setter;

public class ApiContractCreateDto {

    @Getter
    @Setter
    Long definitionId;

    @Getter
    @Setter
    Long specificationId;
}
