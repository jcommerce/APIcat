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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValidationProblem problem = (ValidationProblem) o;

        return message.equals(problem.message) && problemLevel == problem.problemLevel;
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + problemLevel.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ValidationProblem{" +
                "problemLevel=" + problemLevel +
                ", message='" + message + '\'' +
                '}';
    }
}
