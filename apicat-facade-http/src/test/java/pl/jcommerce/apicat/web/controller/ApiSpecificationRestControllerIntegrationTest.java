/*
package pl.jcommerce.apicat.web.controller;

import org.junit.Assert;
import org.junit.Test;
import pl.jcommerce.apicat.service.apispecification.dto.ApiSpecificationCreateDto;
import pl.jcommerce.apicat.web.AbstractBaseIntegrationTest;

import java.io.File;

import static com.jayway.restassured.RestAssured.given;

public class ApiSpecificationRestControllerIntegrationTest extends AbstractBaseIntegrationTest {

    @Test
    public void testCreateSpecification() {
        Assert.assertTrue(new File("Readme.md").exists());
        ApiSpecificationCreateDto data = new ApiSpecificationCreateDto();
        data.setName("Test specification");
        data.setType("SWAGGER");

        given().
                multiPart("file", new File("Readme.md")).
                formParam("name", "TEST").
                formParam("type", "SWAGGER").
                when().
                post("/specifications").
                then().
                statusCode(200);
    }
}
*/
