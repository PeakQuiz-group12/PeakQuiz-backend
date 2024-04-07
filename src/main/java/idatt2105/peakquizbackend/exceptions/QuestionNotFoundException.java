package idatt2105.peakquizbackend.exceptions;

/**
 * Exception thrown when attempting to find a question that does not exist.
 */
public class QuestionNotFoundException extends RuntimeException {

    /**
     * Constructs a new QuestionNotFoundException with the default error message.
     */
    public QuestionNotFoundException() {
        super("Question not found");
    }
}