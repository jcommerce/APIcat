package pl.jcommerce.apicat.contract.swagger.apidefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONValue;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;

import java.io.File;
import java.io.IOException;


/**
 * Created by krka on 31.10.2016.
 */
public class SwaggerApiDefinition extends ApiDefinition {

    @Getter
    @Setter
    private Swagger swaggerDefinition;

    @Getter
    @Setter
    private JsonNode jsonNode;

    public static SwaggerApiDefinition empty() {
        return new SwaggerApiDefinition();
    }

    public static SwaggerApiDefinition fromContent(String content) {
        return createSwaggerApiDefinition(createJsonNode(getJson(content)));
    }

    public static SwaggerApiDefinition fromPath(String path) {

        ClassLoader classLoader = SwaggerApiDefinition.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        String content = null;
        try {
            content = FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new ApicatSystemException(ErrorCode.READ_FILE_EXCEPTION, e.getMessage());
        }

        return createSwaggerApiDefinition(createJsonNode(getJson(content)));
    }

    private static JsonNode createJsonNode(String content) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(content);
        } catch (IOException e) {
            throw new ApicatSystemException(ErrorCode.PARSE_JSON_EXCEPTION, e.getMessage());
        }
        return node;
    }

    private static String getJson(String content) {
        if (!content.trim().startsWith("{")) {
            Yaml yaml = new Yaml();
            try {
                Object obj = yaml.load(content);
                content = JSONValue.toJSONString(obj);
            } catch (ParserException e) {
                throw new ApicatSystemException(ErrorCode.PARSE_INPUT_DATA_EXCEPTION, e.getMessage());
            }
        }
        return content;
    }

    private static SwaggerApiDefinition createSwaggerApiDefinition(JsonNode node) {
        Swagger swaggerDefinition = new SwaggerParser().read(node);
        SwaggerApiDefinition swaggerApiDefinition = new SwaggerApiDefinition();
        swaggerApiDefinition.setSwaggerDefinition(swaggerDefinition);
        swaggerApiDefinition.setJsonNode(node);
        return swaggerApiDefinition;
    }


//    @Override
//    public void validateAllContracts()  {
//        for (ApiContract apiContract : apiContracts) {
//            if (!validateContract(apiContract)) {
//                valid = false;
//                break;
//            }
//        }
//    }

//    private boolean validateContract(ApiContract apiContract) {
//        Optional<ApiContractValidator> apiContractValidator = apiContractValidators.stream().filter(a -> a.support(apiContract)).findAny();
//        return !(apiContractValidator.isPresent() && !apiContractValidator.get().validate(apiContract));
//    }

//    @Override
//    public void validateDefinition() {
//        Optional<ApiDefinitionValidator> apiDefinitionValidator = validators.stream().filter(a -> a.support(this)).findAny();
//        if (apiDefinitionValidator.isPresent()) {
//            if(!apiDefinitionValidator.get().validate(this))
//                valid = false;
//        } else
//            valid = false;
//    }

//    @Override
//    public void validateSpecifications(ApiDefinition apiDefinition, ApiSpecification apiSpecification) {
//        Optional<ApiContractValidator> apiContractValidator = apiContractValidators.stream().filter(a -> a.support(apiDefinition, apiSpecification)).findAny();
//        if (apiContractValidator.isPresent()) {
//            if(!apiContractValidator.get().validate(apiDefinition, apiSpecification))
//                valid = false;
//        } else
//            valid = false;
//    }
}