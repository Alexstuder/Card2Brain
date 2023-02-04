package ch.zhaw.card2brain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class represents a custom exception for handling a user not found scenario.
 *
 * @author Alexander Studer
 * @author Niklaus Hänggi
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 *
 * @see ResponseStatus Specifies the HTTP status code that should be returned for this exception type.
 * value = HttpStatus.NOT_FOUND - Indicates that the requested resource could not be found.
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
