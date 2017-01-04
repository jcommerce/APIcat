package pl.jcommerce.apicat.contract.swagger.apidefinition;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.google.auto.service.AutoService;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.swagger.validation.MessageConstants;
import pl.jcommerce.apicat.contract.swagger.validation.SwaggerApiSchemaValidator;
import pl.jcommerce.apicat.contract.validation.ApiDefinitionValidator;
import pl.jcommerce.apicat.contract.validation.problem.ProblemLevel;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Created by krka on 31.10.2016.
 */
@AutoService(ApiDefinitionValidator.class)
public class SwaggerApiDefinitionValidator implements ApiDefinitionValidator {

    @Override
    public boolean support(ApiDefinition apiDefinition) {
        return apiDefinition instanceof SwaggerApiDefinition &&
                ((SwaggerApiDefinition) apiDefinition).getSwaggerDefinition() != null;
    }

    @Override
    public ValidationResult validate(ApiDefinition apiDefinition) {
        ValidationResult result = new ValidationResult();
        SwaggerApiDefinition swaggerApiDefinition = (SwaggerApiDefinition) apiDefinition;

        ProcessingReport processingReport = null;
        SwaggerApiSchemaValidator swaggerApiSchemaValidator = new SwaggerApiSchemaValidator();
        try {
            processingReport = swaggerApiSchemaValidator.validate(swaggerApiDefinition.getJsonNode());
        } catch (ProcessingException e) {
            ValidationProblem problem = new ValidationProblem("Invalid Json format", ProblemLevel.ERROR);
            result.addProblem(problem);
            return result;
        }

        if (!processingReport.isSuccess()) {
            mapProcessingReportToValidationProblem(processingReport, result);
        }

        return result;
    }

    private void mapProcessingReportToValidationProblem(ProcessingReport processingReport, ValidationResult result) {
        for (ProcessingMessage processingMessage : processingReport) {
            LogLevel logLevel = processingMessage.getLogLevel();
            ProblemLevel problemLevel;
            if (logLevel == LogLevel.ERROR || logLevel == LogLevel.FATAL) {
                problemLevel = ProblemLevel.ERROR;
            } else {
                problemLevel = ProblemLevel.WARN;
            }

            result.addProblem(new ValidationProblem(MessageConstants.INCONSISTENT_PROVIDER_CONTRACT + ": " + processingMessage.getMessage(),
                    problemLevel));
        }
    }
}
