package pl.jcommerce.apicat.contract.validation.result;

import lombok.Getter;
import pl.jcommerce.apicat.contract.validation.problem.ProblemLevel;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    @Getter
    private ValidationResultCategory validationResultCategory;

    @Getter
    private List<ValidationProblem> problemList = new ArrayList<>();

    public void merge(ValidationResult anotherResult) {
        //TODO: implement me.
        //Set validation result category: if any problem with error level -> error, if only warnings - warn category, if no problems - CORRECT
    }

    public void addProblem(ValidationProblem problem) {
        problemList.add(problem);
        if (problem.getProblemLevel() == ProblemLevel.ERROR) {
            validationResultCategory = ValidationResultCategory.ERROR;
        }
        if ((problem.getProblemLevel() == ProblemLevel.WARN) && (validationResultCategory != ValidationResultCategory.ERROR)) {
            validationResultCategory = ValidationResultCategory.WARN;
        }
    }
}
