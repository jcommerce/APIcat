package pl.jcommerce.apicat.service.apicontract.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiContractCreateDto {

    private Long definitionId;
    private Long specificationId;
}
