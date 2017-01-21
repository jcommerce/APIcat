package pl.jcommerce.apicat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class ApiDefinitionModel extends AbstractBaseModel {

    @OneToMany
    private List<ApiContractModel> apiContractModels;

    @Column
    private String name;

    @Column
    private String version;

    @Column
    private String stage;

    @Column
    private String author;

    @Column
    private String content;

    @Column
    public String type;

}
