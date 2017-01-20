package pl.jcommerce.apicat.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping("/specifications")
public class ApiSpecificationRestController extends AbstractBaseRestController {

    private static final Logger logger = LoggerFactory.getLogger(ApiSpecificationRestController.class);

    private ApiSpecificationService apiSpecificationService;

    @Autowired
    public ApiSpecificationRestController(ApiSpecificationService apiSpecificationService) {
        this.apiSpecificationService = apiSpecificationService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiSpecificationDto> getSpecification(@PathVariable Long id) {
        logger.debug("Call api specification endpoint with id: {}", id);
        ApiSpecificationDto apiSpecification = apiSpecificationService.getSpecification(id);
        ResponseEntity<ApiSpecificationDto> response = new ResponseEntity<>(apiSpecification, HttpStatus.OK);
        return response;
    }

    @PostMapping
    public ResponseEntity<Void> createSpecification(@RequestPart("file") MultipartFile file, @RequestPart String name, @RequestPart String type) {
        logger.debug("Call create api specification endpoint.");
        ApiSpecificationCreateDto apiSpecification = new ApiSpecificationCreateDto();
        apiSpecification.setType(name);
        apiSpecification.setName(type);

        Long specificationId = null;

        try {
            apiSpecificationService.createSpecification(apiSpecification, file.getBytes());
            logger.debug("Api specification created with id: {}", specificationId);
        } catch (IOException e) {
            logger.debug("Definition file error: ", e);
            throw new ApicatSystemException(READ_FILE_EXCEPTION, e.getMessage());
        }

        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getSpecification(specificationId)).withSelfRel().getHref());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateSpecification(@PathVariable Long id, @RequestBody ApiSpecificationUpdateDto apiSpecificationUpdateDto) {
        logger.debug("Call Api specification update with id: {}", id);
        apiSpecificationService.updateSpecification(id, apiSpecificationUpdateDto);
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getSpecification(id)).withSelfRel().getHref());
    }

    @PutMapping(path = "/{id}/file")
    public ResponseEntity<Void> updateSpecificationFile(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        logger.debug("Call Api specification update file with id: {}", id);
        try {
            apiSpecificationService.updateSpecificationFile(id, file.getBytes());
        } catch (IOException e) {
            logger.error("Specification file error: {}.", e);
            throw new ApicatSystemException(READ_FILE_EXCEPTION, e.getMessage());
        }
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getSpecification(id)).withSelfRel().getHref());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeSpecification(@PathVariable Long id) {
        logger.debug("Call Api specification delete with id: {}", id);
        apiSpecificationService.deleteSpecification(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}

