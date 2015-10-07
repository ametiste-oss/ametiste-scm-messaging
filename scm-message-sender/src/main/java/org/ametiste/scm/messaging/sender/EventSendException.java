package org.ametiste.scm.messaging.sender;

/**
 * {@code EventSendException} signals about any problem or error occurred during event send process.
 */
public class EventSendException extends RuntimeException {

    public EventSendException(String message) {
        super(message);
    }

    public EventSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
