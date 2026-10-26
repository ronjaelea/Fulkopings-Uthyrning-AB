package se.gritacademy.fulkopingsuthyrning.exception;

/**
 * kastar i service när t.ex. produkten redan uthyrd, email redan registrerad...
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
