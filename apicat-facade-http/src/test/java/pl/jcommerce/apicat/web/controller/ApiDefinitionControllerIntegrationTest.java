package pl.jcommerce.apicat.web.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.entity.ApiSpecificationEntity;
import pl.jcommerce.apicat.model.model.ApiFormat;
import pl.jcommerce.apicat.model.repository.ApiContractRepository;
import pl.jcommerce.apicat.model.repository.ApiDefinitionRepository;
import pl.jcommerce.apicat.web.IntegrationTest;
import pl.jcommerce.apicat.web.dto.ApiDefinitionDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by jada on 05.12.2016.
 */

public class ApiDefinitionControllerIntegrationTest extends IntegrationTest {

    private final String FIRST_DEFINITION_CONTENT = getValidSwaggerDefinition();
    private final String FIRST_DEFINITION_NAME = "Definition name 1";
    private final String FIRST_DEFINITION_AUTHOR = "John Doe";
    private final String FIRST_DEFINITION_VERSION = "v1.1.1";

    private final String SECOND_DEFINITION_CONTENT = getValidSwaggerDefinition();
    private final String SECOND_DEFINITION_NAME = "Definition name 2";
    private final String SECOND_DEFINITION_AUTHOR = "Uncle Bob";
    private final String SECOND_DEFINITION_VERSION = "v2.1.7";

    private final String NEW_DEFINITION_NAME = "lorem ipsum";
    private final String NEW_DEFINITION_AUTHOR = "api cat";
    private final String NEW_DEFINITION_CONTENT = getModifiedValidSwaggerDefinition();
    private final String NEW_DEFINITION_VERSION = "5.5646546";

    @Autowired
    private ApiDefinitionRepository apiDefinitionRepository;

    @Autowired
    private ApiContractRepository contractRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private ApiDefinitionEntity definitionEntityWithoutContract;
    private ApiDefinitionEntity definitionEntityWithContract;
    private ApiContractEntity contract;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        definitionEntityWithoutContract = getApiDefinitionEntity(FIRST_DEFINITION_AUTHOR, FIRST_DEFINITION_NAME, FIRST_DEFINITION_CONTENT,
                FIRST_DEFINITION_VERSION);
        definitionEntityWithContract = getApiDefinitionEntity(SECOND_DEFINITION_AUTHOR, SECOND_DEFINITION_NAME, SECOND_DEFINITION_CONTENT,
                SECOND_DEFINITION_VERSION);

        apiDefinitionRepository.save(Arrays.asList(definitionEntityWithoutContract, definitionEntityWithContract));

        ApiSpecificationEntity specification = new ApiSpecificationEntity();
        specification.setAuthor("spec author");
        specification.setContent("spec content");
        specification.setName("spec name");
        specification.setVersion("spec version");

        contract = new ApiContractEntity();
        contract.setDefinition(definitionEntityWithContract);
        contract.setSpecification(specification);

        ApiContractValidationDetailsEntity validationDetails = new ApiContractValidationDetailsEntity();
        validationDetails.setValid(true);
        validationDetails.setContract(contract);

        contract.setValidationDetails(validationDetails);

        contractRepository.save(contract);

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void tearDown() throws Exception {
        apiDefinitionRepository.deleteAll();
    }

