package pl.jcommerce.apicat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.jcommerce.apicat.business.service.ApiSpecificationService;
import pl.jcommerce.apicat.model.entity.ApiSpecificationEntity;
import pl.jcommerce.apicat.web.dto.ApiSpecificationDto;
import pl.jcommerce.apicat.web.mapper.ApiSpecificationMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jada on 02.12.2016.
 */

@RestController
@RequestMapping("/api/definitions/{definitionId}/specifications")
public class ApiSpecificationController {

    @Autowired
    private ApiSpecificationService apiSpecificationService;

    @Autowired
    private ApiSpecificationMapper apiSpecificationMapper;

    @GetMapping
    public Page<ApiSpecificationDto> getAll(Pageable pageable, @PathVariable Long definitionId) {
        Page<ApiSpecificationEntity> page = apiSpecificationService.findAllByDefinitionId(definitionId, pageable);

        List<ApiSpecificationDto> specifications = page.getContent().stream()
                .map(apiSpecificationMapper::getDtoFromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(specifications, pageable, page.getTotalElements());
    }

    @GetMapping(path = "/{id}")
    public ApiSpecificationDto getOne(@PathVariable Long definitionId, @PathVariable Long id) {
        ApiSpecificationEntity entity = apiSpecificationService.findOneByIdAndDefinitionId(id, definitionId);

        return apiSpecificationMapper.getDtoFromEntity(entity);
    }

    @PostMapping
    public ApiSpecificationDto add(@PathVariable Long definitionId, @RequestBody ApiSpecificationDto apiSpecificationDto) {
        ApiSpecificationEntity entity = apiSpecificationMapper.getEntityFromDto(apiSpecificationDto);

        apiSpecificationService.addApiSpecification(entity, definitionId);

        return apiSpecificationMapper.getDtoFromEntity(entity);
    }

    @PutMapping(path = "/{id}")
    public ApiSpecificationDto update(@PathVariable Long definitionId, @PathVariable Long id, @RequestBody ApiSpecificationDto apiSpecificationDto) {
        ApiSpecificationEntity entity = apiSpecificationMapper.getEntityFromDto(apiSpecificationDto);

        apiSpecificationService.updateApiSpecification(entity, id, definitionId);

        return apiSpecificationMapper.getDtoFromEntity(entity);
    }

    @DeleteMapping(path = "/{id}")
    public ApiSpecificationDto remove(@PathVariable Long definitionId, @PathVariable Long id) {
        ApiSpecificationEntity entity = apiSpecificationService.deleteByIdAndDefinitionId(id, definitionId);

        return apiSpecificationMapper.getDtoFromEntity(entity);
    }

}
