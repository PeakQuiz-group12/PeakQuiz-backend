package idatt2105.peakquizbackend.exceptions;

/**
 * Exception thrown when a requested quiz is not found.
 */
public class QuizNotFoundException extends RuntimeException {

    /**
     * Constructs a new QuizNotFoundException with the default error message.
     */
    public QuizNotFoundException() {
        super("Quiz not found");
    }
}
