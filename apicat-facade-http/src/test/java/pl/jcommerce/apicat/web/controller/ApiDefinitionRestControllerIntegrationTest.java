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

public class ApiDefinitionRestControllerIntegrationTest extends AbstractBaseIntegrationTest {

    private final String testDefinitionName = "Test definition";
    private final String testDefinitionType = "SWAGGER";

    @Test
    public void testCreateAndReadDefinition() {
        File definitionFile = new File(getFilepath("json/providerContract.json"));
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

        given().
        when().
            get(locationDefinitionUrl).
        then().
            body("id", notNullValue()).
            body("name", equalTo(testDefinitionName)).
            body("type", equalTo(testDefinitionType)).
            body("data", notNullValue());
    }

    @Test
    public void testCreateApiDefinitionWithInvalidContentData() {
        File definitionFile = new File(getFilepath("json/incorrectJsonFormat.json"));
        assertTrue(definitionFile.exists());

        given().
            multiPart("file", definitionFile).
            formParam("name", testDefinitionName).
            formParam("type", testDefinitionType).
        when().
            post("/definitions").
        then().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
