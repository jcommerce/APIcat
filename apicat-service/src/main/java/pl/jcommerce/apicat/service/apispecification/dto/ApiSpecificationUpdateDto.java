package pl.jcommerce.apicat.service.apispecification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiSpecificationUpdateDto {

    private String type;
    private String name;
    private Long contractId;
}
