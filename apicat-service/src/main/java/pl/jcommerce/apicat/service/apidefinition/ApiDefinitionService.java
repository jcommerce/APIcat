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

    Long createDefinition(ApiDefinitionCreateDto apiDefinition, byte[] content);

    ApiDefinitionDto getDefinition(Long id);

    void updateDefinition(Long id, ApiDefinitionUpdateDto apiDefinition);

    void updateDefinitionFile(Long id, byte[] content);

    void deleteDefinition(Long id);

    ValidationResult validateAgainstSpecifications(Long definitionId, List<Long> specificationIds);

    ValidationResult validateAgainstAllSpecifications(Long id);
}
