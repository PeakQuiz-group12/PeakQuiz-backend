package idatt2105.peakquizbackend.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private void logError(Exception ex) {
        LOGGER.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
    }

    @ExceptionHandler(value = { QuizAlreadyExistsException.class, UserAlreadyExistsException.class })
    public ResponseEntity<String> handleObjectAlreadyExistException(Exception ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = { QuizNotFoundException.class, UserNotFoundException.class })
    public ResponseEntity<String> handleObjectDoesNotExistException(Exception ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

  @ExceptionHandler(value = {
      IllegalArgumentException.class,
      NullPointerException.class,
      MissingServletRequestParameterException.class,
      HttpRequestMethodNotSupportedException.class
  })
  public ResponseEntity<String> handleBadInputException(Exception ex) {
    logError(ex);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getClass().getSimpleName());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
    List<String> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getMessage());
    }

    String errorMessage = String.join(", ", errors);
    return ResponseEntity.badRequest().body(errorMessage);
  }

  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<String> handleRemainderExceptions(Exception ex) {
    logError(ex);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ex.getClass().getSimpleName());
  }
}
