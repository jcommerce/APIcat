package pl.jcommerce.apicat.contract.swagger.apispecification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;
import pl.jcommerce.apicat.contract.ApiSpecification;
import pl.jcommerce.apicat.contract.exception.ApicatSystemException;
import pl.jcommerce.apicat.contract.exception.ErrorCode;
import pl.jcommerce.apicat.contract.swagger.SwaggerOpenAPISpecificationException;

import java.io.File;
import java.io.IOException;

@Slf4j
public class SwaggerApiSpecification extends ApiSpecification {

    public static String TYPE = "Swagger";
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
        String content = null;
        try {
            content = FileUtils.readFileToString(file);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Cannot read file " + path);
        }
        return createSwaggerApiSpecification(createJsonNode(getJson(content)));
    }

    private static JsonNode createJsonNode(String content) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(content);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Cannot parse Json");
        }
        return node;
    }

    private static String getJson(String content) throws ApicatSystemException {

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

    private static SwaggerApiSpecification createSwaggerApiSpecification(JsonNode node) {
        Swagger swaggerSpecification = new SwaggerParser().read(node);
        if (swaggerSpecification == null) {
            throw new SwaggerOpenAPISpecificationException();
        }
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
