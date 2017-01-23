package pl.jcommerce.apicat.web.controller;

import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionCreateDto;
import pl.jcommerce.apicat.web.AbstractBaseIntegrationTest;

import java.io.File;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ApiDefinitionRestControllerIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    public void testCreateApiDefinitionWithInvalidContentData() {

        Assert.assertTrue(new File("Readme.md").exists());

        ApiDefinitionCreateDto data = new ApiDefinitionCreateDto();
        data.setName("Test definition");
        data.setType("SWAGGER");

        given().
                multiPart("file", new File("Readme.md")).
                formParam("name", "TEST").
                formParam("type", "SWAGGER").
                when().
                post("/definitions").
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    public void testCreateAndReadDefinition() {
        File definitionFile = new File("src/test/resources/json/providerContract.json");//TODO move path to
        final String testDefinitionName = "TEST";
        final String testDefinitionType = "SWAGGER";

        Assert.assertTrue(definitionFile.exists());

        Response response =
                given().
                        multiPart("file", definitionFile).
                        formParam("name", testDefinitionName).
                        formParam("type", testDefinitionType).
                        when().
                        post("/definitions").
                        then().
                        statusCode(200).
                        extract().response();

        String locationDefinitionUrl = response.getHeader("Location");

        given().
                when().
                get(locationDefinitionUrl).
                then().
                body("id", equalTo(1)).
                body("type", equalTo(testDefinitionType)).
                body("name", equalTo(testDefinitionName)).
                body("data", notNullValue());
    }
}
