package pl.jcommerce.apicat.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by jada on 05.12.2016.
 */

@Getter
@MappedSuperclass
public class BaseEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    protected Long id;

    @Column(nullable = false, updatable = false, length = 36)
    protected String uuid = UUID.randomUUID().toString();

    @CreatedDate
    protected LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    protected LocalDateTime updatedDate = LocalDateTime.now();

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object objectToCompare) {
        if (this == objectToCompare) {
            return true;
        }
        if (objectToCompare == null) {
            return false;
        }
        if (!(objectToCompare instanceof BaseEntity)) {
            return false;
        }

        BaseEntity baseEntityToCompare = (BaseEntity) objectToCompare;

        return getUuid().equals(baseEntityToCompare.getUuid());
    }

}
