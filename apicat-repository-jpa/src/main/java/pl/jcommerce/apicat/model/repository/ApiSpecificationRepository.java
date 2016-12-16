package pl.jcommerce.apicat.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jcommerce.apicat.model.entity.ApiSpecificationEntity;

/**
 * Created by jada on 05.12.2016.
 */

public interface ApiSpecificationRepository extends JpaRepository<ApiSpecificationEntity, Long> {
    Page<ApiSpecificationEntity> findAllByContractDefinitionId(Long definitionId, Pageable pageable);

    ApiSpecificationEntity findOneByIdAndContractDefinitionId(Long id, Long definitionId);

    @Query("SELECT COUNT(s)>0 FROM specification s JOIN s.contract c JOIN c.definition d WHERE s.id = ?1 AND d.id = ?2")
    boolean existsByIdAndContractDefinitionId(Long id, Long definitionId);
}
