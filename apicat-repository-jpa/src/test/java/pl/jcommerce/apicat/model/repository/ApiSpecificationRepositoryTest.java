package pl.jcommerce.apicat.model.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.jcommerce.apicat.ModuleInitializer;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.model.entity.ApiSpecificationEntity;
import pl.jcommerce.apicat.model.model.ApiFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by jada on 12.12.2016.
 */
@SpringBootTest(classes = ModuleInitializer.class)
@RunWith(SpringRunner.class)
public class ApiSpecificationRepositoryTest {

    @Autowired
    private ApiSpecificationRepository specificationRepository;

    @Autowired
    private ApiContractRepository contractRepository;

    @Autowired
    private ApiDefinitionRepository definitionRepository;

    private ApiSpecificationEntity specification;
    private ApiDefinitionEntity definition;

    @Before
    public void setUp() throws Exception {
        definition = new ApiDefinitionEntity();
        definition.setName("definition name");
        definition.setContent("definition content");
        definition.setAuthor("definition author");
        definition.setVersion("definition version");
        definition.setFormat(ApiFormat.SWAGGER);

        definitionRepository.save(definition);

        specification = new ApiSpecificationEntity();
        specification.setName("Version");
        specification.setContent("Content");
        specification.setAuthor("Author");
        specification.setVersion("Version");

        ApiContractEntity contract = new ApiContractEntity();
        contract.setDefinition(definition);
        contract.setSpecification(specification);

        contractRepository.save(contract);
    }

    @Test
    public void shouldReturnTrueForExistingSpecification() {
        boolean exists = specificationRepository.existsByIdAndContractDefinitionId(specification.getId(), definition.getId());

        assertThat(exists, is(true));
    }

    @Test
    public void shouldReturnFalseForNonExistingSpecification() {
        boolean exists = specificationRepository.existsByIdAndContractDefinitionId(999L, definition.getId());

        assertThat(exists, is(false));
    }

    @Test
    public void shouldReturnFalseForNonExistingDefinition() {
        boolean exists = specificationRepository.existsByIdAndContractDefinitionId(specification.getId(), 999L);

        assertThat(exists, is(false));
    }

    @Test
    public void shouldReturnFalseForNonExistingDefinitionAndSpecification() {
        boolean exists = specificationRepository.existsByIdAndContractDefinitionId(888L, 999L);

        assertThat(exists, is(false));
    }
}