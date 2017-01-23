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
import org.yaml.snakeyaml.scanner.ScannerException;
import pl.jcommerce.apicat.contract.ApiDefinition;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;

import java.io.File;
import java.io.IOException;

public class SwaggerApiDefinition extends ApiDefinition {

    public static final String TYPE = "SWAGGER";

    @Getter
    @Setter
    private Swagger swaggerDefinition;

    @Getter
    @Setter
    private JsonNode jsonNode;

    public void generateSwaggerFromContent() {
        if ((getContent() != null) && (getSwaggerDefinition() == null)) {
            if (jsonNode == null) {
                setJsonNode(createJsonNode(getJson(getContent())));
            }
            setSwaggerDefinition(new SwaggerParser().read(jsonNode));
        }
    }

    public static SwaggerApiDefinition empty() {
        return new SwaggerApiDefinition();
    }

    public static SwaggerApiDefinition fromContent(String content) {
        SwaggerApiDefinition swaggerApiDefinition = createSwaggerApiDefinition(createJsonNode(getJson(content)));
        swaggerApiDefinition.setContent(content);
        return swaggerApiDefinition;
    }

    public static SwaggerApiDefinition fromPath(String path) {
        ClassLoader classLoader = SwaggerApiDefinition.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        String content;
        try {
            content = FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new ApicatSystemException(ErrorCode.READ_FILE_EXCEPTION, e.getMessage());
        }

        SwaggerApiDefinition swaggerApiDefinition = createSwaggerApiDefinition(createJsonNode(getJson(content)));
        swaggerApiDefinition.setContent(content);
        return swaggerApiDefinition;
    }

    private static JsonNode createJsonNode(String content) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
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
            } catch (ParserException | ScannerException e) {
                throw new ApicatSystemException(ErrorCode.PARSE_INPUT_DATA_EXCEPTION, e.getMessage());
            }
        }
        return content;
    }

    private static SwaggerApiDefinition createSwaggerApiDefinition(JsonNode node) {
        Swagger swaggerDefinition;
        try {
            swaggerDefinition = new SwaggerParser().read(node);
        } catch (IllegalArgumentException e) {
            throw new ApicatSystemException(ErrorCode.PARSE_JSON_EXCEPTION, e.getMessage());
        }
        SwaggerApiDefinition swaggerApiDefinition = new SwaggerApiDefinition();
        swaggerApiDefinition.setSwaggerDefinition(swaggerDefinition);
        swaggerApiDefinition.setJsonNode(node);
        return swaggerApiDefinition;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
