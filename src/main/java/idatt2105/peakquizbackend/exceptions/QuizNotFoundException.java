package idatt2105.peakquizbackend.exceptions;

public class QuizNotFoundException extends RuntimeException {
  public QuizNotFoundException(){
    super("Quiz not found");
  }
}
