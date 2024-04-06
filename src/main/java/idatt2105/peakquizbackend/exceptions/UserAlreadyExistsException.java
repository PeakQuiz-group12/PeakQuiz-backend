package idatt2105.peakquizbackend.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException() {
    super("User already exists");
  }
}
