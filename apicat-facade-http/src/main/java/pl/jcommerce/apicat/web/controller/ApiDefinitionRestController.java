package pl.jcommerce.apicat.web.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.service.apidefinition.ApiDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionCreateDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionDto;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionUpdateDto;

import java.io.IOException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static pl.jcommerce.apicat.contract.exception.ErrorCode.READ_FILE_EXCEPTION;

@RestController
@RequestMapping("/definitions")
public class ApiDefinitionRestController extends AbstractBaseRestController {

    private static final Logger logger = LoggerFactory.getLogger(ApiDefinitionRestController.class);

    private ApiDefinitionService apiDefinitionService;

    @Autowired
    public ApiDefinitionRestController(ApiDefinitionService apiDefinitionService) {
        this.apiDefinitionService = apiDefinitionService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiDefinitionDto> getDefinition(@PathVariable Long id) {
        logger.debug("Call api definition endpoint with id: {}", id);
        ApiDefinitionDto apiDefinition = apiDefinitionService.getDefinition(id);
        ResponseEntity<ApiDefinitionDto> response = new ResponseEntity<>(apiDefinition, HttpStatus.OK);
        return response;
    }

    @PostMapping
    public ResponseEntity<Void> createDefinition(@RequestPart("file") MultipartFile file, @RequestPart String name, @RequestPart String type) {
        logger.debug("Call create api definition endpoint.");
        ApiDefinitionCreateDto apiDefinition = new ApiDefinitionCreateDto();
        apiDefinition.setName(name);
        apiDefinition.setType(type);

        Long definitionId = null;

        try {
            definitionId = apiDefinitionService.createDefinition(apiDefinition, file.getBytes());
            logger.debug("Api definition created with id: {}", definitionId);
        } catch (IOException e) {
            logger.error("Definition file error: {}.", e);
            throw new ApicatSystemException(READ_FILE_EXCEPTION, e.getMessage());
        }

        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getDefinition(definitionId)).withSelfRel().getHref());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateDefinition(@PathVariable Long id, @RequestBody ApiDefinitionUpdateDto apiDefinitionUpdateDto) {
        logger.debug("Call Api definition update with id: {}", id);
        apiDefinitionService.updateDefinition(id, apiDefinitionUpdateDto);
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getDefinition(id)).withSelfRel().getHref());
    }

    @PutMapping(path = "/{id}/file")
    public ResponseEntity<Void> updateDefinitionFile(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        logger.debug("Call Api definition update file with id: {}", id);
        try {
            apiDefinitionService.updateDefinitionFile(id, file.getBytes());
        } catch (IOException e) {
            logger.error("Definition file error: {}.", e);
            throw new ApicatSystemException(READ_FILE_EXCEPTION, e.getMessage());
        }
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getDefinition(id)).withSelfRel().getHref());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeDefinition(@PathVariable Long id) {
        logger.debug("Call Api definition delete with id: {}", id);
        apiDefinitionService.deleteDefinition(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
