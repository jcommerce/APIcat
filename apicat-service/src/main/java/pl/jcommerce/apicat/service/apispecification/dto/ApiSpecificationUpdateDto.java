package pl.jcommerce.apicat.service.apispecification.dto;

import lombok.Getter;
import lombok.Setter;

public class ApiSpecificationUpdateDto {

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
}
