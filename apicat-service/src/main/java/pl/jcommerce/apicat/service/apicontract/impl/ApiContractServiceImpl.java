package pl.jcommerce.apicat.service.apicontract.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.dao.ApiDefinitionDao;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.exception.ObjectNotFoundException;
import pl.jcommerce.apicat.exception.ObjectType;
import pl.jcommerce.apicat.model.ApiContractModel;
import pl.jcommerce.apicat.model.ApiDefinitionModel;
import pl.jcommerce.apicat.model.ApiSpecificationModel;
import pl.jcommerce.apicat.service.BaseService;
import pl.jcommerce.apicat.service.apicontract.ApiContractService;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractUpdateDto;

/**
 * Created by luwa on 17.01.17.
 */
@Service("apiContractService")
public class ApiContractServiceImpl extends BaseService implements ApiContractService {

    @Autowired
    private ApiContractDao apiContractDao;

    @Autowired
    private ApiDefinitionDao apiDefinitionDao;

    @Autowired
    private ApiSpecificationDao apiSpecificationDao;

    @Override
    public Long createContract(ApiContractCreateDto apiContractDto) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(apiContractDto.getDefinitionId());
        if (apiDefinitionModel == null) {
            throw new ObjectNotFoundException(ObjectType.DEFINITION);
        }

        ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(apiContractDto.getSpecificationId());
        if (apiSpecificationModel == null) {
            throw new ObjectNotFoundException(ObjectType.SPECIFICATION);
        }

        ApiContractModel apiContractModel = new ApiContractModel();
        apiContractModel.setApiDefinitionModel(apiDefinitionModel);
        apiContractModel.setApiSpecificationModel(apiSpecificationModel);

        apiContractModel = apiContractDao.create(apiContractModel);

        return apiContractModel.getId();
    }

    @Override
    public ApiContractDto getContract(Long id) {
        ApiContractModel apiContractModel = apiContractDao.find(id);
        if (apiContractModel == null) {
            throw new ObjectNotFoundException(ObjectType.CONTRACT);
        }
        return mapper.map(apiContractModel, ApiContractDto.class);
    }

    @Override
    public void updateContract(Long id, ApiContractUpdateDto apiContractDto) {
        ApiDefinitionModel apiDefinitionModel = apiDefinitionDao.find(apiContractDto.getDefinitionId());
        if (apiDefinitionModel == null) {
            throw new ObjectNotFoundException(ObjectType.DEFINITION);
        }

        ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(apiContractDto.getSpecificationId());
        if (apiSpecificationModel == null) {
            throw new ObjectNotFoundException(ObjectType.SPECIFICATION);
        }

        ApiContractModel apiContractModel = apiContractDao.find(id);
        if (apiContractModel == null) {
            throw new ObjectNotFoundException(ObjectType.CONTRACT);
        }
        apiContractModel.setApiDefinitionModel(apiDefinitionModel);
        apiContractModel.setApiSpecificationModel(apiSpecificationModel);

        apiContractDao.update(apiContractModel);
    }

    @Override
    public void deleteContract(Long id) {
        apiContractDao.delete(id);
    }
}
