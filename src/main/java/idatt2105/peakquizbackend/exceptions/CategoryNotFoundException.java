package idatt2105.peakquizbackend.exceptions;

public class CategoryNotFoundException extends RuntimeException {
  public CategoryNotFoundException() {
    super("Category does not exist");
  }
}
