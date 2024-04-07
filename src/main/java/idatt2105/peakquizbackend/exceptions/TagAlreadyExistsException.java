package idatt2105.peakquizbackend.exceptions;

public class TagAlreadyExistsException extends RuntimeException {
  public TagAlreadyExistsException() {
    super("A tag with the provided title already exists for this user");
  }
}
