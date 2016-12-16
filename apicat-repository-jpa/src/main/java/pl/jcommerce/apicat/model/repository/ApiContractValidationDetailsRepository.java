package pl.jcommerce.apicat.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;

/**
 * Created by jada on 08.12.2016.
 */

public interface ApiContractValidationDetailsRepository extends JpaRepository<ApiContractValidationDetailsEntity, Long> {
    ApiContractValidationDetailsEntity findOneByContractId(Long contractId);
}
