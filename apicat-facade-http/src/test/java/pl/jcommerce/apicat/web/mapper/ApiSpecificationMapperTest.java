package pl.jcommerce.apicat.web.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.entity.ApiSpecificationEntity;
import pl.jcommerce.apicat.web.dto.ApiSpecificationDto;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Created by jada on 09.12.2016.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiSpecificationMapperTest {

    private static final long ENTITY_ID = 111L;
    private static final String ENTITY_NAME = "spec name";
    private static final String ENTITY_AUTHOR = "John Doe";
    private static final String ENTITY_CONTENT = "spec content";
    private static final String ENTITY_VERSION = "spec version";
    private static final long DTO_ID = 222L;
    private static final String DTO_CONTENT = "dto content";
    private static final String DTO_AUTHOR = "Paulo Coelho";
    private static final String DTO_VERSION = "87463";
    private static final String DTO_NAME = "foo bar";
    private static final long CONTRACT_VALIDATION_DETAILS_ID = 333L;
    private static final long CONTRACT_ID = 4L;
    private static final long DEFINITION_ID = 555L;

    @Autowired
    private ApiSpecificationMapper apiSpecificationMapper;

    @Test
    public void shouldConvertEntityToDto() {
        ApiSpecificationDto dto = apiSpecificationMapper.getDtoFromEntity(getApiSpecificationEntity());

        assertThat(dto.getId(), is(ENTITY_ID));
        assertThat(dto.getAuthor(), is(ENTITY_AUTHOR));
        assertThat(dto.getName(), is(ENTITY_NAME));
        assertThat(dto.getVersion(), is(ENTITY_VERSION));
        assertThat(dto.getContent(), is(ENTITY_CONTENT));
    }


    @Test
    public void shouldConvertDtoToEntity() {
        ApiSpecificationEntity entity = apiSpecificationMapper.getEntityFromDto(getApiSpecificationDto());

        assertThat(entity.getId(), is(DTO_ID));
        assertThat(entity.getAuthor(), is(DTO_AUTHOR));
        assertThat(entity.getName(), is(DTO_NAME));
        assertThat(entity.getVersion(), is(DTO_VERSION));
        assertThat(entity.getContent(), is(DTO_CONTENT));
        assertThat(entity.getContract(), nullValue());
    }

    private ApiSpecificationEntity getApiSpecificationEntity() {
        ApiContractEntity contract = getApiContractEntity();

        ApiSpecificationEntity entity = new ApiSpecificationEntity();
        entity.setId(ENTITY_ID);
        entity.setContent(ENTITY_CONTENT);
        entity.setVersion(ENTITY_VERSION);
        entity.setName(ENTITY_NAME);
        entity.setAuthor(ENTITY_AUTHOR);
        entity.setContract(contract);

        return entity;
    }

    private ApiSpecificationDto getApiSpecificationDto() {
        ApiSpecificationDto dto = new ApiSpecificationDto();
        dto.setId(DTO_ID);
        dto.setContent(DTO_CONTENT);
        dto.setAuthor(DTO_AUTHOR);
        dto.setVersion(DTO_VERSION);
        dto.setName(DTO_NAME);

        return dto;
    }

    public ApiContractEntity getApiContractEntity() {
        ApiContractEntity contract = new ApiContractEntity();

        ApiDefinitionEntity definitionEntity = new ApiDefinitionEntity();
        definitionEntity.setId(DEFINITION_ID);

        ApiContractValidationDetailsEntity validationDetails = new ApiContractValidationDetailsEntity();
        validationDetails.setValid(true);
        validationDetails.setId(CONTRACT_VALIDATION_DETAILS_ID);
        validationDetails.setContract(contract);

        contract.setId(CONTRACT_ID);
        contract.setValidationDetails(validationDetails);
        contract.setDefinition(definitionEntity);

        return contract;
    }

}