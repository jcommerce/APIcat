package pl.jcommerce.apicat.web.controller;

import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import pl.jcommerce.apicat.web.AbstractBaseIntegrationTest;

import java.io.File;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class ApiSpecificationRestControllerIntegrationTest extends AbstractBaseIntegrationTest {

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
                post("/specifications").
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
            post("/specifications").
        then().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
