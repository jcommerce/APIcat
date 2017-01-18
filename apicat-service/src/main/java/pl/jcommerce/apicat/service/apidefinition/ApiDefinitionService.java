package pl.jcommerce.apicat.service.apidefinition;

import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionCreateDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionUpdateDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionDto;

import java.util.List;

/**
 * Created by luwa on 18.01.17.
 */
public interface ApiDefinitionService {

    Long createDefinition(ApiDefinitionCreateDto data);

    ApiDefinitionDto getDefinition(Long id);

    Long updateDefinition(ApiDefinitionUpdateDto data);

    void deleteDefinition(Long id);

    ValidationResult validateAgainstSpecifications(Long definitionId, List<Long> specificationIds);

    ValidationResult validateAgainstAllSpecifications(Long id);
}
