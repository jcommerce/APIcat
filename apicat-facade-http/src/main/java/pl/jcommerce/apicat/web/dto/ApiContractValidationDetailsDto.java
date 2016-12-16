package pl.jcommerce.apicat.web.dto;

import lombok.Data;

import java.util.Set;

/**
 * Created by jada on 05.12.2016.
 */

@Data
public class ApiContractValidationDetailsDto {
    private Long id;
    private Long contractId;
    private boolean valid;
    private Set<String> differences;
}
