package pl.jcommerce.apicat.contract.swagger.apispecification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONValue;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;
import org.yaml.snakeyaml.scanner.ScannerException;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;

import java.io.File;
import java.io.IOException;

@AutoService(ApiSpecification.class)
public class SwaggerApiSpecification extends ApiSpecification {

    public static final String TYPE = "Swagger";

    @Getter
    @Setter
    private Swagger swaggerDefinition;

    @Getter
    @Setter
    private JsonNode jsonNode;

    public static SwaggerApiSpecification fromContent(String content) {
        return createSwaggerApiSpecification(createJsonNode(getJson(content)));
    }

    public static SwaggerApiSpecification fromPath(String path) {
        ClassLoader classLoader = SwaggerApiSpecification.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        String content;
        try {
            content = FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new ApicatSystemException(ErrorCode.READ_FILE_EXCEPTION, e.getMessage());
        }
        return createSwaggerApiSpecification(createJsonNode(getJson(content)));
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

    private static String getJson(String content) throws ApicatSystemException {
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

    private static SwaggerApiSpecification createSwaggerApiSpecification(JsonNode node) {
        Swagger swaggerSpecification = new SwaggerParser().read(node);
        SwaggerApiSpecification swaggerApiSpecification = new SwaggerApiSpecification();
        swaggerApiSpecification.setSwaggerDefinition(swaggerSpecification);
        swaggerApiSpecification.setJsonNode(node);
        return swaggerApiSpecification;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
