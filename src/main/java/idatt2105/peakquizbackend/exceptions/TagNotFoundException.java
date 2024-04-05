package idatt2105.peakquizbackend.exceptions;

public class TagNotFoundException extends RuntimeException {
  public TagNotFoundException() {
    super("Tag not found");
  }
}
