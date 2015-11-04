package cz.muni.fi.dictatetrainer.common.exception;

/**
 * Exception thrown when invalid JSON is given to a JsonReader
 */
public class InvalidJsonException extends RuntimeException {
    private static final long serialVersionUID = 6087454351913028554L;

    public InvalidJsonException(final String message) {
        super(message);
    }

    public InvalidJsonException(final Throwable throwable) {
        super(throwable);
    }

}
