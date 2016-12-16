package pl.jcommerce.apicat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.jcommerce.apicat.business.service.ApiDefinitionService;
import pl.jcommerce.apicat.model.entity.ApiDefinitionEntity;
import pl.jcommerce.apicat.web.dto.ApiDefinitionDto;
import pl.jcommerce.apicat.web.mapper.ApiDefinitionMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jada on 02.12.2016.
 */

@RestController
@RequestMapping("/api/definitions")
public class ApiDefinitionController {

    @Autowired
    private ApiDefinitionService apiDefinitionService;

    @Autowired
    private ApiDefinitionMapper apiDefinitionMapper;

    @GetMapping
    public Page<ApiDefinitionDto> getAll(Pageable pageable) {

        Page<ApiDefinitionEntity> definitionEntityPage = apiDefinitionService.findAll(pageable);

        List<ApiDefinitionDto> definitions = definitionEntityPage.getContent().stream()
                .map(apiDefinitionMapper::getDtoFromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(definitions, pageable, definitionEntityPage.getTotalElements());
    }

    @GetMapping(path = "/{id}")
    public ApiDefinitionDto getOne(@PathVariable Long id) {
        ApiDefinitionEntity entity = apiDefinitionService.findOneById(id);

        return apiDefinitionMapper.getDtoFromEntity(entity);
    }

    @PostMapping
    public ApiDefinitionDto add(@RequestBody ApiDefinitionDto apiDefinitionDto) {
        ApiDefinitionEntity entity = apiDefinitionService.save(apiDefinitionMapper.getEntityFromDto(apiDefinitionDto));

        return apiDefinitionMapper.getDtoFromEntity(entity);
    }

    @PutMapping(path = "/{id}")
    public ApiDefinitionDto update(@PathVariable Long id, @RequestBody ApiDefinitionDto apiDefinitionDto) {
        ApiDefinitionEntity entity = apiDefinitionMapper.getEntityFromDto(apiDefinitionDto);

        ApiDefinitionEntity updatedEntity = apiDefinitionService.update(entity, id);

        return apiDefinitionMapper.getDtoFromEntity(updatedEntity);
    }

    @DeleteMapping(path = "/{id}")
    public ApiDefinitionDto remove(@PathVariable Long id) {
        ApiDefinitionEntity deletedEntity = apiDefinitionService.delete(id);

        return apiDefinitionMapper.getDtoFromEntity(deletedEntity);
    }

}
