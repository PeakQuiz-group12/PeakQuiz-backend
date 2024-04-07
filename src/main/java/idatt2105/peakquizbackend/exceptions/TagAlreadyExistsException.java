package idatt2105.peakquizbackend.exceptions;

/**
 * Exception thrown when attempting to create a tag with a title that already exists for a user.
 */
public class TagAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new TagAlreadyExistsException with the default error message.
     */
    public TagAlreadyExistsException() {
        super("A tag with the provided title already exists for this user");
    }
}
