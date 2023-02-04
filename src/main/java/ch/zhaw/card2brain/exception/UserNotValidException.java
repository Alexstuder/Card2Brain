package ch.zhaw.card2brain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Custom exception class that represents a response status of HttpStatus.NOT_ACCEPTABLE.
 * This exception will be thrown when the user provided is not valid.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16-01-2023
 *
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class UserNotValidException extends RuntimeException {
    public UserNotValidException(String message) {
        super(message);
    }
}
