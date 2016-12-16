package pl.jcommerce.apicat.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;

/**
 * Created by jada on 05.12.2016.
 */

public interface ApiDefinitionRepository extends JpaRepository<ApiDefinitionEntity, Long> {
}
