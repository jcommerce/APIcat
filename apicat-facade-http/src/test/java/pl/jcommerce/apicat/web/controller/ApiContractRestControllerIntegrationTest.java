package pl.jcommerce.apicat.web.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractUpdateDto;
import pl.jcommerce.apicat.web.AbstractBaseIntegrationTest;

import java.io.File;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by luwa on 23.01.17.
 */
public class ApiContractRestControllerIntegrationTest extends AbstractBaseIntegrationTest {

    private final String contractsPath = "/contracts/";

    private final String testDefinitionName = "Test definition name";
    private final String testDefinitionType = "SWAGGER";
    private final String testSpecificationName = "Test specification name";
    private final String testSpecificationType = "SWAGGER";

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

    private Long createContract() {
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
        return Long.valueOf(locationContractUrl.substring(locationContractUrl.lastIndexOf("/") + 1));
    }

    private Long createDefinition(String filename) {
        File definitionFile = new File(getFilepath(filename));
        assertTrue(definitionFile.exists());

        Response response =
            given().
                multiPart("file", definitionFile).
                formParam("name", testDefinitionName).
                formParam("type", testDefinitionType).
            when().
                post("/definitions").
            then().
                statusCode(HttpStatus.SC_OK).
            extract().
                response();

        String locationDefinitionUrl = response.getHeader("Location");
        return Long.valueOf(locationDefinitionUrl.substring(locationDefinitionUrl.lastIndexOf("/") + 1));
    }

    private Long createSpecification(String filename) {
        File specificationFile = new File(getFilepath(filename));
        assertTrue(specificationFile.exists());

        Response response =
        given().
            multiPart("file", specificationFile).
            formParam("name", testSpecificationName).
            formParam("type", testSpecificationType).
        when().
            post("/specifications").
        then().
            statusCode(HttpStatus.SC_OK).
        extract().
            response();

        String locationSpecificationUrl = response.getHeader("Location");
        return Long.valueOf(locationSpecificationUrl.substring(locationSpecificationUrl.lastIndexOf("/") + 1));
    }
}
