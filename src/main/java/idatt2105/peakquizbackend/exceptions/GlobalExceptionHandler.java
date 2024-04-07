package idatt2105.peakquizbackend.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(value = { QuizNotFoundException.class, UserNotFoundException.class,
            TagAlreadyExistsException.class })
    public ResponseEntity<String> handleObjectDoesNotExistException(Exception ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, NullPointerException.class })
    public ResponseEntity<String> handleBadInputException(Exception ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getClass().getSimpleName());
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<String> handleRemainderExceptions(Exception ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getClass().getSimpleName());
    }
}
