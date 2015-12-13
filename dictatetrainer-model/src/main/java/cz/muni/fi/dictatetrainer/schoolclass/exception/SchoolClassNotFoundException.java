package cz.muni.fi.dictatetrainer.schoolclass.exception;

import javax.ejb.ApplicationException;

/**
 * Exception thrown when school class is not found in the database
 */
@ApplicationException
public class SchoolClassNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -7845456142783445243L;
}
