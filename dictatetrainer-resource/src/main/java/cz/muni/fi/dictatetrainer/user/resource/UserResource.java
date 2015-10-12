/**
 * REST resources for user entity
 */
package cz.muni.fi.dictatetrainer.user.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.json.JsonReader;
import cz.muni.fi.dictatetrainer.common.json.JsonUtils;
import cz.muni.fi.dictatetrainer.common.json.JsonWriter;
import cz.muni.fi.dictatetrainer.common.json.OperationResultJsonWriter;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.common.model.OperationResult;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.model.ResourceMessage;
import cz.muni.fi.dictatetrainer.user.exception.UserExistentException;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import cz.muni.fi.dictatetrainer.user.model.Student;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.model.filter.UserFilter;
import cz.muni.fi.dictatetrainer.user.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;

import static cz.muni.fi.dictatetrainer.common.model.StandardsOperationResults.*;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("user");

    @Inject
    UserServices userServices;

    @Inject
    UserJsonConverter userJsonConverter;

    @Context
    SecurityContext securityContext; // context of the logged user

    @Context
    UriInfo uriInfo;

    @POST
    public Response add(final String body) {
        logger.debug("Adding a new user with body {}", body);
        User user = userJsonConverter.convertFrom(body);
        if (user.getUserType().equals(User.UserType.TEACHER)) {
            return Response.status(HttpCode.FORBIDDEN.getCode()).build();
        }

        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result;
        try {
            user = userServices.add(user);
            //changed from returning id to returning jwt
            result = OperationResult.success(JsonUtils.getJsonElementWithJWT(userJsonConverter, user));
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the user is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final UserExistentException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("There is already an user for the given email", e);
            result = getOperationResultExistent(RESOURCE_MESSAGE, "email");
        }

        logger.debug("Returning the operation result after adding user: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @PUT
    @Path("/{id}")
    @PermitAll // user just need to be logged (with any role)
    public Response update(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the user {} with body {}", id, body);

        if (!securityContext.isUserInRole(User.Roles.ADMINISTRATOR.name())) {
            if (!isLoggedUser(id)) {
                return Response.status(HttpCode.FORBIDDEN.getCode()).build();
            }
        }

        final User user = userJsonConverter.convertFrom(body);
        user.setId(id);

        HttpCode httpCode = HttpCode.OK;
        OperationResult result;
        try {
            userServices.update(user);
            result = OperationResult.success();
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the user is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final UserExistentException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("There is already an user for the given email", e);
            result = getOperationResultExistent(RESOURCE_MESSAGE, "email");
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.NOT_FOUND;
            logger.error("No user found for the given id", e);
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        logger.debug("Returning the operation result after updating user: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @PUT
    @Path("/{id}/password")
    @PermitAll // user just need to be logged (with any role)
    public Response updatePassword(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the password for user {}", id);

        if (!securityContext.isUserInRole(User.Roles.ADMINISTRATOR.name())) {
            if (!isLoggedUser(id)) {
                return Response.status(HttpCode.FORBIDDEN.getCode()).build();
            }
        }

        HttpCode httpCode = HttpCode.OK;
        OperationResult result;
        try {
            userServices.updatePassword(id, getPasswordFromJson(body));
            result = OperationResult.success();
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.NOT_FOUND;
            logger.error("No user found for the given id", e);
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        logger.debug("Returning the operation result after updating user password: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMINISTRATOR"}) // role name have to match the one in web.xml
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find user by id: {}", id);
        ResponseBuilder responseBuilder;
        try {
            final User user = userServices.findById(id);
            final OperationResult result = OperationResult.success(userJsonConverter.convertToJsonElement(user));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("User found by id: {}", user);
        } catch (final UserNotFoundException e) {
            logger.error("No user found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    @POST
    @Path("/authenticate")
    @PermitAll
    public Response findByEmailAndPassword(final String body) {
        logger.debug("Find user by email and password");
        ResponseBuilder responseBuilder;
        try {
            final User userWithEmailAndPassword = getUserWithEmailAndPasswordFromJson(body);
            final User user = userServices.findByEmailAndPassword(userWithEmailAndPassword.getEmail(),
                    userWithEmailAndPassword.getPassword());
            final OperationResult result = OperationResult.success(userJsonConverter.convertToJsonElement(user));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("User found by email/password: {}", user);
        } catch (final UserNotFoundException e) {
            logger.error("No user found for email/password");
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    @POST
    @Path("/authenticate/jwt")
    @PermitAll
    public Response findByEmailAndPasswordAndSendJWT(final String body) {
        logger.debug("Find user by email and password");

        ResponseBuilder responseBuilder;
        try {
            final User userWithEmailAndPassword = getUserWithEmailAndPasswordFromJson(body);
            final User user = userServices.findByEmailAndPassword(userWithEmailAndPassword.getEmail(),
                    userWithEmailAndPassword.getPassword());
            final OperationResult result = OperationResult.success(JsonUtils.getJsonElementWithJWT(userJsonConverter, user));

            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("User found by email/password: {}", user);
        } catch (final UserNotFoundException e) {
            logger.error("No user found for email/password");
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    @GET
    @RolesAllowed({"ADMINISTRATOR"}) // list all the users in the system
    public Response findByFilter() {
        final UserFilter userFilter = new UserFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding users using filter: {}", userFilter);

        final PaginatedData<User> users = userServices.findByFilter(userFilter);

        logger.debug("Found {} users", users.getNumberOfRows());

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(users,
                userJsonConverter);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

    private boolean isLoggedUser(final Long id) {
        try {
            // getUserPrincipal.getName() returns identity of the user - in this case email
            final User loggerUser = userServices.findByEmail(securityContext.getUserPrincipal().getName());
            if (loggerUser.getId().equals(id)) {
                return true;
            }
        } catch (final UserNotFoundException e) {
        }
        return false;
    }

    private User getUserWithEmailAndPasswordFromJson(final String body) {
        final User user = new Student(); // The implementation does not matter

        final JsonObject jsonObject = JsonReader.readAsJsonObject(body);
        user.setEmail(JsonReader.getStringOrNull(jsonObject, "email"));
        user.setPassword(JsonReader.getStringOrNull(jsonObject, "password"));

        return user;
    }

    private String getPasswordFromJson(final String body) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(body);
        return JsonReader.getStringOrNull(jsonObject, "password");
    }

}