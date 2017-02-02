package pl.jcommerce.apicat.web.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import pl.jcommerce.apicat.contract.ApiStage;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;
import pl.jcommerce.apicat.contract.validation.result.ValidationResultCategory;
import pl.jcommerce.apicat.service.apidefinition.dto.ApiDefinitionUpdateDto;
import pl.jcommerce.apicat.web.AbstractBaseIntegrationTest;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ApiDefinitionRestControllerIntegrationTest extends AbstractBaseIntegrationTest {

    private final String definitionsPath = "/definitions/";

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
                post(definitionsPath).
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
            post(definitionsPath).
        then().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testUpdateDefinition() {
        Long definitionId = createDefinition("json/providerContract.json");

        String data =
            given().
            when().
                get(definitionsPath + definitionId).
            then().
                statusCode(HttpStatus.SC_OK).
                body("id", equalTo(definitionId.intValue())).
            extract().
                path("data");

        String title = getSwaggerTitle(data);

        String newName = "newName";
        Long newContractId = createContract();
        ApiDefinitionUpdateDto definitionUpdateDto = new ApiDefinitionUpdateDto();
        definitionUpdateDto.setName(newName);
        definitionUpdateDto.setContractIds(Collections.singletonList(newContractId));

        given().
            contentType(ContentType.JSON).
            body(definitionUpdateDto).
        when().
            put(definitionsPath + definitionId).
        then().
            statusCode(HttpStatus.SC_OK);

        File newDefinitionFile = new File(getFilepath("json/providerContractChanged.json"));
        given().
            multiPart("file", newDefinitionFile).
        when().
            put(definitionsPath + definitionId + "/file").
        then().
            statusCode(HttpStatus.SC_OK);

        String newData =
            given().
            when().
                get(definitionsPath + definitionId).
            then().
                statusCode(HttpStatus.SC_OK).
                body("id", equalTo(definitionId.intValue())).
                body("name", equalTo(newName)).
                body("contractIds", contains(newContractId.intValue())).
            extract().
                path("data");

        String newTitle = getSwaggerTitle(newData);

        assertNotNull(newTitle);
        assertNotEquals(title, newTitle);
    }

    @Test
    public void testDeleteDefinition() {
        Long definitionId = createDefinition("json/providerContract.json");

        given().
        when().
            get(definitionsPath + definitionId).
        then().
            statusCode(HttpStatus.SC_OK).
            body("id", equalTo(definitionId.intValue()));

        given().
        when().
            delete(definitionsPath + definitionId).
        then().
            statusCode(HttpStatus.SC_OK);

        given().
        when().
            get(definitionsPath + definitionId).
        then().
            statusCode(HttpStatus.SC_NOT_FOUND).
            body("id", nullValue());
    }

    @Test
    public void testValidateDefinition() {
        Long definitionId = createDefinition("json/providerContract.json");
        Long correctSpecificationId = createSpecification("json/consumerContract.json");
        Long invalidSpecificationId = createSpecification("json/consumerContractWithoutEndpoints.json");

        List<ValidationProblem> validationProblemsFromSelected =
            given().
            when().
                get(definitionsPath + definitionId + "/validate/" + correctSpecificationId + "," + invalidSpecificationId ).
            then().
                statusCode(HttpStatus.SC_OK).
                body("validationResultCategory", equalTo(ValidationResultCategory.ERROR.name())).
            extract().
                path("problemList");

        assertFalse(validationProblemsFromSelected.isEmpty());

        Long correctContractId = createContract(definitionId, correctSpecificationId);
        Long invalidContractId = createContract(definitionId, invalidSpecificationId);

        ApiDefinitionUpdateDto definitionUpdateDto = new ApiDefinitionUpdateDto();
        definitionUpdateDto.setContractIds(Arrays.asList(correctContractId, invalidContractId));

        given().
            contentType(ContentType.JSON).
            body(definitionUpdateDto).
        when().
            put(definitionsPath + definitionId).
        then().
            statusCode(HttpStatus.SC_OK);

        List<ValidationProblem> allValidationProblems =
            given().
            when().
                get(definitionsPath + definitionId + "/validateAll").
            then().
                statusCode(HttpStatus.SC_OK).
                body("validationResultCategory", equalTo(ValidationResultCategory.ERROR.name())).
            extract().
                path("problemList");

            assertFalse(allValidationProblems.isEmpty());
            assertEquals(validationProblemsFromSelected.size(), allValidationProblems.size());
    }

    @Test
    public void testReleaseDefinition() {
        Long definitionId = createDefinition("json/providerContract.json");
        Long correctSpecificationId = createSpecification("json/consumerContract.json");
        Long invalidSpecificationId = createSpecification("json/consumerContractWithoutEndpoints.json");

        Long correctContractId = createContract(definitionId, correctSpecificationId);
        Long invalidContractId = createContract(definitionId, invalidSpecificationId);

        //Test correct definition
        ApiDefinitionUpdateDto definitionUpdateDto = new ApiDefinitionUpdateDto();
        definitionUpdateDto.setContractIds(Collections.singletonList(correctContractId));
        given().
            contentType(ContentType.JSON).
            body(definitionUpdateDto).
        when().
            put(definitionsPath + definitionId).
        then().
            statusCode(HttpStatus.SC_OK);

        given().
        when().
            patch(definitionsPath + definitionId + "/release").
        then().
            statusCode(HttpStatus.SC_OK);

        given().
        when().
            get(definitionsPath + definitionId).
        then().
            statusCode(HttpStatus.SC_OK).
            body("id", notNullValue()).
            body("stage", equalTo(ApiStage.RELEASED.name()));

        //Test incorrect definition
        definitionUpdateDto.setContractIds(Arrays.asList(correctContractId, invalidContractId));
        given().
            contentType(ContentType.JSON).
            body(definitionUpdateDto).
        when().
            put(definitionsPath + definitionId).
        then().
            statusCode(HttpStatus.SC_OK);

        given().
        when().
            patch(definitionsPath + definitionId + "/release").
        then().
            statusCode(HttpStatus.SC_FORBIDDEN);

        given().
        when().
            get(definitionsPath + definitionId).
        then().
            statusCode(HttpStatus.SC_OK).
            body("id", notNullValue()).
            body("stage", equalTo(ApiStage.DRAFT.name()));
    }

    private String getSwaggerTitle(String data) {
        SwaggerApiDefinition apiDefinition = new SwaggerApiDefinition();
        apiDefinition.setContent(data);
        apiDefinition.generateSwaggerFromContent();
        return apiDefinition.getSwaggerDefinition().getInfo().getTitle();
    }
}
