package pl.jcommerce.apicat.web.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.repository.ApiDefinitionRepository;
import pl.jcommerce.apicat.web.dto.ApiDefinitionDto;

/**
 * Created by jada on 05.12.2016.
 */
@Component
public class ApiDefinitionMapper extends Mapper<ApiDefinitionEntity, ApiDefinitionDto> {

    @Autowired
    private ApiDefinitionRepository repository;

    @Override
    public ApiDefinitionEntity getEntityFromDto(ApiDefinitionDto dto) {
        ApiDefinitionEntity entity = super.getEntityFromDto(dto);

        if (dto.getId() != null) {
            ApiDefinitionEntity existingEntity = repository.findOne(dto.getId());
            if (existingEntity != null) {
                entity.setContracts(existingEntity.getContracts());
            }
        }

        return entity;
    }
}
