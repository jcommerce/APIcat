package pl.jcommerce.apicat.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;

/**
 * Created by jada on 07.12.2016.
 */

public interface ApiContractRepository extends JpaRepository<ApiContractEntity, Long> {
}
