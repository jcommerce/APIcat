package pl.jcommerce.apicat.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.service.apidefinition.ApiDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionCreateDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionUpdateDto;

import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static pl.jcommerce.apicat.contract.exception.ErrorCode.READ_FILE_EXCEPTION;

@Slf4j
@RestController
@RequestMapping("/definitions")
public class ApiDefinitionRestController extends AbstractBaseRestController {

    private ApiDefinitionService apiDefinitionService;

    @Autowired
    public ApiDefinitionRestController(ApiDefinitionService apiDefinitionService) {
        this.apiDefinitionService = apiDefinitionService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiDefinitionDto> getDefinition(@PathVariable Long id) {
        log.debug("Call api definition endpoint with id: {}", id);
        ApiDefinitionDto apiDefinition = apiDefinitionService.getDefinition(id);
        return new ResponseEntity<>(apiDefinition, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createDefinition(@RequestPart("file") MultipartFile file, @RequestPart String name, @RequestPart String type) {
        log.debug("Call create api definition endpoint.");
        ApiDefinitionCreateDto apiDefinition = new ApiDefinitionCreateDto();
        apiDefinition.setName(name);
        apiDefinition.setType(type);

        Long definitionId;
        try {
            definitionId = apiDefinitionService.createDefinition(apiDefinition, file.getBytes());
            log.debug("Api definition created with id: {}", definitionId);
        } catch (IOException e) {
            log.error("Definition file error: {}.", e);
            throw new ApicatSystemException(READ_FILE_EXCEPTION, e.getMessage());
        }

        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getDefinition(definitionId)).withSelfRel().getHref());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateDefinition(@PathVariable Long id, @RequestBody ApiDefinitionUpdateDto apiDefinitionUpdateDto) {
        log.debug("Call Api definition update with id: {}", id);
        apiDefinitionService.updateDefinition(id, apiDefinitionUpdateDto);
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getDefinition(id)).withSelfRel().getHref());
    }

    @PutMapping(path = "/{id}/file")
    public ResponseEntity<Void> updateDefinitionFile(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        log.debug("Call Api definition update file with id: {}", id);
        try {
            apiDefinitionService.updateDefinitionFile(id, file.getBytes());
        } catch (IOException e) {
            log.error("Definition file error: {}.", e);
            throw new ApicatSystemException(READ_FILE_EXCEPTION, e.getMessage());
        }
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getDefinition(id)).withSelfRel().getHref());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeDefinition(@PathVariable Long id) {
        log.debug("Call Api definition delete with id: {}", id);
        apiDefinitionService.deleteDefinition(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/validate/{specificationIds}")
    public ResponseEntity<ValidationResult> validateAgainstSpecifications(@PathVariable Long id, @PathVariable List<Long> specificationIds) {
        log.debug("Call api definition endpoint with id: {}", id);
        ValidationResult result = apiDefinitionService.validateAgainstSpecifications(id, specificationIds);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/validateAll")
    public ResponseEntity<ValidationResult> validateAllSpecifications(@PathVariable Long id) {
        log.debug("Call api definition endpoint with id: {}", id);
        ValidationResult result = apiDefinitionService.validateAgainstAllSpecifications(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
