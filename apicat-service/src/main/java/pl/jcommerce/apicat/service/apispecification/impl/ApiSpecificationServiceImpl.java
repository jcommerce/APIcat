package pl.jcommerce.apicat.service.apispecification.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.exception.ModelNotFoundException;
import pl.jcommerce.apicat.model.ApiContractModel;
import pl.jcommerce.apicat.model.ApiSpecificationModel;
import pl.jcommerce.apicat.service.BaseService;
import pl.jcommerce.apicat.service.apispecification.ApiSpecificationService;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationCreateDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationUpdateDto;

@Service("apiSpecificationService")
public class ApiSpecificationServiceImpl extends BaseService implements ApiSpecificationService {

    @Autowired
    private ApiSpecificationDao apiSpecificationDao;

    @Autowired
    private ApiContractDao apiContractDao;

    @Override
    @Transactional
    public Long createSpecification(ApiSpecificationCreateDto apiSpecificationDto, byte[] content) {
        //TODO Use correct ApiSpecificationBuilder depending on data.type field
        ApiSpecification apiSpecification = SwaggerApiSpecification.fromContent(new String(content));
        apiSpecification.setName(apiSpecificationDto.getName());

        apiSpecification.validate();

        ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.create(mapper.map(apiSpecification, ApiSpecificationModel.class));
        return apiSpecificationModel.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiSpecificationDto getSpecification(Long id) {
        ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(id);
        if (apiSpecificationModel == null) {
            throw new ModelNotFoundException("Could not find specification data model.");
        }

        return mapper.map(apiSpecificationModel, ApiSpecificationDto.class);
    }

    @Override
    @Transactional
    public void updateSpecification(Long id, ApiSpecificationUpdateDto apiSpecificationDto) {
        ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(id);
        if (apiSpecificationModel == null) {
            throw new ModelNotFoundException("Could not find specification data model.");
        }

        ApiContractModel apiContractModel = null;
        if (apiSpecificationDto.getContractId() != null) {
            apiContractModel = apiContractDao.find(apiSpecificationDto.getContractId());
            if (apiContractModel == null) {
                throw new ModelNotFoundException("Could not find contract data model.");
            }
        }

        mapper.map(apiSpecificationDto, apiSpecificationModel);
        apiSpecificationModel.setApiContractModel(apiContractModel);
        apiSpecificationDao.update(apiSpecificationModel);
    }

    @Override
    @Transactional
    public void updateSpecificationFile(Long id, byte[] content) {
        ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(id);
        if (apiSpecificationModel == null) {
            throw new ModelNotFoundException("Could not find specification data model.");
        }

        apiSpecificationModel.setContent(new String(content));
        apiSpecificationDao.update(apiSpecificationModel);
    }

    @Override
    @Transactional
    public void deleteSpecification(Long id) {
        ApiSpecificationModel apiSpecificationModel = apiSpecificationDao.find(id);
        if (apiSpecificationModel == null) {
            throw new ModelNotFoundException("Could not find specification data model.");
        }

        if(apiSpecificationModel.getApiContractModel() != null && apiSpecificationModel.getApiContractModel().getApiSpecificationModel() != null) {
            ApiContractModel apiContractModel = apiSpecificationModel.getApiContractModel();
            if (apiContractModel.getApiSpecificationModel().getId().equals(id)) {
                apiContractModel.setApiSpecificationModel(null);
                apiContractDao.update(apiContractModel);
            }
        }

        apiSpecificationDao.delete(id);
    }
}
