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
import pl.jcommerce.apicat.web.dto.ApiContractDto;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Created by jada on 09.12.2016.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiContractMapperTest {

    private static final long SPECIFICATION_ID = 364L;
    private static final long DEFINITION_ID = 778L;
    private static final long VALIDATION_DETAILS_ID = 889L;
    private static final long CONTRACT_ID = 657468L;

    @Autowired
    private ApiContractMapper apiContractMapper;

    @Test
    public void shouldConvertEntityToDto() {
        ApiContractDto dto = apiContractMapper.getDtoFromEntity(getApiContractEntity());

        assertThat(dto.getId(), is(CONTRACT_ID));
        assertThat(dto.getDefinitionId(), is(DEFINITION_ID));
        assertThat(dto.getSpecificationId(), is(SPECIFICATION_ID));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldConvertDtoToEntity() {
        apiContractMapper.getEntityFromDto(getApiContractDto());
    }

    public ApiContractEntity getApiContractEntity() {
        ApiContractEntity contract = new ApiContractEntity();

        ApiSpecificationEntity specification = new ApiSpecificationEntity();
        specification.setId(SPECIFICATION_ID);

        ApiDefinitionEntity definitionEntity = new ApiDefinitionEntity();
        definitionEntity.setId(DEFINITION_ID);

        ApiContractValidationDetailsEntity validationDetails = new ApiContractValidationDetailsEntity();
        validationDetails.setValid(true);
        validationDetails.setId(VALIDATION_DETAILS_ID);
        validationDetails.setContract(contract);

        contract.setId(CONTRACT_ID);
        contract.setValidationDetails(validationDetails);
        contract.setSpecification(specification);
        contract.setDefinition(definitionEntity);

        return contract;
    }

    public ApiContractDto getApiContractDto() {
        ApiContractDto dto = new ApiContractDto();
        dto.setId(1L);
        dto.setDefinitionId(2L);
        dto.setSpecificationId(3L);

        return dto;
    }
}