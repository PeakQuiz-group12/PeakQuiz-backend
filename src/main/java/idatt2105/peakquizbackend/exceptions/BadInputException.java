package idatt2105.peakquizbackend.exceptions;

/**
 * Exception thrown when the input provided by the user is invalid or insufficient.
 */
public class BadInputException extends RuntimeException {

    /**
     * Constructs a new BadInputException with the specified detail message.
     * @param message the detail message
     */
    public BadInputException(String message) {
        super(message);
    }
}
