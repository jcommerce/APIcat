package pl.jcommerce.apicat.service.apispecification;

import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationCreateDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationUpdateDto;

/**
 * Created by luwa on 18.01.17.
 */
public interface ApiSpecificationService {

    Long createSpecification(ApiSpecificationCreateDto data);

    ApiSpecificationDto getSpecification(Long id);

    Long updateSpecification(ApiSpecificationUpdateDto data);

    void deleteSpecification(Long id);
}
