package cz.muni.fi.dictatetrainer.dictate.exception;

import javax.ejb.ApplicationException;

/**
 * Exception thrown when dictate is not found in the database
 */
@ApplicationException
public class DictateNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 9006354796017342822L;
}
