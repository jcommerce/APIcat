package pl.jcommerce.apicat.contract.swagger;

import com.google.auto.service.AutoService;
import pl.jcommerce.apicat.contract.ApiDefinition;
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
        if (swaggerApiDefinition.getSwaggerDefinition() == null) {
            ValidationProblem problem = new ValidationProblem("Invalid SwaggerApiDefinition", ProblemLevel.ERROR);
            result.addProblem(problem);
        }
        return result;
    }
}
