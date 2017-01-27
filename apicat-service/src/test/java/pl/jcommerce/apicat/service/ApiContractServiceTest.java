package pl.jcommerce.apicat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.dao.ApiDefinitionDao;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.exception.ModelNotFoundException;
import pl.jcommerce.apicat.model.ApiContractModel;
import pl.jcommerce.apicat.model.ApiDefinitionModel;
import pl.jcommerce.apicat.model.ApiSpecificationModel;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractUpdateDto;
import pl.jcommerce.apicat.service.apicontract.impl.ApiContractServiceImpl;

import java.util.ArrayList;
import java.util.List;

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
    public void testCreateContract() {
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

    @Test(expected = ModelNotFoundException.class)
    public void testCreateContractDefinitionNotFound() {
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
    public void testGetContract() {
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

    @Test(expected = ModelNotFoundException.class)
    public void testUpdateContractSpecificationNotFound() {
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

        Mockito.verify(apiContractDao).update(apiContractModel);

        apiContractUpdateDto.setSpecificationId(4L);
        apiContractService.updateContract(contractId, apiContractUpdateDto);
    }

    @Test
    public void testDeleteContract() {
        ApiDefinitionModel apiDefinitionModel = new ApiDefinitionModel();
        apiDefinitionModel.setId(definitionId);

        ApiSpecificationModel apiSpecificationModel = new ApiSpecificationModel();
        apiSpecificationModel.setId(specificationId);

        ApiContractModel apiContractModel = new ApiContractModel();
        apiContractModel.setId(contractId);

        apiContractModel.setApiDefinitionModel(apiDefinitionModel);
        apiContractModel.setApiSpecificationModel(apiSpecificationModel);

        ApiContractModel apiContractModel2 = new ApiContractModel();
        apiContractModel2.setId(4L);
        apiContractModel2.setApiDefinitionModel(apiDefinitionModel);

        List<ApiContractModel> contractModelList = new ArrayList<>();
        contractModelList.add(apiContractModel2);
        contractModelList.add(apiContractModel);
        apiDefinitionModel.setApiContractModels(contractModelList);
        apiSpecificationModel.setApiContractModel(apiContractModel);

        when(apiContractDao.find(contractId)).thenReturn(apiContractModel);

        apiContractService.deleteContract(contractId);

        Mockito.verify(apiSpecificationDao).update(apiSpecificationModel);
        Mockito.verify(apiDefinitionDao).update(apiDefinitionModel);
        Mockito.verify(apiContractDao).delete(contractId);
    }
}
