package pl.jcommerce.apicat.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by jada on 07.12.2016.
 */

@Getter
@Setter
@Entity(name = "contract")
@NoArgsConstructor
public class ApiContractEntity extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ApiDefinitionEntity definition;

    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private ApiSpecificationEntity specification;

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ApiContractValidationDetailsEntity validationDetails;

    public ApiContractEntity(ApiDefinitionEntity definition, ApiSpecificationEntity specification) {
        this.definition = definition;
        this.specification = specification;
    }
}
