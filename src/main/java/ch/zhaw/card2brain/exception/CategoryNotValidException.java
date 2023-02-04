package ch.zhaw.card2brain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class CategoryNotValidException extends RuntimeException {
    public CategoryNotValidException(String message) {
        super(message);
    }
}