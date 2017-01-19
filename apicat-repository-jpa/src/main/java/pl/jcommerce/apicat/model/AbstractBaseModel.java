package pl.jcommerce.apicat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by prho on 17.01.17.
 */
@Getter
@Setter
public abstract class AbstractBaseModel {

    @Id
    Long id;

    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "MODIFICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @PrePersist
    protected void onPersist() {
        if (creationDate == null) {
            creationDate = new Date();
        }
        if (modificationDate == null) {
            modificationDate = new Date();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modificationDate = new Date();
    }
}

