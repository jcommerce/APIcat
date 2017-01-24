package pl.jcommerce.apicat.web.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractUpdateDto;
import pl.jcommerce.apicat.web.AbstractBaseIntegrationTest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by luwa on 23.01.17.
 */
public class ApiContractRestControllerIntegrationTest extends AbstractBaseIntegrationTest {

    private final String contractsPath = "/contracts/";

    @Test
    public void testCreateAndReadContract() {
        Long definitionId = createDefinition("json/providerContract.json");
        assertNotNull(definitionId);

        Long specificationId = createSpecification("json/consumerContract.json");
        assertNotNull(specificationId);

        ApiContractCreateDto contractCreateDto = new ApiContractCreateDto();
        contractCreateDto.setDefinitionId(definitionId);
        contractCreateDto.setSpecificationId(specificationId);

        Response response =
            given().
                contentType(ContentType.JSON).
                body(contractCreateDto).
            when().
                post(contractsPath).
            then().
                statusCode(HttpStatus.SC_OK).
            extract().
                response();

        String locationContractUrl = response.getHeader("Location");

        given().
        when().
            get(locationContractUrl).
        then().
            body("id", notNullValue()).
            body("definitionId", equalTo(definitionId.intValue())).
            body("specificationId", equalTo(specificationId.intValue()));
    }

    @Test
    public void testUpdateContract() {
        Long contractId = createContract();

        Response response =
            given().
            when().
                get(contractsPath + contractId).
            then().
                statusCode(HttpStatus.SC_OK).
                body("id", equalTo(contractId.intValue())).
            extract().
                response();

        Integer definitionId = response.path("definitionId");
        Integer specificationId = response.path("specificationId");

        Long newDefinitionId = createDefinition("json/providerContract.json");

        ApiContractUpdateDto contractUpdateDto = new ApiContractUpdateDto();
        contractUpdateDto.setDefinitionId(newDefinitionId);
        contractUpdateDto.setSpecificationId(Long.valueOf(specificationId));

        given().
            contentType(ContentType.JSON).
            body(contractUpdateDto).
        when().
            put(contractsPath + contractId).
        then().
            statusCode(HttpStatus.SC_OK);

        given().
        when().
            get(contractsPath + contractId).
        then().
            statusCode(HttpStatus.SC_OK).
            body("id", equalTo(contractId.intValue())).
            body("definitionId", not(equalTo(definitionId))).
            body("definitionId", equalTo(newDefinitionId.intValue())).
            body("specificationId", equalTo(specificationId));
    }

    @Test
    public void testDeleteContract() {
        Long contractId = createContract();

        given().
        when().
            get(contractsPath + contractId).
        then().
            statusCode(HttpStatus.SC_OK).
            body("id", notNullValue());

        given().
        when().
            delete(contractsPath + contractId).
        then().
            statusCode(HttpStatus.SC_OK);

        given().
        when().
            get(contractsPath + contractId).
        then().
            statusCode(HttpStatus.SC_NOT_FOUND).
            body("id", nullValue());
    }
}
