package pl.jcommerce.apicat.web.dto;

import lombok.Data;
import pl.jcommerce.apicat.model.model.ApiFormat;

@Data
public class ApiDefinitionDto {
    public Long id;
    public String name;
    private String version;
    private String content;
    private String author;
    private ApiFormat format;
}
