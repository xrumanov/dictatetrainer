/**
 * Is thrown when there is an attempt to create user that already exists
 */
package cz.muni.fi.dictatetrainer.user.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class UserExistentException extends RuntimeException {
    private static final long serialVersionUID = 8706762471635372429L;

}