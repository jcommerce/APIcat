package pl.jcommerce.apicat.contract.swagger;

import com.google.auto.service.AutoService;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.contract.ApiSpecification;

@AutoService(ApiSpecification.class)
public class SwaggerApiSpecification extends ApiSpecification {

    public static String TYPE = "Swagger";
    @Getter
    @Setter
    private Swagger swaggerDefinition;

    public static ApiSpecification fromContent(String content) {
        Swagger swaggerDefinition = new SwaggerParser().parse(content);
        if (swaggerDefinition == null) {
            throw new SwaggerOpenAPISpecificationException();
        }
        SwaggerApiSpecification swaggerApiSpecification = new SwaggerApiSpecification();
        swaggerApiSpecification.setSwaggerDefinition(swaggerDefinition);
        return swaggerApiSpecification;
    }

    public static ApiSpecification fromPath(String path) {
        Swagger swaggerDefinition = new SwaggerParser().read(path);
        if (swaggerDefinition == null) {
            throw new SwaggerOpenAPISpecificationException();
        }
        SwaggerApiSpecification swaggerApiSpecification = new SwaggerApiSpecification();
        swaggerApiSpecification.setSwaggerDefinition(swaggerDefinition);
        return swaggerApiSpecification;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
