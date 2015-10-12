package cz.muni.fi.dictatetrainer.common.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class UserNotAuthorizedException extends RuntimeException {
    private static final long serialVersionUID = -1449722059595947793L;

}