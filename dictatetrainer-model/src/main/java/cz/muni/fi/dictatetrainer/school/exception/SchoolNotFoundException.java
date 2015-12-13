package cz.muni.fi.dictatetrainer.school.exception;

import javax.ejb.ApplicationException;

/**
 * Exception thrown when school is not found in the database
 */
@ApplicationException
public class SchoolNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5264144157066717100L;
}