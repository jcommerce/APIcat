package pl.jcommerce.apicat.contract.validation.result;

import lombok.Getter;
import pl.jcommerce.apicat.contract.validation.problem.ProblemLevel;
import pl.jcommerce.apicat.contract.validation.problem.ValidationProblem;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    @Getter
    private ValidationResultCategory validationResultCategory = ValidationResultCategory.CORRECT;

    @Getter
    private List<ValidationProblem> problemList = new ArrayList<>();

    public void merge(ValidationResult anotherResult) {
        this.problemList.addAll(anotherResult.getProblemList());

        if (anotherResult.getValidationResultCategory() == ValidationResultCategory.ERROR) {
            validationResultCategory = ValidationResultCategory.ERROR;
        }
        if ((anotherResult.getValidationResultCategory() == ValidationResultCategory.WARN) && (validationResultCategory != ValidationResultCategory.ERROR)) {
            validationResultCategory = ValidationResultCategory.WARN;
        }
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
