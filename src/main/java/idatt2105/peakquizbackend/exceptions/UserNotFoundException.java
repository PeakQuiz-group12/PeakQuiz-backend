package idatt2105.peakquizbackend.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("User not found");
  }

}
