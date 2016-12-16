package pl.jcommerce.apicat.web.mapper;

import org.springframework.stereotype.Component;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.web.dto.ApiContractDto;

/**
 * Created by jada on 05.12.2016.
 */
@Component
public class ApiContractMapper extends Mapper<ApiContractEntity, ApiContractDto> {
    @Override
    public ApiContractEntity getEntityFromDto(ApiContractDto dto) {
        throw new UnsupportedOperationException();
    }
}
