package pl.jcommerce.apicat.service.apicontract.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.dao.ApiDefinitionDao;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.exception.ModelNotFoundException;
import pl.jcommerce.apicat.model.ApiContractModel;
import pl.jcommerce.apicat.model.ApiDefinitionModel;
import pl.jcommerce.apicat.model.ApiSpecificationModel;
import pl.jcommerce.apicat.service.BaseService;
import pl.jcommerce.apicat.service.apicontract.ApiContractService;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractUpdateDto;

@Service("apiContractService")
public class ApiContractServiceImpl extends BaseService implements ApiContractService {

    @Autowired
    private ApiContractDao apiContractDao;

    @Autowired
    private ApiDefinitionDao apiDefinitionDao;

    @Autowired
    private ApiSpecificationDao apiSpecificationDao;

    @Override
    @Transactional
    public Long createContract(ApiContractCreateDto apiContractDto) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(apiContractDto.getDefinitionId());
        if (apiDefinitionModel == null) {
            throw new ModelNotFoundException("Could not find definition data model.");
        }

        ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(apiContractDto.getSpecificationId());
        if (apiSpecificationModel == null) {
            throw new ModelNotFoundException("Could not find specification data model.");
        }

        ApiContractModel apiContractModel = new ApiContractModel();
        apiContractModel.setApiDefinitionModel(apiDefinitionModel);
        apiContractModel.setApiSpecificationModel(apiSpecificationModel);

        apiContractModel = apiContractDao.create(apiContractModel);

        return apiContractModel.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiContractDto getContract(Long id) {
        ApiContractModel apiContractModel = apiContractDao.find(id);
        if (apiContractModel == null) {
            throw new ModelNotFoundException("Could not find contract data model.");
        }
        return mapper.map(apiContractModel, ApiContractDto.class);
    }

    @Override
    @Transactional
    public void updateContract(Long id, ApiContractUpdateDto apiContractDto) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(apiContractDto.getDefinitionId());
        if (apiDefinitionModel == null) {
            throw new ModelNotFoundException("Could not find definition data model.");
        }

        ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(apiContractDto.getSpecificationId());
        if (apiSpecificationModel == null) {
            throw new ModelNotFoundException("Could not find specification data model.");
        }

        ApiContractModel apiContractModel = apiContractDao.find(id);
        if (apiContractModel == null) {
            throw new ModelNotFoundException("Could not find contract data model.");
        }
        apiContractModel.setApiDefinitionModel(apiDefinitionModel);
        apiContractModel.setApiSpecificationModel(apiSpecificationModel);

        apiContractDao.update(apiContractModel);
    }

    @Override
    @Transactional
    public void deleteContract(Long id) {
        apiContractDao.delete(id);
    }
}
