package pl.jcommerce.apicat.web.mapper;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;
import pl.jcommerce.apicat.web.dto.ApiContractValidationDetailsDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Created by jada on 12.12.2016.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiContractValidationDetailsMapperTest {

    private static final Long ID = 865L;
    private static final Long CONTRACT_ID = 77L;
    private static final String DIFF_A = "lorem";
    private static final String DIFF_B = "ipsum";

    @Autowired
    private ApiContractValidationDetailsMapper mapper;

    @Test
    public void shouldConvertEntityToDto() {
        ApiContractValidationDetailsDto dto = mapper.getDtoFromEntity(getValidationDetailsEntity());

        assertThat(dto.getId(), is(ID));
        assertThat(dto.getDifferences(), hasItems(DIFF_A, DIFF_B));
        assertThat(dto.getContractId(), is(CONTRACT_ID));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldConvertDtoToEntity() {
        mapper.getEntityFromDto(getValidationDetailsDto());
    }

    private ApiContractValidationDetailsDto getValidationDetailsDto() {
        ApiContractValidationDetailsDto dto = new ApiContractValidationDetailsDto();
        dto.setId(ID);
        dto.setValid(false);
        dto.setDifferences(Sets.newHashSet(DIFF_A, DIFF_B));
        dto.setContractId(CONTRACT_ID);

        return dto;
    }

    private ApiContractValidationDetailsEntity getValidationDetailsEntity() {
        ApiContractValidationDetailsEntity validationDetails = new ApiContractValidationDetailsEntity();
        validationDetails.setId(ID);
        validationDetails.setValid(false);
        validationDetails.setDifferences(Sets.newHashSet(DIFF_A, DIFF_B));

        ApiContractEntity contract = new ApiContractEntity();
        contract.setId(CONTRACT_ID);
        contract.setValidationDetails(validationDetails);

        validationDetails.setContract(contract);

        return validationDetails;
    }
}