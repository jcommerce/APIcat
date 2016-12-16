package pl.jcommerce.apicat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jcommerce.apicat.business.service.ApiContractService;
import pl.jcommerce.apicat.model.entity.ApiContractEntity;
import pl.jcommerce.apicat.model.entity.ApiContractValidationDetailsEntity;
import pl.jcommerce.apicat.web.dto.ApiContractDto;
import pl.jcommerce.apicat.web.dto.ApiContractValidationDetailsDto;
import pl.jcommerce.apicat.web.mapper.ApiContractMapper;
import pl.jcommerce.apicat.web.mapper.ApiContractValidationDetailsMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jada on 05.12.2016.
 */

@RestController
@RequestMapping("/api/contracts")
public class ApiContractController {

    @Autowired
    private ApiContractService apiContractService;

    @Autowired
    private ApiContractMapper contractMapper;

    @Autowired
    private ApiContractValidationDetailsMapper contractValidationDetailsMapper;

    @GetMapping
    public Page<ApiContractDto> getAll(Pageable pageable) {
        Page<ApiContractEntity> contractEntityPage = apiContractService.findAll(pageable);

        List<ApiContractDto> definitions = contractEntityPage.getContent().stream()
                .map(contractMapper::getDtoFromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(definitions, pageable, contractEntityPage.getTotalElements());
    }

    @GetMapping(path = "/{id}")
    public ApiContractDto getOne(@PathVariable Long id) {
        ApiContractEntity contractEntity = apiContractService.findOneById(id);

        return contractMapper.getDtoFromEntity(contractEntity);
    }

    @GetMapping(path = "/{id}/validationDetails")
    public ApiContractValidationDetailsDto getValidationDetails(@PathVariable Long id) {
        ApiContractValidationDetailsEntity entity = apiContractService.getValidationDetails(id);

        return contractValidationDetailsMapper.getDtoFromEntity(entity);
    }


}
