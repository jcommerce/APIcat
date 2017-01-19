package pl.jcommerce.apicat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Created by prho on 17.01.17.
 */

@Entity
@Getter
@Setter
public class ApiContractModel extends AbstractBaseModel {

    @ManyToOne
    private ApiDefinitionModel apiDefinitionModel;

    @OneToOne
    private ApiSpecificationModel apiSpecificationModel;

}
