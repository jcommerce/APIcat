package pl.jcommerce.apicat.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jada on 08.12.2016.
 */

@Getter
@Setter
@Entity(name = "contractValidationDetails")
public class ApiContractValidationDetailsEntity extends BaseEntity {
    @OneToOne
    private ApiContractEntity contract;

    @ElementCollection
    @CollectionTable(name = "ApiDifferences", joinColumns = @JoinColumn(name = "validationDetailsId"))
    @Column(name = "difference")
    private Set<String> differences = new HashSet<>();

    @Column(nullable = false)
    private boolean valid;
}
