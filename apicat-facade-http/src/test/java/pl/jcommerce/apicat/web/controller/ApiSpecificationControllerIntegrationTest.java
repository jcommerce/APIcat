package pl.jcommerce.apicat.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jcommerce.apicat.business.service.ApiSpecificationService;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.entity.ApiSpecificationEntity;
import pl.jcommerce.apicat.model.model.ApiFormat;
import pl.jcommerce.apicat.model.repository.ApiContractRepository;
import pl.jcommerce.apicat.model.repository.ApiDefinitionRepository;
import pl.jcommerce.apicat.model.repository.ApiSpecificationRepository;
import pl.jcommerce.apicat.web.IntegrationTest;
import pl.jcommerce.apicat.web.dto.ApiSpecificationDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by jada on 05.12.2016.
 */

@Transactional
public class ApiSpecificationControllerIntegrationTest extends IntegrationTest {

    private final String SAMPLE_SPECIFICATION_CONTENT = getValidSwaggerSpecification();
    private final String SAMPLE_SPECIFICATION_NAME = "qwerty";
    private final String SAMPLE_SPECIFICATION_AUTHOR = "Donald duck";
    private final String SAMPLE_SPECIFICATION_VERSION = "v5.7";

    private final String MODIFIED_SPECIFICATION_CONTENT = getModifiedValidSwaggerSpecification();

    @Autowired
    private ApiDefinitionRepository definitionRepository;

    @Autowired
    private ApiSpecificationRepository specificationRepository;

    @Autowired
    private ApiSpecificationService specificationService;

    @Autowired
    private ApiContractRepository contractRepository;

    private MockMvc mockMvc;

