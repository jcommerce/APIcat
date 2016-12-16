package pl.jcommerce.apicat.web.mapper;

import org.springframework.stereotype.Component;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;
import pl.jcommerce.apicat.web.dto.ApiContractValidationDetailsDto;

/**
 * Created by jada on 08.12.2016.
 */

@Component
public class ApiContractValidationDetailsMapper extends Mapper<ApiContractValidationDetailsEntity, ApiContractValidationDetailsDto> {
    @Override
    public ApiContractValidationDetailsEntity getEntityFromDto(ApiContractValidationDetailsDto dto) {
        throw new UnsupportedOperationException();
    }
}
