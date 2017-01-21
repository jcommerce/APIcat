package pl.jcommerce.apicat.service.apidefinition.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.ApiSpecificationStage;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinitionBuilder;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.dao.ApiDefinitionDao;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.exception.ModelNotFoundException;
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
    @Transactional
    public Long createDefinition(ApiDefinitionCreateDto apiDefinitionDto, byte[] content) {
        //TODO Use correct ApiDefinitionBuilder depending on data.type field

        ApiDefinition apiDefinition = null;
        try {
            apiDefinition = SwaggerApiDefinitionBuilder.fromContent(new String(content)).build();
        } catch (Exception ex) {
            throw new ApicatSystemException(ErrorCode.PARSE_JSON_EXCEPTION);
        }
        apiDefinition.setName(apiDefinitionDto.getName());

        apiDefinition.validate();

        ApiDefinitionModel apiDefinitionModel = mapper.map(apiDefinitionDto, ApiDefinitionModel.class);
        apiDefinitionModel.setStage(ApiSpecificationStage.DRAFT.name());
        apiDefinitionModel.setContent(apiDefinition.getContent());
        apiDefinitionModel = apiDefinitionDao.create(apiDefinitionModel);
        return apiDefinitionModel.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiDefinitionDto getDefinition(Long id) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(id);
        if (apiDefinitionModel == null) {
            throw new ModelNotFoundException("Could not find definition data model.");
        }

        return mapper.map(apiDefinitionModel, ApiDefinitionDto.class);
    }

    @Override
    @Transactional
    public void updateDefinition(Long id, ApiDefinitionUpdateDto apiDefinitionDto) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(id);
        if (apiDefinitionModel == null) {
            throw new ModelNotFoundException("Could not find definition data model.");
        }

        List<ApiContractModel> apiContractModels = new ArrayList<>();
        if (!(apiDefinitionDto.getContractIds() == null) && !(apiDefinitionDto.getContractIds().isEmpty())) {
            for (Long contractId : apiDefinitionDto.getContractIds()) {
                ApiContractModel apiContractModel = apiContractDao.find(contractId);
                if (apiContractModel == null) {
                    throw new ModelNotFoundException("Could not find contract data model.");
                }
                apiContractModels.add(apiContractModel);
            }
        }

        mapper.map(apiDefinitionDto, apiDefinitionModel);
        apiDefinitionModel.setApiContractModels(apiContractModels);
        apiDefinitionDao.update(apiDefinitionModel);
    }

    @Override
    @Transactional
    public void updateDefinitionFile(Long id, byte[] content) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(id);
        if (apiDefinitionModel == null) {
            throw new ModelNotFoundException("Could not find definition data model.");
        }

        apiDefinitionModel.setContent(new String(content));
        apiDefinitionDao.update(apiDefinitionModel);
    }

    @Override
    @Transactional
    public void deleteDefinition(Long id) {
        apiDefinitionDao.delete(id);
    }

    @Override
    @Transactional
    public ValidationResult validateAgainstSpecifications(Long definitionId, List<Long> specificationIds) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(definitionId);
        if (apiDefinitionModel == null) {
            throw new ModelNotFoundException("Could not find definition data model.");
        }
        //TODO Use correct ApiDefinition implementation depending on type field
        SwaggerApiDefinition apiDefinition = mapper.map(apiDefinitionModel, SwaggerApiDefinition.class);
        apiDefinition.generateSwaggerFromContent();

        List<ApiSpecification> apiSpecifications = new ArrayList<>();
        for (Long specificationId : specificationIds) {
            ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(specificationId);
            if (apiSpecificationModel == null) {
                throw new ModelNotFoundException("Could not find specyfication data model.");
            }
            //TODO Use correct ApiSpecification implementation depending on type field
            SwaggerApiSpecification apiSpecification = mapper.map(apiSpecificationModel, SwaggerApiSpecification.class);
            apiSpecification.generateSwaggerFromContent();
            apiSpecifications.add(apiSpecification);
        }

        return apiDefinition.validateAgainstApiSpecifications(apiSpecifications);
    }

    @Override
    @Transactional
    public ValidationResult validateAgainstAllSpecifications(Long id) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(id);
        if (apiDefinitionModel == null) {
            throw new ModelNotFoundException("Could not find definition data model.");
        }
        //TODO Use correct ApiDefinition implementation depending on type field
        SwaggerApiDefinition apiDefinition = mapper.map(apiDefinitionModel, SwaggerApiDefinition.class);
        apiDefinition.generateSwaggerFromContent();

        List<ApiContract> apiContracts = new ArrayList<>();
        for (ApiContractModel apiContractModel : apiDefinitionModel.getApiContractModels()) {
            ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(apiContractModel.getApiSpecificationModel().getId());
            SwaggerApiSpecification apiSpecification = mapper.map(apiSpecificationModel, SwaggerApiSpecification.class);
            apiSpecification.generateSwaggerFromContent();

            ApiContract apiContract = new ApiContract();
            apiContract.setApiDefinition(apiDefinition);
            apiContract.setApiSpecification(apiSpecification);
            apiContracts.add(apiContract);
        }
        apiDefinition.setApiContracts(apiContracts);

        return apiDefinition.validateAllContracts();
    }
}
