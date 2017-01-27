package pl.jcommerce.apicat.service;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.contract.validation.result.ValidationResultCategory;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.dao.ApiDefinitionDao;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.exception.ModelNotFoundException;
import pl.jcommerce.apicat.model.ApiContractModel;
import pl.jcommerce.apicat.model.ApiDefinitionModel;
import pl.jcommerce.apicat.model.ApiSpecificationModel;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionCreateDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionUpdateDto;
import pl.jcommerce.apicat.service.apidefinition.impl.ApiDefinitionServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by luwa on 18.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiDefinitionServiceTest {

    @InjectMocks
    private ApiDefinitionServiceImpl apiDefinitionService;

    @Mock
    private ApiContractDao apiContractDao;

    @Mock
    private ApiDefinitionDao apiDefinitionDao;

    @Mock
    private ApiSpecificationDao apiSpecificationDao;

    private final Long definitionId = 1L;
    private final Long contractId = 2L;
    private final Long specificationId = 3L;
    private final String definitionName = "Test";
    private final String definitionType = SwaggerApiDefinition.TYPE;
    private final String testFilepath = "consumerContract.yaml";
    private final String content = getContent();

    @Test
    public void testCreateDefinition() {
        ApiDefinitionModel apiDefinitionModel = setupApiDefinitionModel();
        when(apiDefinitionDao.create(any(ApiDefinitionModel.class))).thenReturn(apiDefinitionModel);

        ApiDefinitionCreateDto apiDefinitionCreateDto = new ApiDefinitionCreateDto();
        apiDefinitionCreateDto.setName(definitionName);
        apiDefinitionCreateDto.setType(definitionType);

        byte[] bytes = content.getBytes();

        Long returnedContractId = apiDefinitionService.createDefinition(apiDefinitionCreateDto, bytes);
        assertEquals(definitionId, returnedContractId);
    }

    @Test
    public void testGetDefinition() {
        ApiDefinitionModel apiDefinitionModel = setupApiDefinitionModel();
        when(apiDefinitionDao.find(definitionId)).thenReturn(apiDefinitionModel);

        ApiDefinitionDto apiDefinitionDto = apiDefinitionService.getDefinition(definitionId);
        assertEquals(definitionId, apiDefinitionDto.getId());
        assertEquals(definitionName, apiDefinitionDto.getName());
        assertEquals(content, apiDefinitionDto.getData());
        assertEquals(Collections.singletonList(contractId), apiDefinitionDto.getContractIds());
    }

    @Test(expected = ModelNotFoundException.class)
    public void testUpdateDefinitionContractNotFound() {
        ApiDefinitionModel apiDefinitionModel = setupApiDefinitionModel();
        when(apiDefinitionDao.find(definitionId)).thenReturn(apiDefinitionModel);

        ApiContractModel apiContractModel = new ApiContractModel();
        apiContractModel.setId(contractId);
        when(apiContractDao.find(contractId)).thenReturn(apiContractModel);

        ApiDefinitionUpdateDto apiDefinitionUpdateDto = new ApiDefinitionUpdateDto();
        apiDefinitionUpdateDto.setName("Test2");
        apiDefinitionService.updateDefinition(definitionId, apiDefinitionUpdateDto);

        Mockito.verify(apiDefinitionDao).update(apiDefinitionModel);

        apiDefinitionUpdateDto.setContractIds(Arrays.asList(contractId, 3L));
        apiDefinitionService.updateDefinition(definitionId, apiDefinitionUpdateDto);
    }

    @Test
    public void testDeleteDefinition() {
        ApiDefinitionModel apiDefinitionModel = setupApiDefinitionModel();
        addContract(apiDefinitionModel);
        when(apiDefinitionDao.find(definitionId)).thenReturn(apiDefinitionModel);

        apiDefinitionService.deleteDefinition(definitionId);

        for (ApiContractModel contractModel : apiDefinitionModel.getApiContractModels()) {
            Mockito.verify(apiContractDao).update(contractModel);
        }
        Mockito.verify(apiDefinitionDao).delete(definitionId);
    }

    @Test
    public void testValidateAgainstSpecifications() {
        ApiDefinitionModel apiDefinitionModel = setupApiDefinitionModel();
        when(apiDefinitionDao.find(definitionId)).thenReturn(apiDefinitionModel);

        ApiSpecificationModel apiSpecificationModel = setupApiSpecificationModel();
        when(apiSpecificationDao.find(specificationId)).thenReturn(apiSpecificationModel);

        ValidationResult result = apiDefinitionService.validateAgainstSpecifications(definitionId, Collections.singletonList(specificationId));
        assertEquals(ValidationResultCategory.CORRECT, result.getValidationResultCategory());
    }

    @Test
    public void testValidateAgainstAllSpecifications() {
        ApiDefinitionModel apiDefinitionModel = setupApiDefinitionModel();
        when(apiDefinitionDao.find(definitionId)).thenReturn(apiDefinitionModel);

        ApiSpecificationModel apiSpecificationModel = setupApiSpecificationModel();
        when(apiSpecificationDao.find(specificationId)).thenReturn(apiSpecificationModel);

        ApiContractModel apiContractModel = setupContractModel();
        when(apiContractDao.find(contractId)).thenReturn(apiContractModel);

        ValidationResult result = apiDefinitionService.validateAgainstAllSpecifications(definitionId);
        assertEquals(ValidationResultCategory.CORRECT, result.getValidationResultCategory());
    }

    private ApiDefinitionModel setupApiDefinitionModel() {
        ApiDefinitionModel apiDefinitionModel = new ApiDefinitionModel();
        apiDefinitionModel.setId(definitionId);
        apiDefinitionModel.setName(definitionName);
        apiDefinitionModel.setType(definitionType);
        apiDefinitionModel.setContent(content);
        addContract(apiDefinitionModel);
        return apiDefinitionModel;
    }

    private void addContract(ApiDefinitionModel apiDefinitionModel) {
        ApiContractModel apiContractModel = setupContractModel();
        if (apiDefinitionModel.getApiContractModels() == null) {
            apiDefinitionModel.setApiContractModels(new ArrayList<>());
        }
        apiDefinitionModel.getApiContractModels().add(apiContractModel);
    }

    private String getContent() {
        ClassLoader classLoader = SwaggerApiSpecification.class.getClassLoader();
        File file = new File(classLoader.getResource(testFilepath).getFile());
        String content;
        try {
            content = FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new ApicatSystemException(ErrorCode.READ_FILE_EXCEPTION, e.getMessage());
        }
        return content;
    }

    private ApiContractModel setupContractModel() {
        ApiContractModel model = new ApiContractModel();
        model.setId(contractId);

        ApiDefinitionModel apiDefinitionModel = new ApiDefinitionModel();
        apiDefinitionModel.setId(definitionId);
        model.setApiDefinitionModel(apiDefinitionModel);

        ApiSpecificationModel apiSpecificationModel = new ApiSpecificationModel();
        apiSpecificationModel.setId(specificationId);
        model.setApiSpecificationModel(apiSpecificationModel);

        return model;
    }

    private ApiSpecificationModel setupApiSpecificationModel() {
        ApiSpecificationModel apiSpecificationModel = new ApiSpecificationModel();
        apiSpecificationModel.setId(specificationId);
        apiSpecificationModel.setType(SwaggerApiSpecification.TYPE);
        apiSpecificationModel.setContent(content);
        return apiSpecificationModel;
    }
}
