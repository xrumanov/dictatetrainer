package cz.muni.fi.dictatetrainer.user.exception;

import javax.ejb.ApplicationException;

/**
 * Exception thrown when user already exists in the database
 */
@ApplicationException
public class UserExistentException extends RuntimeException {
    private static final long serialVersionUID = 8706762471635372429L;

}