package ch.zhaw.card2brain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Exception to indicate that email already exists and must be unique.
 *
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class EmailHasToBeUniqueException extends RuntimeException {
    public EmailHasToBeUniqueException(String message) {
        super(message);
    }
}
