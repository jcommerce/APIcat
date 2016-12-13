package pl.jcommerce.apicat.contract.validation.problem;

import lombok.Getter;

public class ValidationProblem {

    @Getter
    private String message;
    @Getter
    private ProblemLevel problemLevel;

    public ValidationProblem(String message) {
        this(message, ProblemLevel.ERROR);
    }

    public ValidationProblem(String message, ProblemLevel level) {
        this.message = message;
        this.problemLevel = level;
    }

}
