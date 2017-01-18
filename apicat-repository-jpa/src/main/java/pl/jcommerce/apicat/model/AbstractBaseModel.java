package pl.jcommerce.apicat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

/**
 * Created by prho on 17.01.17.
 */
public abstract class AbstractBaseModel {

    @Id
    @Getter
    @Setter
    Long id;

}
