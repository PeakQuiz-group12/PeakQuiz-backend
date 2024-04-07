package idatt2105.peakquizbackend.exceptions;

/**
 * Exception thrown when attempting to access a tag that does not exist.
 */
public class TagNotFoundException extends RuntimeException {

    /**
     * Constructs a new TagNotFoundException with the default error message.
     */
    public TagNotFoundException() {
        super("Tag not found");
    }
}
