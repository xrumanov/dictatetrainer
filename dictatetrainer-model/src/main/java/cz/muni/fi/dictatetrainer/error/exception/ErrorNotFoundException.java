/**
 * Exception thrown when error is not found in the database
 */
package cz.muni.fi.dictatetrainer.error.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class ErrorNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -7468041142348861919L;
}
