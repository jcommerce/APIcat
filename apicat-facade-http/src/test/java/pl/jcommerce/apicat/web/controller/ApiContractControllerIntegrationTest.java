package pl.jcommerce.apicat.web.controller;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.entity.ApiSpecificationEntity;
import pl.jcommerce.apicat.model.model.ApiFormat;
import pl.jcommerce.apicat.model.repository.ApiContractRepository;
import pl.jcommerce.apicat.model.repository.ApiDefinitionRepository;
import pl.jcommerce.apicat.model.repository.ApiSpecificationRepository;
import pl.jcommerce.apicat.web.IntegrationTest;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by jada on 08.12.2016.
 */

@Transactional
public class ApiContractControllerIntegrationTest extends IntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private ApiDefinitionRepository definitionRepository;

    @Autowired
    private ApiSpecificationRepository specificationRepository;

    @Autowired
    private ApiContractRepository contractRepository;

    private ApiContractEntity contract;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        ApiDefinitionEntity definition = new ApiDefinitionEntity();
        definition.setContent("def content");
        definition.setVersion("def version");
        definition.setAuthor("def author");
        definition.setName("def name");
        definition.setFormat(ApiFormat.SWAGGER);

        definitionRepository.save(definition);

        ApiSpecificationEntity specification = new ApiSpecificationEntity();
        specification.setAuthor("spec author");
        specification.setContent("spec content");
        specification.setName("spec name");
        specification.setVersion("spec version");

        specificationRepository.save(specification);

        contract = new ApiContractEntity();
        contract.setDefinition(definition);
        contract.setSpecification(specification);

        ApiContractValidationDetailsEntity validationDetails = new ApiContractValidationDetailsEntity();
        validationDetails.setValid(true);
        validationDetails.setContract(contract);

        contract.setValidationDetails(validationDetails);

        contractRepository.save(contract);
    }

    @Test
    public void shouldReturnContract() throws Exception {
        mockMvc.perform(get("/api/contracts/" + contract.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("definitionId", is(contract.getDefinition().getId().intValue())))
                .andExpect(jsonPath("specificationId", is(contract.getSpecification().getId().intValue())));

    }

    @Test
    public void shouldReturnInvalidContractValidationDetails() throws Exception {
        Set<String> differences = Sets.newHashSet("a", "b", "c");

        contract.getValidationDetails().setValid(false);
        contract.getValidationDetails().setDifferences(differences);

        contractRepository.save(contract);

        mockMvc.perform(get("/api/contracts/" + contract.getId() + "/validationDetails"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("id", instanceOf(Number.class)))
                .andExpect(jsonPath("contractId", is(contract.getId().intValue())))
                .andExpect(jsonPath("valid", is(false)))
                .andExpect(jsonPath("differences[*]", hasSize(differences.size())));
    }

    @Test
    public void shouldReturnValidContractValidationDetails() throws Exception {
        mockMvc.perform(get("/api/contracts/" + contract.getId() + "/validationDetails"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("id", instanceOf(Number.class)))
                .andExpect(jsonPath("contractId", is(contract.getId().intValue())))
                .andExpect(jsonPath("valid", is(true)))
                .andExpect(jsonPath("differences[*]", hasSize(0)));
    }

    @Test
    public void shouldReturn404WhileFetchingNonExistingContract() throws Exception {
        mockMvc.perform(get("/api/contracts/" + 999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhileFetchingNonExistingContractValidationStatus() throws Exception {
        mockMvc.perform(get("/api/contracts/" + 999 + "/validationDetails"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnAllContracts() throws Exception {
        mockMvc.perform(get("/api/contracts/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("size", instanceOf(Integer.class)))
                .andExpect(jsonPath("number", equalTo(0)))
                .andExpect(jsonPath("last", equalTo(true)))
                .andExpect(jsonPath("totalPages", equalTo(1)))
                .andExpect(jsonPath("totalElements", equalTo(1)))
                .andExpect(jsonPath("content", hasSize(1)));
    }
}
