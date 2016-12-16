package pl.jcommerce.apicat.web.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.repository.ApiDefinitionRepository;
import pl.jcommerce.apicat.web.dto.ApiDefinitionDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by jada on 07.12.2016.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiDefinitionMapperTest {

    private final Long CONTRACT_VALIDATION_DETAILS_ID = 59741L;
    private final Long CONTRACT_ID = 9992L;
    private final Long ID = 1234L;
    private final String AUTHOR = "author name";
    private final String CONTENT = "foo bar";
    private final String VERSION = "1.12.1231";
    private final String NAME = "ApiCat api";

    @Autowired
    private ApiDefinitionMapper apiDefinitionMapper;

    @MockBean
    private ApiDefinitionRepository definitionRepository;

    private final ApiContractEntity contractEntity = getApiContractEntity();


    @Test
    public void shouldConvertEntityToDto() {
        ApiDefinitionDto dto = apiDefinitionMapper.getDtoFromEntity(getApiDefinitionEntity());

        assertThat(dto.getAuthor(), is(AUTHOR));
        assertThat(dto.getContent(), is(CONTENT));
        assertThat(dto.getName(), is(NAME));
        assertThat(dto.getVersion(), is(VERSION));
        assertThat(dto.getId(), is(ID));
    }

    @Transactional
    @Test
    public void shouldConvertDtoToEntity() {
        given(definitionRepository.findOne(ID)).willReturn(getApiDefinitionEntity());

        ApiDefinitionEntity entity = apiDefinitionMapper.getEntityFromDto(getApiDefinitionDto());

        assertThat(entity.getId(), is(ID));
        assertThat(entity.getAuthor(), is(AUTHOR));
        assertThat(entity.getContent(), is(CONTENT));
        assertThat(entity.getName(), is(NAME));
        assertThat(entity.getVersion(), is(VERSION));
        assertThat(entity.getContracts(), hasSize(1));
        assertThat(entity.getContracts(), contains(contractEntity));
    }

    @Test
    public void shouldConvertDtoToEntityWithNullProperty() {
        ApiDefinitionDto dto = getApiDefinitionDto();
        dto.setName(null);

        ApiDefinitionEntity entity = apiDefinitionMapper.getEntityFromDto(dto);

        assertThat(entity.getId(), is(ID));
        assertThat(entity.getAuthor(), is(AUTHOR));
        assertThat(entity.getContent(), is(CONTENT));
        assertThat(entity.getName(), nullValue());
        assertThat(entity.getVersion(), is(VERSION));
    }

    @Test
    public void shouldConvertEntityToDtoWithNullProperty() {
        ApiDefinitionEntity entity = getApiDefinitionEntity();
        entity.setAuthor(null);
        entity.setId(null);

        ApiDefinitionDto dto = apiDefinitionMapper.getDtoFromEntity(entity);

        assertThat(dto.getAuthor(), nullValue());
        assertThat(dto.getContent(), is(CONTENT));
        assertThat(dto.getName(), is(NAME));
        assertThat(dto.getVersion(), is(VERSION));
        assertThat(dto.getId(), nullValue());
    }

    public ApiDefinitionEntity getApiDefinitionEntity() {
        ApiDefinitionEntity entity = new ApiDefinitionEntity();
        entity.setId(ID);
        entity.setAuthor(AUTHOR);
        entity.setContent(CONTENT);
        entity.setName(NAME);
        entity.setVersion(VERSION);
        entity.getContracts().add(contractEntity);

        return entity;
    }

    public ApiDefinitionDto getApiDefinitionDto() {
        ApiDefinitionDto entity = new ApiDefinitionDto();
        entity.setId(ID);
        entity.setAuthor(AUTHOR);
        entity.setContent(CONTENT);
        entity.setName(NAME);
        entity.setVersion(VERSION);

        return entity;
    }

    public ApiContractEntity getApiContractEntity() {
        ApiContractEntity contract = new ApiContractEntity();

        ApiContractValidationDetailsEntity validationDetails = new ApiContractValidationDetailsEntity();
        validationDetails.setValid(true);
        validationDetails.setId(CONTRACT_VALIDATION_DETAILS_ID);
        validationDetails.setContract(contract);

        contract.setId(CONTRACT_ID);
        contract.setValidationDetails(validationDetails);

        return contract;
    }
}