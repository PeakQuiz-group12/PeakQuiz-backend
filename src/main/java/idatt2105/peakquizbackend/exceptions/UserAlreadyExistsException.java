package idatt2105.peakquizbackend.exceptions;

/**
 * Exception thrown when attempting to create a user that already exists in the system.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistsException with the default error message.
     */
    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
