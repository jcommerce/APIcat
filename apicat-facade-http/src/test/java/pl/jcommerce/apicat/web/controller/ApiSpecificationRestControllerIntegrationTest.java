package pl.jcommerce.apicat.web.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationUpdateDto;
import pl.jcommerce.apicat.web.AbstractBaseIntegrationTest;

import java.io.File;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ApiSpecificationRestControllerIntegrationTest extends AbstractBaseIntegrationTest {

    private final String specificationsPath = "/specifications/";

    private final String testSpecificationName = "Test specification name";
    private final String testSpecificationType = "SWAGGER";

    @Test
    public void testCreateAndReadSpecification() {
        File specificationFile = new File(getFilepath("json/consumerContract.json"));
        assertTrue(specificationFile.exists());

        Response response =
            given().
                multiPart("file", specificationFile).
                formParam("name", testSpecificationName).
                formParam("type", testSpecificationType).
            when().
                post(specificationsPath).
            then().
                statusCode(HttpStatus.SC_OK).
            extract().
                response();

        String locationSpecificationUrl = response.getHeader("Location");

        given().
        when().
            get(locationSpecificationUrl).
        then().
            body("id", notNullValue()).
            body("name", equalTo(testSpecificationName)).
            body("type", equalTo(testSpecificationType)).
            body("data", notNullValue());
    }

    @Test
    public void testCreateSpecificationWithInvalidContentData() {
        File specificationFile = new File(getFilepath("yaml/incorrectYamlFormat.yaml"));
        assertTrue(specificationFile.exists());

        given().
            multiPart("file", specificationFile).
            formParam("name", testSpecificationName).
            formParam("type", testSpecificationType).
        when().
            post(specificationsPath).
        then().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testUpdateSpecification() {
        Long specificationId = createSpecification("json/consumerContract.json");

        String data =
            given().
            when().
                get(specificationsPath + specificationId).
            then().
                statusCode(HttpStatus.SC_OK).
                body("id", equalTo(specificationId.intValue())).
            extract().
                path("data");

        String title = getSwaggerTitle(data);

        String newName = "newName";
        Long newContractId = createContract();
        ApiSpecificationUpdateDto specificationUpdateDto = new ApiSpecificationUpdateDto();
        specificationUpdateDto.setName(newName);
        specificationUpdateDto.setContractId(newContractId);

        given().
            contentType(ContentType.JSON).
            body(specificationUpdateDto).
        when().
            put(specificationsPath + specificationId).
        then().
            statusCode(HttpStatus.SC_OK);

        File newSpecificationFile = new File(getFilepath("json/consumerContractChanged.json"));
        given().
            multiPart("file", newSpecificationFile).
        when().
            put(specificationsPath + specificationId + "/file").
        then().
            statusCode(HttpStatus.SC_OK);

        String newData =
            given().
            when().
                get(specificationsPath + specificationId).
            then().
                statusCode(HttpStatus.SC_OK).
                body("id", equalTo(specificationId.intValue())).
                body("name", equalTo(newName)).
                body("contractId", equalTo(newContractId.intValue())).
            extract().
                path("data");

        String newTitle = getSwaggerTitle(newData);

        assertNotNull(newTitle);
        assertNotEquals(title, newTitle);
    }

    @Test
    public void testDeleteSpecification() {
        Long specificationId = createSpecification("json/consumerContract.json");

        given().
        when().
            get(specificationsPath + specificationId).
        then().
            statusCode(HttpStatus.SC_OK).
            body("id", equalTo(specificationId.intValue()));

        given().
        when().
            delete(specificationsPath + specificationId).
        then().
            statusCode(HttpStatus.SC_OK);

        given().
        when().
            get(specificationsPath + specificationId).
        then().
            statusCode(HttpStatus.SC_NOT_FOUND).
            body("id", nullValue());
    }

    private String getSwaggerTitle(String data) {
        SwaggerApiSpecification apiSpecification = new SwaggerApiSpecification();
        apiSpecification.setContent(data);
        apiSpecification.generateSwaggerFromContent();
        return apiSpecification.getSwaggerDefinition().getInfo().getTitle();
    }
}
