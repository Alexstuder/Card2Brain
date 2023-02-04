package ch.zhaw.card2brain.exception;

import ch.zhaw.card2brain.util.HasLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 The {@code ExceptionHandlerAdvice} class provides a centralized exception handling mechanism for the application.
 It implements the {@link HasLogger} interface which provides the logging functionality.
 It has several {@code @ExceptionHandler} methods which handle specific exceptions and return a response
 with appropriate HTTP status code and error message.
 <p>For example, the {@code handleException} method for {@link UserNotValidException} returns a response with
 HTTP status code 406 (NOT_ACCEPTABLE) and the error message from the exception.
 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
*/


@ControllerAdvice
public class ExceptionHandlerAdvice implements HasLogger {

    /**
     * Handles the {@link UserNotValidException} and returns error message with status code 406.
     *
     * @param e the exception to be handled
     * @return the response entity with error message and status code
     */

    @ExceptionHandler(UserNotValidException.class)
    public ResponseEntity<String> handleException(UserNotValidException e) {
        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Error: " + e.getMessage());
    }
    /**
     * Handles the {@link CategoryNotValidException} and returns error message with status code 406.
     *
     * @param e the exception to be handled
     * @return the response entity with error message and status code
     */

    @ExceptionHandler(CategoryNotValidException.class)
    public ResponseEntity<String> handleException(CategoryNotValidException e) {
        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Error: " + e.getMessage());
    }

    /**
     * Handles the {@link CardNotValidException} and returns error message with status code 406.
     *
     * @param e the exception to be handled
     * @return the response entity with error message and status code
     */


    @ExceptionHandler(CardNotValidException.class)
    public ResponseEntity<String> handleException(CardNotValidException e) {
        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Error: " + e.getMessage());
    }

    /**
     * Handles the {@link PasswordNotValidException} and returns error message with status code 406.
     *
     * @param e the exception to be handled
     * @return the response entity with error message and status code
     */
    @ExceptionHandler(PasswordNotValidException.class)
    public ResponseEntity<String> handleException(PasswordNotValidException e) {
        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Error: " + e.getMessage());
    }


    /**
     * Handles the {@link CardAlreadyExistsException} and returns error message with status code 409.
     *
     * @param e the exception to be handled
     * @return the response entity with error message and status code
     */
    @ExceptionHandler(CardAlreadyExistsException.class)
    public ResponseEntity<String> handleException(CardAlreadyExistsException e) {
        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Error: " + e.getMessage());
    }

    /**
     * Handles the {@link UserNotFoundException} and returns error message with status code 404.
     *
     * @param e the exception to be handled
     * @return the response entity with error message and status code
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleException(UserNotFoundException e) {
        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
    }

    /**
     * Handles CategoryNotFoundException and returns a response with HTTP status code NOT_FOUND.
     *
     * @param e the CategoryNotFoundException to be handled
     * @return a ResponseEntity with the error message and HTTP status code NOT_FOUND
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleException(CategoryNotFoundException e) {

        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
    }

    /**
     * Handles UserAlreadyExistsException and returns a response with HTTP status code CONFLICT.
     *
     * @param e the UserAlreadyExistsException to be handled
     * @return a ResponseEntity with the error message and HTTP status code CONFLICT
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleException(UserAlreadyExistsException e) {
        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Error: " + e.getMessage());
    }

    /**
     * Handles CategoryAlreadyExistsException and returns a response with HTTP status code CONFLICT.
     *
     * @param e the CategoryAlreadyExistsException to be handled
     * @return a ResponseEntity with the error message and HTTP status code CONFLICT
     */

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<String> handleException(CategoryAlreadyExistsException e) {
        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Error: " + e.getMessage());
    }


    /**
     * Handles EmailHasToBeUniqueException and returns a response with HTTP status code CONFLICT.
     *
     * @param e the EmailHasToBeUniqueException to be handled
     * @return a ResponseEntity with the error message and HTTP status code CONFLICT
     */
    @ExceptionHandler(EmailHasToBeUniqueException.class)
    public ResponseEntity<String> handleException(EmailHasToBeUniqueException e) {
        getLogger().error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Error: " + e.getMessage());
    }


}

