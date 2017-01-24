package pl.jcommerce.apicat.web;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;

import java.io.File;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class AbstractBaseIntegrationTest {

    @LocalServerPort
    private Integer serverPort;

    @Before
    public void setup() {
        RestAssured.port = serverPort;
    }

    protected String getFilepath(String filename) {
        ClassLoader classLoader = AbstractBaseIntegrationTest.class.getClassLoader();
        return classLoader.getResource("contracts/" + filename).getFile();
    }

    protected Long createContract() {
        Long definitionId = createDefinition("json/providerContract.json");

        Long specificationId = createSpecification("json/consumerContract.json");

        ApiContractCreateDto contractCreateDto = new ApiContractCreateDto();
        contractCreateDto.setDefinitionId(definitionId);
        contractCreateDto.setSpecificationId(specificationId);

        Response response =
            given().
                contentType(ContentType.JSON).
                body(contractCreateDto).
            when().
                post("/contracts").
            then().
                statusCode(HttpStatus.SC_OK).
            extract().
                response();

        String locationContractUrl = response.getHeader("Location");
        return Long.valueOf(locationContractUrl.substring(locationContractUrl.lastIndexOf("/") + 1));
    }

    protected Long createDefinition(String filename) {
        File definitionFile = new File(getFilepath(filename));

        Response response =
            given().
                multiPart("file", definitionFile).
                formParam("name", "Definition test name").
                formParam("type", "SWAGGER").
            when().
                post("/definitions").
            then().
                statusCode(HttpStatus.SC_OK).
            extract().
                response();

        String locationDefinitionUrl = response.getHeader("Location");
        return Long.valueOf(locationDefinitionUrl.substring(locationDefinitionUrl.lastIndexOf("/") + 1));
    }

    protected Long createSpecification(String filename) {
        File specificationFile = new File(getFilepath(filename));

        Response response =
            given().
                multiPart("file", specificationFile).
                formParam("name", "Specification test name").
                formParam("type", "SWAGGER").
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
