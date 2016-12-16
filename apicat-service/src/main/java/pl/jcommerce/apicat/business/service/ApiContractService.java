package pl.jcommerce.apicat.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jcommerce.apicat.business.exception.ResourceNotFoundException;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;
import pl.jcommerce.apicat.model.repository.ApiContractRepository;
import pl.jcommerce.apicat.model.repository.ApiContractValidationDetailsRepository;

/**
 * Created by jada on 07.12.2016.
 */
@Service
public class ApiContractService extends CrudService<ApiContractEntity, ApiContractRepository> {

    @Autowired
    private ApiContractValidationDetailsRepository validationDetailsRepository;

    public ApiContractValidationDetailsEntity getValidationDetails(Long contractId) {
        ApiContractValidationDetailsEntity validationDetails = validationDetailsRepository.findOneByContractId(contractId);

        if (validationDetails == null) {
            throw new ResourceNotFoundException();
        }

        return validationDetails;
    }
}
