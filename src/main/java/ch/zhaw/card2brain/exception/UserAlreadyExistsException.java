package ch.zhaw.card2brain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * This class represents a custom exception for handling a scenario where a user already exists.
 *
 * @author Alexander Studer
 * @author Niklaus Hänggi
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 *
 * @see java.lang.RuntimeException
 *
 * @see ResponseStatus Specifies the HTTP status code that should be returned for this exception type.
 * value = HttpStatus.NOT_ACCEPTABLE - Indicates that the request cannot be processed due to a conflict with the current state of the resource.
 */

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}