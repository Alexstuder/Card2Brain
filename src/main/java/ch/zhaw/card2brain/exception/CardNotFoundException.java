package ch.zhaw.card2brain.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**

 A custom exception class for signaling that a card could not be found.
 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.12.2023
 */



@ResponseStatus(value = HttpStatus.NOT_FOUND)
 /**
 Constructs a new exception with the specified detail message.
 @param message The detail message.
 */
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String message) {
        super(message);
    }

}
