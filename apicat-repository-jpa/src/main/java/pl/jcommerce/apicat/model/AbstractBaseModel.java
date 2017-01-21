package pl.jcommerce.apicat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractBaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "modification_date")
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

