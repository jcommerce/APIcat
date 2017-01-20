package pl.jcommerce.apicat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.dao.ApiDefinitionDao;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.exception.ObjectNotFoundException;
import pl.jcommerce.apicat.model.ApiContractModel;
import pl.jcommerce.apicat.model.ApiDefinitionModel;
import pl.jcommerce.apicat.model.ApiSpecificationModel;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractUpdateDto;
import pl.jcommerce.apicat.service.apicontract.impl.ApiContractServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * Created by luwa on 17.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiContractServiceTest {

    @InjectMocks
    private ApiContractServiceImpl apiContractService;

    @Mock
    private ApiContractDao apiContractDao;

    @Mock
    private ApiDefinitionDao apiDefinitionDao;

    @Mock
    private ApiSpecificationDao apiSpecificationDao;

    private final Long contractId = 1L;
    private final Long definitionId = 2L;
    private final Long specificationId = 3L;

    @Test
    public void createContract() {
        ApiDefinitionModel apiDefinitionModel = new ApiDefinitionModel();
        apiDefinitionModel.setId(definitionId);
        when(apiDefinitionDao.find(definitionId)).thenReturn(apiDefinitionModel);

        ApiSpecificationModel apiSpecificationModel = new ApiSpecificationModel();
        apiSpecificationModel.setId(specificationId);
        when(apiSpecificationDao.find(specificationId)).thenReturn(apiSpecificationModel);

        ApiContractModel apiContractModel = new ApiContractModel();
        apiContractModel.setId(contractId);
        when(apiContractDao.create(any(ApiContractModel.class))).thenReturn(apiContractModel);

        ApiContractCreateDto apiContractCreateDto = new ApiContractCreateDto();
        apiContractCreateDto.setDefinitionId(definitionId);
        apiContractCreateDto.setSpecificationId(specificationId);

        Long returnedContractId = apiContractService.createContract(apiContractCreateDto);
        assertEquals(contractId, returnedContractId);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void createContractDefinitionNotFound() {
        ApiDefinitionModel apiDefinitionModel = new ApiDefinitionModel();
        apiDefinitionModel.setId(definitionId);
        when(apiDefinitionDao.find(definitionId)).thenReturn(apiDefinitionModel);

        ApiSpecificationModel apiSpecificationModel = new ApiSpecificationModel();
        apiSpecificationModel.setId(specificationId);
        when(apiSpecificationDao.find(specificationId)).thenReturn(apiSpecificationModel);

        ApiContractModel apiContractModel = new ApiContractModel();
        apiContractModel.setId(contractId);
        when(apiContractDao.create(any(ApiContractModel.class))).thenReturn(apiContractModel);

        ApiContractCreateDto apiContractCreateDto = new ApiContractCreateDto();
        apiContractCreateDto.setDefinitionId(5L);
        apiContractCreateDto.setSpecificationId(specificationId);

        apiContractService.createContract(apiContractCreateDto);
    }

    @Test
    public void getContract() {
        ApiSpecificationModel apiSpecificationModel = new ApiSpecificationModel();
        apiSpecificationModel.setId(specificationId);

        ApiDefinitionModel apiDefinitionModel = new ApiDefinitionModel();
        apiDefinitionModel.setId(definitionId);

        ApiContractModel apiContractModel = new ApiContractModel();
        apiContractModel.setId(contractId);
        apiContractModel.setApiSpecificationModel(apiSpecificationModel);
        apiContractModel.setApiDefinitionModel(apiDefinitionModel);

        when(apiContractDao.find(contractId)).thenReturn(apiContractModel);

        ApiContractDto apiContract = apiContractService.getContract(contractId);
        assertEquals(contractId, apiContract.getId());
        assertEquals(specificationId, apiContract.getSpecificationId());
        assertEquals(definitionId, apiContract.getDefinitionId());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateContractSpecificationNotFound() {
        ApiDefinitionModel apiDefinitionModel = new ApiDefinitionModel();
        apiDefinitionModel.setId(definitionId);
        when(apiDefinitionDao.find(definitionId)).thenReturn(apiDefinitionModel);

        ApiSpecificationModel apiSpecificationModel = new ApiSpecificationModel();
        apiSpecificationModel.setId(specificationId);
        when(apiSpecificationDao.find(specificationId)).thenReturn(apiSpecificationModel);

        ApiContractModel apiContractModel = new ApiContractModel();
        apiContractModel.setId(contractId);
        when(apiContractDao.find(contractId)).thenReturn(apiContractModel);

        ApiContractUpdateDto apiContractUpdateDto = new ApiContractUpdateDto();
        apiContractUpdateDto.setDefinitionId(definitionId);
        apiContractUpdateDto.setSpecificationId(specificationId);

        apiContractService.updateContract(contractId, apiContractUpdateDto);

        apiContractUpdateDto.setSpecificationId(4L);
        apiContractService.updateContract(contractId, apiContractUpdateDto);
    }
}
