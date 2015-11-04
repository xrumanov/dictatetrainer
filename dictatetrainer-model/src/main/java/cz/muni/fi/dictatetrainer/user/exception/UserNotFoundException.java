package cz.muni.fi.dictatetrainer.user.exception;

import javax.ejb.ApplicationException;

/**
 * Exception thrown when user is not found in the database
 */
@ApplicationException
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -60289524691210822L;

}