    private ApiDefinitionEntity existingDefinitionEntity;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        existingDefinitionEntity = getDefinitionEntity();
        definitionRepository.save(existingDefinitionEntity);
    }

    @Test
    public void shouldCreateApiSpecificationAndContract() throws Exception {
        assertThat(specificationRepository.count(), is(0L));
        assertThat(contractRepository.count(), is(0L));

        ApiSpecificationDto newApiSpecification = getSampleSpecificationDto();

        ResultActions result = mockMvc.perform(
                post("/api/definitions/" + existingDefinitionEntity.getId() + "/specifications")
                        .content(toJson(newApiSpecification))
                        .contentType(MediaType.APPLICATION_JSON_UTF8));

        assertThat(specificationRepository.count(), is(1L));
        assertThat(contractRepository.count(), is(1L));

        ApiSpecificationEntity createdSpecification = specificationRepository.findAll().get(0);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("id", is(createdSpecification.getId().intValue())))
                .andExpect(jsonPath("name", is(newApiSpecification.getName())))
                .andExpect(jsonPath("content", is(newApiSpecification.getContent())))
                .andExpect(jsonPath("version", is(newApiSpecification.getVersion())))
                .andExpect(jsonPath("author", is(newApiSpecification.getAuthor())));

        assertThat(createdSpecification.getName(), is(newApiSpecification.getName()));
        assertThat(createdSpecification.getAuthor(), is(newApiSpecification.getAuthor()));
        assertThat(createdSpecification.getContent(), is(newApiSpecification.getContent()));
        assertThat(createdSpecification.getVersion(), is(newApiSpecification.getVersion()));

        ApiContractEntity createdContract = contractRepository.findAll().get(0);
        assertThat(createdContract.getDefinition(), is(existingDefinitionEntity));
        assertThat(createdContract.getSpecification(), is(createdSpecification));
        assertThat(createdContract.getValidationDetails().isValid(), is(true));
        assertThat(createdContract.getValidationDetails().getDifferences(), hasSize(0));
    }

    @Test
    public void shouldCreateApiSpecificationAndContractWithInvalidValidationStatus() throws Exception {
        ApiSpecificationDto newApiSpecification = getSampleSpecificationDto();
        newApiSpecification.setContent(getInvalidSwaggerSpecification());

        mockMvc.perform(post("/api/definitions/" + existingDefinitionEntity.getId() + "/specifications")
                .content(toJson(newApiSpecification))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        assertThat(specificationRepository.count(), is(1L));
        assertThat(contractRepository.count(), is(1L));

        ApiSpecificationEntity createdSpecification = specificationRepository.findAll().get(0);

        ApiContractEntity createdContract = contractRepository.findAll().get(0);
        assertThat(createdContract.getDefinition(), is(existingDefinitionEntity));
        assertThat(createdContract.getSpecification(), is(createdSpecification));
        assertThat(createdContract.getValidationDetails().isValid(), is(false));
        assertThat(createdContract.getValidationDetails().getDifferences(), hasSize(greaterThan(0)));
    }

    @Test
    public void shouldReturnPageWithTwoSpecifications() throws Exception {
        specificationService.addApiSpecification(getSampleSpecificationEntity(), existingDefinitionEntity.getId());
        specificationService.addApiSpecification(getSampleSpecificationEntity(), existingDefinitionEntity.getId());

        mockMvc.perform(get("/api/definitions/" + existingDefinitionEntity.getId() + "/specifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("size", instanceOf(Integer.class)))
                .andExpect(jsonPath("number", equalTo(0)))
                .andExpect(jsonPath("last", equalTo(true)))
                .andExpect(jsonPath("totalPages", equalTo(1)))
                .andExpect(jsonPath("totalElements", equalTo(2)))
                .andExpect(jsonPath("content", hasSize(2)));
    }

    @Test
    public void shouldReturnEmptySpecificationsPage() throws Exception {
        mockMvc.perform(get("/api/definitions/" + existingDefinitionEntity.getId() + "/specifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("size", instanceOf(Integer.class)))
                .andExpect(jsonPath("number", equalTo(0)))
                .andExpect(jsonPath("last", equalTo(true)))
                .andExpect(jsonPath("totalPages", equalTo(0)))
                .andExpect(jsonPath("totalElements", equalTo(0)))
                .andExpect(jsonPath("content", hasSize(0)));
    }

    @Test
    public void shouldReturnNotFoundErrorCodeWhileFetchingSpecifications() throws Exception {
        mockMvc.perform(get("/api/definitions/" + 999 + "/specifications"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnSpecificationDetails() throws Exception {
        ApiSpecificationEntity specification = specificationService.addApiSpecification(getSampleSpecificationEntity(),
                existingDefinitionEntity.getId());

        mockMvc.perform(get("/api/definitions/" + existingDefinitionEntity.getId() + "/specifications/" + specification.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("id", is(specification.getId().intValue())))
                .andExpect(jsonPath("name", is(specification.getName())))
                .andExpect(jsonPath("content", is(specification.getContent())))
                .andExpect(jsonPath("version", is(specification.getVersion())))
                .andExpect(jsonPath("author", is(specification.getAuthor())));
    }

    @Test
    public void shouldDeleteSpecification() throws Exception {
        ApiSpecificationEntity specification = specificationService.addApiSpecification(getSampleSpecificationEntity(),
                existingDefinitionEntity.getId());

        mockMvc.perform(delete("/api/definitions/" + existingDefinitionEntity.getId() + "/specifications/" + specification.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("id", is(specification.getId().intValue())))
                .andExpect(jsonPath("name", is(specification.getName())))
                .andExpect(jsonPath("content", is(specification.getContent())))
                .andExpect(jsonPath("version", is(specification.getVersion())))
                .andExpect(jsonPath("author", is(specification.getAuthor())));

        assertThat(specificationRepository.findOne(specification.getId()), nullValue());
    }

    @Test
    public void shouldReturn404WhileDeletingNonExistingSpecification() throws Exception {
        Long nonExistingId = 9999L;

        assertThat(specificationRepository.exists(nonExistingId), is(false));

        mockMvc.perform(delete("/api/definitions/" + existingDefinitionEntity.getId() + "/specifications/" + nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateApiSpecification() throws Exception {
        ApiSpecificationEntity createdSpecification = specificationService.addApiSpecification(getSampleSpecificationEntity(),
                existingDefinitionEntity.getId());

        assertThat(createdSpecification.getContent(), is(SAMPLE_SPECIFICATION_CONTENT));
        assertThat(SAMPLE_SPECIFICATION_CONTENT, not(MODIFIED_SPECIFICATION_CONTENT));

        ApiSpecificationDto dto = getSampleSpecificationDto();
        dto.setId(createdSpecification.getId());
        dto.setContent(MODIFIED_SPECIFICATION_CONTENT);

        mockMvc.perform(
                put("/api/definitions/" + existingDefinitionEntity.getId() + "/specifications/" + createdSpecification.getId())
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(dto.getId().intValue())))
                .andExpect(jsonPath("name", is(dto.getName())))
                .andExpect(jsonPath("content", is(dto.getContent())))
                .andExpect(jsonPath("version", is(dto.getVersion())))
                .andExpect(jsonPath("author", is(dto.getAuthor())));

        ApiSpecificationEntity modifiedSpecification = specificationRepository.findOne(dto.getId());

        assertThat(modifiedSpecification.getContent(), is(MODIFIED_SPECIFICATION_CONTENT));

        assertThat(modifiedSpecification.getContract().getId(), is(createdSpecification.getContract().getId()));
        assertThat(modifiedSpecification.getName(), is(SAMPLE_SPECIFICATION_NAME));
        assertThat(modifiedSpecification.getAuthor(), is(SAMPLE_SPECIFICATION_AUTHOR));
        assertThat(modifiedSpecification.getVersion(), is(SAMPLE_SPECIFICATION_VERSION));
    }

    @Test
    public void shouldReturn404WhileUpdatingNonExistingSpecification() throws Exception {
        Long nonExistingId = 9999L;

        assertThat(specificationRepository.exists(nonExistingId), is(false));

        ApiSpecificationDto dto = getSampleSpecificationDto();
        dto.setId(nonExistingId);

        mockMvc.perform(
                put("/api/definitions/" + existingDefinitionEntity.getId() + "/specifications/" + nonExistingId)
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    private ApiSpecificationDto getSampleSpecificationDto() {
        ApiSpecificationDto dto = new ApiSpecificationDto();
        dto.setContent(SAMPLE_SPECIFICATION_CONTENT);
        dto.setName(SAMPLE_SPECIFICATION_NAME);
        dto.setAuthor(SAMPLE_SPECIFICATION_AUTHOR);
        dto.setVersion(SAMPLE_SPECIFICATION_VERSION);

        return dto;
    }

    private ApiDefinitionEntity getDefinitionEntity() {
        ApiDefinitionEntity entity = new ApiDefinitionEntity();
        entity.setContent(getValidSwaggerDefinition());
        entity.setVersion("1.1");
        entity.setAuthor("James Bond");
        entity.setName("api name");
        entity.setFormat(ApiFormat.SWAGGER);

        return entity;
    }

    private ApiSpecificationEntity getSampleSpecificationEntity() {
        ApiSpecificationEntity entity = new ApiSpecificationEntity();
        entity.setContent(SAMPLE_SPECIFICATION_CONTENT);
        entity.setName(SAMPLE_SPECIFICATION_NAME);
        entity.setAuthor(SAMPLE_SPECIFICATION_AUTHOR);
        entity.setVersion(SAMPLE_SPECIFICATION_VERSION);

        return entity;
    }

    private String getValidSwaggerDefinition() {
        return getSwaggerDefinitionFromFile("providerContract.yaml");
    }

    private String getValidSwaggerSpecification() {
        return getSwaggerDefinitionFromFile("sameConsumerContract.yaml");
    }

    private String getModifiedValidSwaggerSpecification() {
        return getSwaggerDefinitionFromFile("consumerContractWithoutEndpoint.yaml");
    }

    private String getInvalidSwaggerSpecification() {
        return getSwaggerDefinitionFromFile("consumerContractWithWrongMetadata.yaml");
    }
}