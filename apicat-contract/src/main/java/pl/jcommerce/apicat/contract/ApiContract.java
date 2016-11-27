package pl.jcommerce.apicat.contract;


import lombok.Getter;
import lombok.Setter;


public class ApiContract {

    //TODO: adjust to model


    @Getter @Setter
    private ApiSpecification apiSpecification;

    @Getter @Setter
    private ApiDefinition apiDefinition;
}



