/**
 * Is thrown when the JSON is not valid
 */
package cz.muni.fi.dictatetrainer.common.exception;

public class InvalidJsonException extends RuntimeException {
    private static final long serialVersionUID = 6087454351913028554L;

    public InvalidJsonException(final String message) {
        super(message);
    }

    public InvalidJsonException(final Throwable throwable) {
        super(throwable);
    }

}