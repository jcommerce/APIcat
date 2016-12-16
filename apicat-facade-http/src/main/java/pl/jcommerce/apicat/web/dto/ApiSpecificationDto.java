package pl.jcommerce.apicat.web.dto;

import lombok.Data;

@Data
public class ApiSpecificationDto {
    public Long id;
    public String name;
    private String version;
    private String content;
    private String author;
}