    @Test
    public void shouldReturnApiDefinitionById() throws Exception {
        mockMvc.perform(get("/api/definitions/" + definitionEntityWithoutContract.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("id", is(definitionEntityWithoutContract.getId().intValue())))
                .andExpect(jsonPath("name", is(definitionEntityWithoutContract.getName())))
                .andExpect(jsonPath("content", is(definitionEntityWithoutContract.getContent())))
                .andExpect(jsonPath("version", is(definitionEntityWithoutContract.getVersion())))
                .andExpect(jsonPath("author", is(definitionEntityWithoutContract.getAuthor())));
    }

    @Test
    public void shouldReturnAllDefinitionsPage() throws Exception {
        mockMvc.perform(get("/api/definitions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("size", instanceOf(Integer.class)))
                .andExpect(jsonPath("number", equalTo(0)))
                .andExpect(jsonPath("last", equalTo(true)))
                .andExpect(jsonPath("totalPages", equalTo(1)))
                .andExpect(jsonPath("totalElements", equalTo(2)))
                .andExpect(jsonPath("content", hasSize(2)))
                .andExpect(jsonPath("content[*].name", containsInAnyOrder(FIRST_DEFINITION_NAME, SECOND_DEFINITION_NAME)))
                .andExpect(jsonPath("content[*].id", containsInAnyOrder(definitionEntityWithoutContract.getId().intValue(),
                        definitionEntityWithContract.getId().intValue())));
    }

    @Test
    public void shouldReturnResourceNotFoundErrorWhileFetchingOneById() throws Exception {
        apiDefinitionRepository.delete(definitionEntityWithoutContract);

        mockMvc.perform(get("/api/definitions/" + definitionEntityWithoutContract.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateApiDefinition() throws Exception {
        ApiDefinitionDto modifiedDto = getModifiedApiDefinitionWithProvidedId(definitionEntityWithoutContract.getId());

        mockMvc.perform(
                put("/api/definitions/" + modifiedDto.getId())
                        .content(toJson(modifiedDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(modifiedDto.getId().intValue())))
                .andExpect(jsonPath("name", is(modifiedDto.getName())))
                .andExpect(jsonPath("content", is(modifiedDto.getContent())))
                .andExpect(jsonPath("version", is(modifiedDto.getVersion())))
                .andExpect(jsonPath("author", is(modifiedDto.getAuthor())));

        ApiDefinitionEntity modifiedEntity = apiDefinitionRepository.findOne(modifiedDto.getId());

        assertThat(modifiedEntity.getName(), is(NEW_DEFINITION_NAME));
    }

    @Transactional
    @Test
    public void shouldUpdateApiDefinitionWithContract() throws Exception {
        entityManager.refresh(definitionEntityWithContract);

        ApiDefinitionDto modifiedDto = getModifiedApiDefinitionWithProvidedId(definitionEntityWithContract.getId());

        mockMvc.perform(
                put("/api/definitions/" + modifiedDto.getId())
                        .content(toJson(modifiedDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(modifiedDto.getId().intValue())))
                .andExpect(jsonPath("name", is(modifiedDto.getName())))
                .andExpect(jsonPath("content", is(modifiedDto.getContent())))
                .andExpect(jsonPath("version", is(modifiedDto.getVersion())))
                .andExpect(jsonPath("author", is(modifiedDto.getAuthor())));

        ApiDefinitionEntity modifiedEntity = apiDefinitionRepository.findOne(modifiedDto.getId());

        assertThat(modifiedEntity.getName(), is(NEW_DEFINITION_NAME));
        assertThat(modifiedEntity.getAuthor(), is(NEW_DEFINITION_AUTHOR));
        assertThat(modifiedEntity.getContent(), is(NEW_DEFINITION_CONTENT));
        assertThat(modifiedEntity.getVersion(), is(NEW_DEFINITION_VERSION));
        assertThat(modifiedEntity.getContracts(), hasSize(1));
        assertThat(modifiedEntity.getContracts().iterator().next().getId(), is(contract.getId()));
    }

    @Test
    public void shouldRespondWithStatus400WhileUpdatingApiDefinitionWithInvalidContent() throws Exception {
        ApiDefinitionDto modifiedDto = getModifiedApiDefinitionWithProvidedId(definitionEntityWithContract.getId());
        modifiedDto.setContent(getInvalidSwaggerDefinition());

        mockMvc.perform(
                put("/api/definitions/" + modifiedDto.getId())
                        .content(toJson(modifiedDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnResourceNotFoundErrorWhileUpdating() throws Exception {
        Long id = definitionEntityWithoutContract.getId();
        apiDefinitionRepository.delete(id);

        String modifiedDefinitionJson = toJson(getModifiedApiDefinitionWithProvidedId(id));

        mockMvc.perform(
                put("/api/definitions/" + id)
                        .content(modifiedDefinitionJson)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddApiDefinition() throws Exception {
        ApiDefinitionDto newApiDefinitionDto = getNewApiDefinition();

        apiDefinitionRepository.deleteAll();
        assertThat(apiDefinitionRepository.count(), is(0L));

        ResultActions result = mockMvc.perform(
                post("/api/definitions/")
                        .content(toJson(newApiDefinitionDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8));

        assertThat(apiDefinitionRepository.count(), is(1L));
        ApiDefinitionEntity createdEntity = apiDefinitionRepository.findAll().get(0);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("id", is(createdEntity.getId().intValue())))
                .andExpect(jsonPath("name", is(newApiDefinitionDto.getName())))
                .andExpect(jsonPath("content", is(newApiDefinitionDto.getContent())))
                .andExpect(jsonPath("version", is(newApiDefinitionDto.getVersion())))
                .andExpect(jsonPath("author", is(newApiDefinitionDto.getAuthor())));

        assertThat(createdEntity.getName(), is(newApiDefinitionDto.getName()));
    }

    @Test
    public void shouldRespondWithStatus400WhileAddingInvalidApiDefinition() throws Exception {
        ApiDefinitionDto newApiDefinitionDto = getNewApiDefinition();
        newApiDefinitionDto.setContent(getInvalidSwaggerDefinition());

        apiDefinitionRepository.deleteAll();
        assertThat(apiDefinitionRepository.count(), is(0L));

        mockMvc.perform(
                post("/api/definitions/")
                        .content(toJson(newApiDefinitionDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldDeleteApiDefinition() throws Exception {
        Long definitionsCount = apiDefinitionRepository.count();

        assertThat(definitionsCount, greaterThan(0L));

        mockMvc.perform(delete("/api/definitions/" + definitionEntityWithoutContract.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(definitionEntityWithoutContract.getId().intValue())))
                .andExpect(jsonPath("name", is(definitionEntityWithoutContract.getName())))
                .andExpect(jsonPath("content", is(definitionEntityWithoutContract.getContent())))
                .andExpect(jsonPath("version", is(definitionEntityWithoutContract.getVersion())))
                .andExpect(jsonPath("author", is(definitionEntityWithoutContract.getAuthor())));

        assertThat(apiDefinitionRepository.count(), is((definitionsCount - 1)));

        assertThat(apiDefinitionRepository.findOne(definitionEntityWithoutContract.getId()), nullValue());
    }

    @Transactional
    @Test
    public void shouldDeleteApiDefinitionWithContract() throws Exception {
        Long definitionsCount = apiDefinitionRepository.count();

        assertThat(definitionsCount, greaterThan(0L));

        entityManager.refresh(definitionEntityWithContract);

        assertThat(definitionEntityWithContract.getContracts(), hasSize(1));

        ApiContractEntity contract = definitionEntityWithContract.getContracts().iterator().next();

        mockMvc.perform(delete("/api/definitions/" + definitionEntityWithContract.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(definitionEntityWithContract.getId().intValue())))
                .andExpect(jsonPath("name", is(definitionEntityWithContract.getName())))
                .andExpect(jsonPath("content", is(definitionEntityWithContract.getContent())))
                .andExpect(jsonPath("version", is(definitionEntityWithContract.getVersion())))
                .andExpect(jsonPath("author", is(definitionEntityWithContract.getAuthor())));

        assertThat(apiDefinitionRepository.count(), is((definitionsCount - 1)));

        assertThat(apiDefinitionRepository.findOne(definitionEntityWithContract.getId()), nullValue());

        assertThat(contractRepository.findOne(contract.getId()), nullValue());
    }

    @Test
    public void shouldReturnResourceNotFoundErrorWhileDeleting() throws Exception {
        apiDefinitionRepository.delete(definitionEntityWithoutContract);

        mockMvc.perform(delete("/api/definitions/" + definitionEntityWithoutContract.getId()))
                .andExpect(status().isNotFound());
    }

    private ApiDefinitionDto getModifiedApiDefinitionWithProvidedId(Long id) {
        ApiDefinitionDto dto = new ApiDefinitionDto();
        dto.setId(id);
        dto.setName(NEW_DEFINITION_NAME);
        dto.setVersion(NEW_DEFINITION_VERSION);
        dto.setAuthor(NEW_DEFINITION_AUTHOR);
        dto.setContent(NEW_DEFINITION_CONTENT);
        dto.setFormat(ApiFormat.SWAGGER);

        return dto;
    }

    private ApiDefinitionDto getNewApiDefinition() {
        ApiDefinitionDto dto = new ApiDefinitionDto();
        dto.setName(NEW_DEFINITION_NAME);
        dto.setVersion(NEW_DEFINITION_VERSION);
        dto.setAuthor(NEW_DEFINITION_AUTHOR);
        dto.setContent(NEW_DEFINITION_CONTENT);
        dto.setFormat(ApiFormat.SWAGGER);

        return dto;
    }

    private ApiDefinitionEntity getApiDefinitionEntity(String author, String name, String content, String version) {
        ApiDefinitionEntity entity = new ApiDefinitionEntity();
        entity.setName(name);
        entity.setAuthor(author);
        entity.setContent(content);
        entity.setVersion(version);
        entity.setFormat(ApiFormat.SWAGGER);

        return entity;
    }

    private String getValidSwaggerDefinition() {
        return getSwaggerDefinitionFromFile("providerContract.yaml");
    }

    private String getModifiedValidSwaggerDefinition() {
        return getSwaggerDefinitionFromFile("providerContractWithModifications.yaml");
    }

    private String getInvalidSwaggerDefinition() {
        return getSwaggerDefinitionFromFile("inconsistentProviderContract.yaml");
    }
}