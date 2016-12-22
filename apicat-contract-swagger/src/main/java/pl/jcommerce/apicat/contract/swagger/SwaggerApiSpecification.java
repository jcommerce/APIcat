package pl.jcommerce.apicat.contract.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.ApiSpecification;

import java.io.File;
import java.io.IOException;

@AutoService(ApiSpecification.class)
public class SwaggerApiSpecification extends ApiSpecification {

    public static String TYPE = "Swagger";
    @Getter
    @Setter
    private Swagger swaggerDefinition;

    @Getter
    @Setter
    private JsonNode jsonNode;

    public static ApiSpecification fromContent(String content) {
        Swagger swaggerSpecification = new SwaggerParser().parse(content);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try{
            node = mapper.readTree(content);
        }catch (IOException e){
            e.printStackTrace();
        }

        return  createSwaggerApiSpecification(node);
    }

    public static ApiSpecification fromPath(String path) {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = SwaggerApiSpecification.class.getClassLoader();
        JsonNode node = null;
        try{
            node = mapper.readTree(new File(classLoader.getResource(path).getFile()));
        }catch (IOException e){
            e.printStackTrace();
        }

        return  createSwaggerApiSpecification(node);
    }

    private static SwaggerApiSpecification createSwaggerApiSpecification(JsonNode node){
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
