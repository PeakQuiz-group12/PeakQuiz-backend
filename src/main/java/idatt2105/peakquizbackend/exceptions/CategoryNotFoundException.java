package idatt2105.peakquizbackend.exceptions;

/**
 * Exception thrown when a category does not exist.
 */
public class CategoryNotFoundException extends RuntimeException {

    /**
     * Constructs a new CategoryNotFoundException with a default message.
     */
    public CategoryNotFoundException() {
        super("Category does not exist");
    }
}
