package idatt2105.peakquizbackend.exceptions;

/**
 * Exception thrown when attempting to create a quiz that already exists.
 */
public class QuizAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new QuizAlreadyExistsException with the default error message.
     */
    public QuizAlreadyExistsException() {
        super("Quiz already exists");
    }
}
