/**
 * Is thrown when there is no user matching the criteria
 */
package cz.muni.fi.dictatetrainer.user.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -60289524691210822L;

}