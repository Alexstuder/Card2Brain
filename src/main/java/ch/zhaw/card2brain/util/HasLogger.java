package ch.zhaw.card2brain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface provides a default method for obtaining a logger instance.
 * The logger is obtained using the {@link LoggerFactory#getLogger(Class)} method,
 * with the class of the implementing class passed as an argument.
 */
public interface HasLogger {

    /**
     Returns a logger instance for the current class.
     @return the logger instance
     */

    default Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }
}
