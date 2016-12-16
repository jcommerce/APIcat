package pl.jcommerce.apicat.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * Created by jada on 07.12.2016.
 */

@Getter
@Setter
@Entity(name = "specification")
public class ApiSpecificationEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    private String author;

    @Column(nullable = false)
    private String version;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @OneToOne(mappedBy = "specification", fetch = FetchType.LAZY)
    private ApiContractEntity contract;
}