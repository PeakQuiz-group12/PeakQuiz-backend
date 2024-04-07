package idatt2105.peakquizbackend.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler to handle different types of exceptions across the application.
 * It provides centralized exception handling for various types of exceptions that may occur
 * during the execution of the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Log the error to the logger.
     *
     * @param ex The exception to be logged.
     */
    private void logError(Exception ex) {
        LOGGER.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
    }

    /**
     * Handle exceptions related to existing objects.
     *
     * @param ex The exception indicating that an object already exists.
     * @return ResponseEntity with an appropriate HTTP status code and error message.
     */
    @ExceptionHandler(value = {QuizAlreadyExistsException.class, UserAlreadyExistsException.class})
    public ResponseEntity<String> handleObjectAlreadyExistException(Exception ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handle exceptions related to non-existing objects.
     *
     * @param ex The exception indicating that an object does not exist.
     * @return ResponseEntity with an appropriate HTTP status code and error message.
     */
    @ExceptionHandler(value = { QuizNotFoundException.class, UserNotFoundException.class, TagNotFoundException.class,
            CategoryNotFoundException.class, QuizNotFoundException.class, })
    public ResponseEntity<String> handleObjectDoesNotExistException(Exception ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handle exceptions related to bad input or invalid requests.
     *
     * @param ex The exception indicating bad input or invalid request.
     * @return ResponseEntity with an appropriate HTTP status code and error message.
     */
    @ExceptionHandler(value = { IllegalArgumentException.class, NullPointerException.class, BadInputException.class,
            MissingServletRequestParameterException.class, HttpRequestMethodNotSupportedException.class,
            DataIntegrityViolationException.class, MessagingException.class })
    public ResponseEntity<String> handleBadInputException(Exception ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handle exceptions related to constraint violations in validation.
     *
     * @param ex The exception indicating constraint violations during validation.
     * @return ResponseEntity with an appropriate HTTP status code and error message.
     */
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

    /**
     * Handle any remaining exceptions that are not explicitly handled.
     *
     * @param ex The exception that is not explicitly handled.
     * @return ResponseEntity with an appropriate HTTP status code and error message.
     */
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<String> handleRemainderExceptions(Exception ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getClass().getSimpleName());
    }
}
