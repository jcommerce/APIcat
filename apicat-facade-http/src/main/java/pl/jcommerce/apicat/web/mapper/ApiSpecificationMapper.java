package pl.jcommerce.apicat.web.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jcommerce.apicat.model.entity.ApiSpecificationEntity;
import pl.jcommerce.apicat.model.repository.ApiSpecificationRepository;
import pl.jcommerce.apicat.web.dto.ApiSpecificationDto;

/**
 * Created by jada on 07.12.2016.
 */

@Component
public class ApiSpecificationMapper extends Mapper<ApiSpecificationEntity, ApiSpecificationDto> {

    @Autowired
    private ApiSpecificationRepository repository;

    @Override
    public ApiSpecificationEntity getEntityFromDto(ApiSpecificationDto dto) {
        ApiSpecificationEntity entity = super.getEntityFromDto(dto);

        if (dto.getId() != null) {
            ApiSpecificationEntity existingEntity = repository.findOne(dto.getId());
            if (existingEntity != null) {
                entity.setContract(existingEntity.getContract());
            }
        }

        return entity;
    }
}
