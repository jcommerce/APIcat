package pl.jcommerce.apicat.web.controller;

import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.apache.http.HttpStatus;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionCreateDto;
import pl.jcommerce.apicat.web.AbstractBaseIntegrationTest;

import java.io.File;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;

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

    /*
    @Test
    public void testCreateAndReadDefinition() {

        Assert.assertTrue(new File("Readme.md").exists());

        ApiDefinitionCreateDto data = new ApiDefinitionCreateDto();
        data.setName("Test definition");
        data.setType("SWAGGER");

        Response response =
                given().
                multiPart("file", new File("Readme.md")).
                formParam("name", "TEST").
                formParam("type", "SWAGGER").
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
                body("name", equalTo("TEST")).
                body("type", equalTo("SWAGGER"));
    }
*/
}
