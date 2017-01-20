package pl.jcommerce.apicat.service.apidefinition.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinitionBuilder;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.dao.ApiDefinitionDao;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.exception.ObjectNotFoundException;
import pl.jcommerce.apicat.exception.ObjectType;
import pl.jcommerce.apicat.model.ApiContractModel;
import pl.jcommerce.apicat.model.ApiDefinitionModel;
import pl.jcommerce.apicat.model.ApiSpecificationModel;
import pl.jcommerce.apicat.service.BaseService;
import pl.jcommerce.apicat.service.apidefinition.ApiDefinitionService;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionCreateDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionUpdateDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luwa on 17.01.17.
 */
@Service("apiDefinitionService")
public class ApiDefinitionServiceImpl extends BaseService implements ApiDefinitionService {

    @Autowired
    private ApiContractDao apiContractDao;

    @Autowired
    private ApiDefinitionDao apiDefinitionDao;

    @Autowired
    private ApiSpecificationDao apiSpecificationDao;

    @Override
    public Long createDefinition(ApiDefinitionCreateDto apiDefinitionDto, byte[] content) {
        //TODO Use correct ApiDefinitionBuilder depending on data.type field
        ApiDefinition apiDefinition = SwaggerApiDefinitionBuilder.fromContent(new String(content)).build();
        apiDefinition.setName(apiDefinitionDto.getName());

        apiDefinition.validate();

        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.create(mapper.map(apiDefinition, ApiDefinitionModel.class));
        return apiDefinitionModel.getId();
    }

    @Override
    public ApiDefinitionDto getDefinition(Long id) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(id);
        if (apiDefinitionModel == null) {
            throw new ObjectNotFoundException(ObjectType.DEFINITION);
        }

        return mapper.map(apiDefinitionModel, ApiDefinitionDto.class);
    }

    @Override
    public void updateDefinition(Long id, ApiDefinitionUpdateDto apiDefinitionDto) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(id);
        if (apiDefinitionModel == null) {
            throw new ObjectNotFoundException(ObjectType.DEFINITION);
        }

        List<ApiContractModel> apiContractModels = new ArrayList<>();
        if (!apiContractModels.isEmpty()) {
            for (Long contractId : apiDefinitionDto.getContractIds()) {
                ApiContractModel apiContractModel = apiContractDao.find(contractId);
                if (apiContractModel == null) {
                    throw new ObjectNotFoundException(ObjectType.CONTRACT);
                }
                apiContractModels.add(apiContractModel);
            }
        }

        mapper.map(apiDefinitionDto, apiDefinitionModel);
        apiDefinitionModel.setApiContractModels(apiContractModels);
        apiDefinitionDao.update(apiDefinitionModel);
    }

    @Override
    public void updateDefinitionFile(Long id, byte[] content) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(id);
        if (apiDefinitionModel == null) {
            throw new ObjectNotFoundException(ObjectType.DEFINITION);
        }

        apiDefinitionModel.setContent(new String(content));
        apiDefinitionDao.update(apiDefinitionModel);
    }

    @Override
    public void deleteDefinition(Long id) {
        apiDefinitionDao.delete(id);
    }

    @Override
    public ValidationResult validateAgainstSpecifications(Long definitionId, List<Long> specificationIds) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(definitionId);
        //TODO Use correct ApiDefinition implementation depending on type field
        ApiDefinition apiDefinition = mapper.map(apiDefinitionModel, SwaggerApiDefinition.class);

        List<ApiSpecification> apiSpecifications = new ArrayList<>();
        for (Long specificationId : specificationIds) {
            ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(specificationId);
            //TODO Use correct ApiSpecification implementation depending on type field
            ApiSpecification apiSpecification = mapper.map(apiSpecificationModel, SwaggerApiSpecification.class);
            apiSpecifications.add(apiSpecification);
        }

        return apiDefinition.validateAgainstApiSpecifications(apiSpecifications);
    }

    @Override
    public ValidationResult validateAgainstAllSpecifications(Long id) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(id);
        //TODO Use correct ApiDefinition implementation depending on type field
        ApiDefinition apiDefinition = mapper.map(apiDefinitionModel, SwaggerApiDefinition.class);
        return apiDefinition.validateAllContracts();
    }
}
