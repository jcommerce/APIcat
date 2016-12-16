package pl.jcommerce.apicat.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.jcommerce.apicat.model.model.ApiFormat;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jada on 05.12.2016.
 */

@Getter
@Setter
@Entity(name = "definition")
public class ApiDefinitionEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    private String author;

    @Column(nullable = false)
    private String version;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApiFormat format;

    @OneToMany(mappedBy = "definition", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApiContractEntity> contracts = new HashSet<>();
}
