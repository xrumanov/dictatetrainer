/**
 * Exception thrown when trial is not found in the database
 */
package cz.muni.fi.dictatetrainer.trial.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class TrialNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -7173034361533648175L;
}
