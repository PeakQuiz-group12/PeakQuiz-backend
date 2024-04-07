package idatt2105.peakquizbackend.exceptions;

/**
 * Exception thrown when a user is not found in the system.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the default error message.
     */
    public UserNotFoundException() {
        super("User not found");
    }
}
