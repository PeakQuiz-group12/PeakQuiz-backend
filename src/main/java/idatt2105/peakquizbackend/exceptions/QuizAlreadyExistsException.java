package idatt2105.peakquizbackend.exceptions;

public class QuizAlreadyExistsException extends RuntimeException {
  public QuizAlreadyExistsException() {
    super("Quiz already exists");
  }
}
