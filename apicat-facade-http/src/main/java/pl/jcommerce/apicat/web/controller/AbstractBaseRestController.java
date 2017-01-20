package pl.jcommerce.apicat.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

abstract class AbstractBaseRestController {

    ResponseEntity<Void> obtainResponseEntityWithLocationHeader(String location){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(URI.create(location));
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.OK);
    }
}
