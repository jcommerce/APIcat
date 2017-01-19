package pl.jcommerce.apicat.service.apispecification;

import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationCreateDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationUpdateDto;

/**
 * Created by luwa on 18.01.17.
 */
public interface ApiSpecificationService {

    Long createSpecification(ApiSpecificationCreateDto apiSpecificationDto, byte[] content);

    ApiSpecificationDto getSpecification(Long id);

    void updateSpecification(Long id, ApiSpecificationUpdateDto apiSpecificationDto);

    void updateSpecificationFile(Long id, byte[] content);

    void deleteSpecification(Long id);
}
