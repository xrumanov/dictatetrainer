package cz.muni.fi.dictatetrainer.school.exception;

import javax.ejb.ApplicationException;

/**
 * Exception thrown when school already exists in the database
 */
@ApplicationException
public class SchoolExistentException extends RuntimeException {
    private static final long serialVersionUID = 7404502730684016240L;
}