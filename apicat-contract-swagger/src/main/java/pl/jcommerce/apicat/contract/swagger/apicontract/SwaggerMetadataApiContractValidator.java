package pl.jcommerce.apicat.contract.swagger.apicontract;

import com.google.auto.service.AutoService;
import io.swagger.models.Scheme;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.swagger.apidefinition.SwaggerApiDefinition;
import pl.jcommerce.apicat.contract.swagger.apispecification.SwaggerApiSpecification;
import pl.jcommerce.apicat.contract.swagger.validation.MessageConstants;
import pl.jcommerce.apicat.contract.validation.ApiContractValidator;
import pl.jcommerce.apicat.contract.validation.problem.ProblemLevel;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;
import pl.jcommerce.apicat.contract.validation.result.ValidationResult;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by krka on 23.10.2016.
 */
@AutoService(ApiContractValidator.class)
public class SwaggerMetadataApiContractValidator extends SwaggerApiContractValidator {

    @Override
    public ValidationResult validate(ApiDefinition apiDefinition, ApiSpecification apiSpecification) {
        ValidationResult result = new ValidationResult();

        SwaggerApiDefinition swaggerApiDefinition = (SwaggerApiDefinition) apiDefinition;
        SwaggerApiSpecification swaggerApiSpecification = (SwaggerApiSpecification) apiSpecification;

        String apiSpecificationHost = swaggerApiSpecification.getSwaggerDefinition().getHost();
        String apiDefinitionHost = swaggerApiDefinition.getSwaggerDefinition().getHost();
        if (!apiSpecificationHost.equals(apiDefinitionHost)) {
            result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.WRONG_HOST_ADDRESS, apiSpecificationHost, apiDefinitionHost), ProblemLevel.ERROR));
        }

        List<Scheme> apiSpecificationSchemes = swaggerApiSpecification.getSwaggerDefinition().getSchemes();
        List<Scheme> apiDefinitionSchemes = swaggerApiDefinition.getSwaggerDefinition().getSchemes();
        if (!apiSpecificationSchemes.equals(apiDefinitionSchemes)) {
            result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.WRONG_HOST_SCHEMES, apiSpecificationSchemes, apiDefinitionSchemes), ProblemLevel.ERROR));
        }

        String apiSpecificationPath = swaggerApiSpecification.getSwaggerDefinition().getBasePath();
        String apiDefinitionPath = swaggerApiDefinition.getSwaggerDefinition().getBasePath();
        if (!apiSpecificationPath.equals(apiDefinitionPath)) {
            result.addProblem(new ValidationProblem(MessageFormat.format(MessageConstants.WRONG_HOST_PATH, apiSpecificationPath, apiDefinitionPath), ProblemLevel.ERROR));
        }

        return result;
    }
}
