package pl.jcommerce.apicat.contract.swagger.validation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krka on 23.10.2016.
 */
@Data
public class DiffDetails {

    private final List<String> differences = new ArrayList<>();

    void addDifference(String difference) {
        this.differences.add(difference);
    }

}
