package pl.jcommerce.apicat.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jcommerce.apicat.contract.exception.ApiValidationException;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.repository.ApiDefinitionRepository;

/**
 * Created by jada on 05.12.2016.
 */
@Service
public class ApiDefinitionService extends CrudService<ApiDefinitionEntity, ApiDefinitionRepository> {

    @Autowired
    private ValidationService validationService;

    @Override
    public ApiDefinitionEntity update(ApiDefinitionEntity entity, Long id) {
        if (!validationService.isDefinitionValid(entity)) {
            throw new ApiValidationException("Invalid api definition");
        }

        return super.update(entity, id);
    }

    @Override
    public ApiDefinitionEntity save(ApiDefinitionEntity entity) {
        if (!validationService.isDefinitionValid(entity)) {
            throw new ApiValidationException("Invalid api definition");
        }

        return super.save(entity);
    }
}
