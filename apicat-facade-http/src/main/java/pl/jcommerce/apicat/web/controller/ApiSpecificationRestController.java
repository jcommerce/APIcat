package pl.jcommerce.apicat.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.service.apispecification.ApiSpecificationService;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationCreateDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationDto;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationUpdateDto;

import java.io.IOException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static pl.jcommerce.apicat.contract.exception.ErrorCode.READ_FILE_EXCEPTION;

@Slf4j
@RestController
@RequestMapping("/specifications")
public class ApiSpecificationRestController extends AbstractBaseRestController {

    private ApiSpecificationService apiSpecificationService;

    @Autowired
    public ApiSpecificationRestController(ApiSpecificationService apiSpecificationService) {
        this.apiSpecificationService = apiSpecificationService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiSpecificationDto> getSpecification(@PathVariable Long id) {
        log.debug("Call api specification endpoint with id: {}", id);
        ApiSpecificationDto apiSpecification = apiSpecificationService.getSpecification(id);
        return new ResponseEntity<>(apiSpecification, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createSpecification(@RequestPart("file") MultipartFile file, @RequestPart String name, @RequestPart String type) {
        log.debug("Call create api specification endpoint.");
        ApiSpecificationCreateDto apiSpecification = new ApiSpecificationCreateDto();
        apiSpecification.setName(name);
        apiSpecification.setType(type);

        Long specificationId;
        try {
            specificationId = apiSpecificationService.createSpecification(apiSpecification, file.getBytes());
            log.debug("Api specification created with id: {}", specificationId);
        } catch (IOException e) {
            log.debug("Definition file error: ", e);
            throw new ApicatSystemException(READ_FILE_EXCEPTION, e.getMessage());
        }

        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getSpecification(specificationId)).withSelfRel().getHref());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateSpecification(@PathVariable Long id, @RequestBody ApiSpecificationUpdateDto apiSpecificationUpdateDto) {
        log.debug("Call Api specification update with id: {}", id);
        apiSpecificationService.updateSpecification(id, apiSpecificationUpdateDto);
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getSpecification(id)).withSelfRel().getHref());
    }

    @PutMapping(path = "/{id}/file")
    public ResponseEntity<Void> updateSpecificationFile(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        log.debug("Call Api specification update file with id: {}", id);
        try {
            apiSpecificationService.updateSpecificationFile(id, file.getBytes());
        } catch (IOException e) {
            log.error("Specification file error: {}.", e);
            throw new ApicatSystemException(READ_FILE_EXCEPTION, e.getMessage());
        }
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getSpecification(id)).withSelfRel().getHref());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeSpecification(@PathVariable Long id) {
        log.debug("Call Api specification delete with id: {}", id);
        apiSpecificationService.deleteSpecification(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

