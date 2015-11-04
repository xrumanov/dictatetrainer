package cz.muni.fi.dictatetrainer.category.exception;

import javax.ejb.ApplicationException;

/**
 * Exception thrown when category is not found in the database
 */
@ApplicationException
public class CategoryNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 6068378752576978396L;

}