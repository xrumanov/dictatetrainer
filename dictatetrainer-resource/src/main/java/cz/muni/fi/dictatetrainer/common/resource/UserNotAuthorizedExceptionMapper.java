package cz.muni.fi.dictatetrainer.common.resource;

import cz.muni.fi.dictatetrainer.common.exception.UserNotAuthorizedException;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Map the UserNotAuthorizedException to the response when user is not authorized
 */
@Provider
public class UserNotAuthorizedExceptionMapper implements ExceptionMapper<UserNotAuthorizedException> {

    @Override
    public Response toResponse(final UserNotAuthorizedException exception) {
        return Response.status(HttpCode.FORBIDDEN.getCode()).build();
    }

}