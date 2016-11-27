package pl.jcommerce.apicat.contract.swagger;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import lombok.*;
import pl.jcommerce.apicat.contract.ApiSpecification;


class SwaggerApiSpecification extends ApiSpecification {

    @Getter
    @Setter
    private Swagger swaggerDefinition;

    public static ApiSpecification fromContent(String content) {
        Swagger swaggerDefinition = new SwaggerParser().parse(content);
        if (swaggerDefinition == null)
            throw new SwaggerOpenAPISpecificationException();
        SwaggerApiSpecification swaggerApiSpecification = new SwaggerApiSpecification();
        swaggerApiSpecification.setSwaggerDefinition(swaggerDefinition);
        return swaggerApiSpecification;
    }

    public static ApiSpecification fromPath(String path) {
        Swagger swaggerDefinition = new SwaggerParser().read(path);
        if (swaggerDefinition == null)
            throw new SwaggerOpenAPISpecificationException();
        SwaggerApiSpecification swaggerApiSpecification = new SwaggerApiSpecification();
        swaggerApiSpecification.setSwaggerDefinition(swaggerDefinition);
        return swaggerApiSpecification;
    }
}