package pl.jcommerce.apicat.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jcommerce.apicat.business.exception.ResourceNotFoundException;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.entity.ApiSpecificationEntity;
import pl.jcommerce.apicat.model.repository.ApiSpecificationRepository;

/**
 * Created by jada on 07.12.2016.
 */

@Service
public class ApiSpecificationService extends CrudService<ApiSpecificationEntity, ApiSpecificationRepository> {

    @Autowired
    private ApiContractService apiContractService;

    @Autowired
    private ApiDefinitionService apiDefinitionService;

    @Autowired
    private ValidationService validationService;

    public ApiSpecificationEntity addApiSpecification(ApiSpecificationEntity specification, Long definitionId) {
        ApiDefinitionEntity definition = apiDefinitionService.getRepository().getOne(definitionId);

        if (definition == null) {
            throw new ResourceNotFoundException();
        }

        ApiContractEntity contract = new ApiContractEntity(definition, specification);

        boolean isValid = validationService.isContractValid(contract);

        ApiContractValidationDetailsEntity validationDetails = new ApiContractValidationDetailsEntity();
        validationDetails.setValid(isValid);

        //TODO: return validationResult instead of boolean and populate validation details differences

        contract.setValidationDetails(validationDetails);

        apiContractService.save(contract);

        specification.setContract(contract);

        return specification;
    }

    public Page<ApiSpecificationEntity> findAllByDefinitionId(Long definitionId, Pageable pageable) {
        if (!apiDefinitionService.exists(definitionId)) {
            throw new ResourceNotFoundException();
        }

        return repository.findAllByContractDefinitionId(definitionId, pageable);
    }

    public ApiSpecificationEntity findOneByIdAndDefinitionId(Long id, Long definitionId) {
        ApiSpecificationEntity specification = repository.findOneByIdAndContractDefinitionId(id, definitionId);

        if (specification == null) {
            throw new ResourceNotFoundException();
        }

        return specification;
    }

    public ApiSpecificationEntity deleteByIdAndDefinitionId(Long id, Long definitionId) {
        ApiSpecificationEntity entity = findOneByIdAndDefinitionId(id, definitionId);

        if (entity == null) {
            throw new ResourceNotFoundException();
        } else {
            delete(id);
        }

        return entity;
    }

    @Transactional
    public ApiSpecificationEntity updateApiSpecification(ApiSpecificationEntity specification, Long id, Long definitionId) {
        if (!repository.existsByIdAndContractDefinitionId(id, definitionId)) {
            throw new ResourceNotFoundException();
        }

        ApiContractEntity contract = specification.getContract();

        boolean isValid = validationService.isContractValid(contract);

        ApiContractValidationDetailsEntity validationDetails = contract.getValidationDetails();
        validationDetails.setValid(isValid);

        //TODO: return validationResult instead of boolean and populate validation details differences

        return update(specification, id);
    }
}
