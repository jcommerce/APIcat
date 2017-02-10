package pl.jcommerce.apicat.contract;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import pl.jcommerce.apicat.contract.swagger.apicontract.SwaggerApiContract;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.swagger.validation.MessageConstants;
import pl.jcommerce.apicat.contract.validation.problem.ProblemLevel;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;
import pl.jcommerce.apicat.contract.validation.result.ValidationResultCategory;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UseCasesTest {

    private final String contractsPath = "contracts/useCasesContracts/";
    private SwaggerApiContract apiContract;
    private SwaggerApiDefinition apiDefinition;
    private SwaggerApiSpecification apiSpecification;

    @Before
    public void setupCorrectContract() {
        apiDefinition = SwaggerApiDefinition.fromPath(contractsPath + "providerContract.json");
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        apiSpecification = SwaggerApiSpecification.fromPath(contractsPath + "consumerContract.json");
        apiSpecification.validate();
        assertTrue(apiSpecification.isValid());

        apiContract = new SwaggerApiContract(apiDefinition, apiSpecification);
        apiContract.validate();
        assertTrue(apiContract.isValid());
    }

    @Test
    public void testAddAndRemoveRequestField() {
        String newContent = loadFile("addedRequestField.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.WARN, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(1, problemList.size());
            assertEquals(
                    new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "name", "Request"),
                            ProblemLevel.WARN),
                    problemList.get(0));
        } else {
            fail();
        }

        newContent = loadFile("providerContract.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.CORRECT, result.get().getValidationResultCategory());
        } else {
            fail();
        }
    }

    @Test
    public void testAddAction() {
        String newContent = loadFile("addedAction.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.WARN, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(1, problemList.size());
            assertEquals(
                    new ValidationProblem(MessageFormat.format(MessageConstants.OPERATION_NOT_USED, "PUT", "updateContract", "/contract/{id}"),
                            ProblemLevel.WARN),
                    problemList.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void testAddReplyField() {
        String newContent = loadFile("addedReplyField.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.WARN, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(1, problemList.size());
            assertEquals(
                    new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "name", "Reply"),
                            ProblemLevel.WARN),
                    problemList.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void testAddRequiredRequestField() {
        String newContent = loadFile("addedRequiredRequestField.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.ERROR, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(1, problemList.size());
            assertEquals(
                    new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "name", "Request"),
                            ProblemLevel.ERROR),
                    problemList.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void testRemoveAction() {
        String newContent = loadFile("removedAction.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.ERROR, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(1, problemList.size());
            assertEquals(
                    new ValidationProblem(MessageFormat.format(MessageConstants.OPERATION_NOT_EXISTS, "getContract"),
                            ProblemLevel.ERROR),
                    problemList.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void testRemoveUsedReplyField() {
        String newContent = loadFile("removedUsedReplyField.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.ERROR, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(1, problemList.size());
            assertEquals(
                    new ValidationProblem(MessageFormat.format(MessageConstants.PROPERTY_NOT_EXISTS, "definitionId", "Reply"),
                            ProblemLevel.ERROR),
                    problemList.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void testModifyActionName() {
        String newContent = loadFile("modifiedActionName.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.ERROR, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(2, problemList.size());
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.OPERATION_NOT_EXISTS, "getContract"),
                            ProblemLevel.ERROR)));
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.OPERATION_NOT_USED, "GET", "modifiedGetContract", "/contract/{id}"),
                            ProblemLevel.WARN)));
        } else {
            fail();
        }
    }

    @Test
    public void testModifyRequestField() {
        String newContent = loadFile("modifiedRequestField.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.ERROR, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(3, problemList.size());
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.PROPERTY_NOT_EXISTS, "definitionId", "Request"),
                            ProblemLevel.ERROR)));
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "definitionIdTest", "Request"),
                            ProblemLevel.WARN)));
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.PROPERTY_WRONG_TYPE, "specificationId"),
                            ProblemLevel.ERROR)));
        } else {
            fail();
        }
    }

    @Test
    public void testModifyRequiredRequestField() {
        String newContent = loadFile("modifiedRequiredRequestField.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.ERROR, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(3, problemList.size());
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.PROPERTY_NOT_EXISTS, "id", "Request"),
                            ProblemLevel.ERROR)));
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "idTest", "Request"),
                            ProblemLevel.WARN)));
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.PROPERTY_WRONG_TYPE, "definitionId"),
                            ProblemLevel.ERROR)));
        } else {
            fail();
        }
    }

    @Test
    public void testModifyReplyField() {
        String newContent = loadFile("modifiedReplyField.json");
        apiDefinition.setContent(newContent);
        apiDefinition.validate();
        assertTrue(apiDefinition.isValid());

        Optional<ValidationResult> result = apiContract.validate();
        if (result.isPresent()) {
            assertEquals(ValidationResultCategory.ERROR, result.get().getValidationResultCategory());
            List<ValidationProblem> problemList = result.get().getProblemList();
            assertEquals(3, problemList.size());
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.PROPERTY_NOT_EXISTS, "specificationId", "Reply"),
                            ProblemLevel.ERROR)));
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.PROPERTY_NOT_USED, "specificationIdTest", "Reply"),
                            ProblemLevel.WARN)));
            assertTrue(
                    problemList.contains(new ValidationProblem(
                            MessageFormat.format(MessageConstants.PROPERTY_WRONG_TYPE, "id"),
                            ProblemLevel.ERROR)));
        } else {
            fail();
        }
    }

    private String loadFile(String fileName) {
        ClassLoader classLoader = SwaggerApiDefinition.class.getClassLoader();
        File file = new File(classLoader.getResource(contractsPath + fileName).getFile());
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            fail();
        }
        return null;
    }
}
