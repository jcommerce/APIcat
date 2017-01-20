package pl.jcommerce.apicat.service;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.exception.ObjectNotFoundException;
import pl.jcommerce.apicat.model.ApiContractModel;
import pl.jcommerce.apicat.model.ApiSpecificationModel;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationCreateDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationUpdateDto;
import pl.jcommerce.apicat.service.apispecification.impl.ApiSpecificationServiceImpl;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by luwa on 18.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiSpecificationServiceTest {

    @InjectMocks
    private ApiSpecificationServiceImpl apiSpecificationService;

    @Mock
    private ApiSpecificationDao apiSpecificationDao;

    @Mock
    private ApiContractDao apiContractDao;

    private final Long specificationId = 1L;
    private final Long contractId = 2L;
    private final String specificationName = "Test";
    private final String specificationType = SwaggerApiSpecification.TYPE;
    private final String testFilepath = "consumerContract.yaml";
    private final String content = getContent();

    @Test
    public void createSpecification() {
        ApiSpecificationModel apiSpecificationModel = setupApiSpecificationModel();
        when(apiSpecificationDao.create(any(ApiSpecificationModel.class))).thenReturn(apiSpecificationModel);

        ApiSpecificationCreateDto apiSpecificationCreateDto = new ApiSpecificationCreateDto();
        apiSpecificationCreateDto.setName(specificationName);
        apiSpecificationCreateDto.setType(specificationType);

        byte[] bytes = content.getBytes();

        Long returnedContractId = apiSpecificationService.createSpecification(apiSpecificationCreateDto, bytes);
        assertEquals(specificationId, returnedContractId);
    }

    @Test
    public void getSpecification() {
        ApiSpecificationModel apiSpecificationModel = setupApiSpecificationModel();
        when(apiSpecificationDao.find(specificationId)).thenReturn(apiSpecificationModel);

        ApiSpecificationDto apiSpecificationDto = apiSpecificationService.getSpecification(specificationId);
        assertEquals(specificationId, apiSpecificationDto.getId());
        assertEquals(specificationName, apiSpecificationDto.getName());
        assertEquals(content, apiSpecificationDto.getData());
        assertEquals(contractId, apiSpecificationDto.getContractId());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateSpecificationContractNotFound() {
        ApiSpecificationModel apiSpecificationModel = setupApiSpecificationModel();
        when(apiSpecificationDao.find(specificationId)).thenReturn(apiSpecificationModel);

        ApiContractModel apiContractModel = new ApiContractModel();
        apiContractModel.setId(contractId);
        when(apiContractDao.find(contractId)).thenReturn(apiContractModel);

        ApiSpecificationUpdateDto apiSpecificationUpdateDto = new ApiSpecificationUpdateDto();
        apiSpecificationUpdateDto.setName("Test2");
        apiSpecificationService.updateSpecification(specificationId, apiSpecificationUpdateDto);

        apiSpecificationUpdateDto.setContractId(3L);
        apiSpecificationService.updateSpecification(specificationId, apiSpecificationUpdateDto);

    }

    private ApiSpecificationModel setupApiSpecificationModel() {
        ApiContractModel apiContractModel = setupContractModel();

        ApiSpecificationModel apiSpecificationModel = new ApiSpecificationModel();
        apiSpecificationModel.setId(specificationId);
        apiSpecificationModel.setName(specificationName);
        apiSpecificationModel.setType(specificationType);
        apiSpecificationModel.setContent(content);
        apiSpecificationModel.setApiContractModel(apiContractModel);
        return apiSpecificationModel;
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
        return model;
    }
}
