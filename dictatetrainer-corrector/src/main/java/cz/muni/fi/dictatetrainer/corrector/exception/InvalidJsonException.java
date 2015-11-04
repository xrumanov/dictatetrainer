package cz.muni.fi.dictatetrainer.corrector.exception;

/**
 * Exception thrown when invalid JSON is given to a JsonReader
 */
public class InvalidJsonException extends RuntimeException {
    private static final long serialVersionUID = -2027318089557991810L;

    public InvalidJsonException(final String message) {
        super(message);
    }

    public InvalidJsonException(final Throwable throwable) {
        super(throwable);
    }

}