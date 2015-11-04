package cz.muni.fi.dictatetrainer.category.exception;

import javax.ejb.ApplicationException;

/**
 * Exception thrown when category already exists in the database
 */
@ApplicationException
public class CategoryExistentException extends RuntimeException {
    private static final long serialVersionUID = -3997070012901203885L;

}