package pl.jcommerce.apicat.contract.swagger.apispecification;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.google.auto.service.AutoService;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.swagger.validation.MessageConstants;
import pl.jcommerce.apicat.contract.swagger.validation.SwaggerApiSchemaValidator;
import pl.jcommerce.apicat.contract.validation.ApiSpecificationValidator;
import pl.jcommerce.apicat.contract.validation.problem.ProblemLevel;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

/**
 * Created by husk on 15.12.2016.
 */
@AutoService(ApiSpecificationValidator.class)
public class SwaggerApiSpecificationValidator implements ApiSpecificationValidator {

    @Override
    public boolean support(ApiSpecification apiSpecification) {
        return apiSpecification instanceof SwaggerApiSpecification &&
                ((SwaggerApiSpecification) apiSpecification).getSwaggerDefinition() != null;
    }

    @Override
    public ValidationResult validate(ApiSpecification apiSpecification) {
        ValidationResult result = new ValidationResult();
        SwaggerApiSpecification swaggerApiSpecification = (SwaggerApiSpecification) apiSpecification;

        ProcessingReport processingReport;
        SwaggerApiSchemaValidator swaggerApiSchemaValidator = new SwaggerApiSchemaValidator();
        try{
            processingReport = swaggerApiSchemaValidator.validate(swaggerApiSpecification.getJsonNode());
        }catch (ProcessingException e){
            ValidationProblem problem = new ValidationProblem("Invalid Json format", ProblemLevel.ERROR);
            result.addProblem(problem);
            return result;
        }
        if(!processingReport.isSuccess()){
           mapProcessingRaportToValidationProblem(processingReport, result);
        }
        return result;
    }

    private void mapProcessingRaportToValidationProblem(ProcessingReport processingReport, ValidationResult result){
        for(ProcessingMessage processingMessage : processingReport){
            LogLevel logLevel = processingMessage.getLogLevel();
            ProblemLevel problemLevel;
            if(logLevel == LogLevel.ERROR || logLevel == LogLevel.FATAL ){
                problemLevel = ProblemLevel.ERROR;
            } else {
                problemLevel = ProblemLevel.WARN;
            }
            result.addProblem(new ValidationProblem(MessageConstants.INCONSISTENT_CONSUMER_CONTRACT + ": " + processingMessage.getMessage(),
                    problemLevel));
        }
    }
}
