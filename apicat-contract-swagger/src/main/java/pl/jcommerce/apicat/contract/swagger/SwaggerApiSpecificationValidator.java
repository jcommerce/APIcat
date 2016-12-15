package pl.jcommerce.apicat.contract.swagger;

import com.google.auto.service.AutoService;
import pl.jcommerce.apicat.contract.ApiSpecification;
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
                ((SwaggerApiSpecification) apiSpecification).getSwaggerDefinition() != null;  //TODO verify - implementation
    }

    @Override
    public ValidationResult validate(ApiSpecification apiSpecification) {
        ValidationResult result = new ValidationResult();
        SwaggerApiSpecification swaggerApiSpecification = (SwaggerApiSpecification) apiSpecification;
        if(swaggerApiSpecification.getSwaggerDefinition() == null){
            ValidationProblem problem = new ValidationProblem("Invalid SwaggerApiSpecification", ProblemLevel.ERROR);
            result.addProblem(problem);
        }
        return result;
    }
}
