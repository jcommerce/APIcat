package pl.jcommerce.apicat.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jcommerce.apicat.service.apicontract.ApiContractService;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractUpdateDto;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/contracts")
public class ApiContractRestController extends AbstractBaseRestController {

    private ApiContractService apiContractService;

    @Autowired
    public ApiContractRestController(ApiContractService apiContractService){
        this.apiContractService = apiContractService;
    }

    @GetMapping(path = "/{id}")
    public Object getContract(@PathVariable Long id) {
        log.debug("Call api contract endpoint with id: {}", id);
        ApiContractDto apiContractDto = apiContractService.getContract(id);
        return new ResponseEntity<>(apiContractDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createContract(@RequestBody ApiContractCreateDto apiContractCreateData) {
        log.debug("Call create api contract endpoint.");
        Long contractId = apiContractService.createContract(apiContractCreateData);
        log.debug("Api contract created with id: {}", contractId);
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getContract(contractId)).withSelfRel().getHref());

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateContract(@PathVariable Long id, @RequestBody ApiContractUpdateDto apiContractUpdateData) {
        log.debug("Call Api contract update with id: {}", id);
        apiContractService.updateContract(id, apiContractUpdateData);
        return obtainResponseEntityWithLocationHeader(linkTo(methodOn(getClass()).getContract(id)).withSelfRel().getHref());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeContract(@PathVariable Long id) {
        log.debug("Call Api contract delete with id: {}", id);
        apiContractService.deleteContract(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
