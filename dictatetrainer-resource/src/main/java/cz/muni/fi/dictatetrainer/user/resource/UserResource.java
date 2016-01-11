/**
 * REST resources for user entity
 */
package cz.muni.fi.dictatetrainer.user.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.IOException;
import java.util.Map;

import static cz.muni.fi.dictatetrainer.common.model.StandardsOperationResults.*;

/**
 * Resource that allows system to perform CRUD operations with User entities in Database
 * also to authenticate user in the system and update password
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("Uživatel");

    public static final String CLIENT_ID_KEY = "client_id", REDIRECT_URI_KEY = "redirect_uri",
            CLIENT_SECRET = "client_secret", CODE_KEY = "code", GRANT_TYPE_KEY = "grant_type",
            AUTH_CODE = "authorization_code";

    public static final ObjectMapper MAPPER = new ObjectMapper();

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
//        if (user.getUserType().equals(User.UserType.TEACHER)) {
//            return Response.status(HttpCode.FORBIDDEN.getCode()).build();
//        }

        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result;
        try {
            user = userServices.add(user);
            //changed from returning id to returning jwt
            result = OperationResult.success(JsonUtils.getJsonElementWithId(user.getId()));
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

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMINISTRATOR")
    public Response delete(@PathParam("id") final Long id) {
        logger.debug("Find dictate: {}", id);
        ResponseBuilder responseBuilder;
        try {
            userServices.delete(id);
            final OperationResult result = OperationResult.success(JsonUtils.getJsonElementWithId(id));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("Dictate deleted: {}", id);
        } catch (final UserNotFoundException e) {
            logger.error("No dictate found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    @GET
    @Path("/{id}")
    @PermitAll// role name have to match the one in web.xml
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find user by id: {}", id);
        ResponseBuilder responseBuilder;
        try {
//            if ((!securityContext.isUserInRole(User.Roles.ADMINISTRATOR.name())) ||
//                    (!securityContext.isUserInRole(User.Roles.TEACHER.name()))) {
//                if (!isLoggedUser(id)) {
//                    return Response.status(HttpCode.FORBIDDEN.getCode()).build();
//                }
//            }
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
            final OperationResult result = OperationResult.error("User/Password error", "User/Password pair not found");
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode()).entity(OperationResultJsonWriter.toJson(result));
        }

        return responseBuilder.build();
    }

    @POST
    @Path("/authenticate/facebook")
    @PermitAll
    public Response authenticateFacebook(@Valid final Payload payload,
                                         @Context final HttpServletRequest request) throws IOException {

        Client client = ClientBuilder.newClient();
        final String accessTokenUrl = "https://graph.facebook.com/v2.5/oauth/access_token";
        final String graphApiUrl = "https://graph.facebook.com/v2.5/me?fields=id,name,email";

        Response response;

        // Step 1. Exchange authorization code for access token.

        //get the params from the URL
        response =
                client.target(accessTokenUrl)
                        .queryParam(CLIENT_ID_KEY, payload.getClientId())
                        .queryParam(REDIRECT_URI_KEY, payload.getRedirectUri())
                        .queryParam(CLIENT_SECRET, "c27441f7e8eb54fcb87c046ef2aa4510")
                        .queryParam(CODE_KEY, payload.getCode())
                        .request("text/plain").accept(MediaType.TEXT_PLAIN).get();

        // save the response in the responseEntity
        Map<String, Object> responseEntity = getResponseEntity(response);


        // use the response in another request
        response =
                client.target(graphApiUrl)
                        .queryParam("access_token", responseEntity.get("access_token"))
                        .queryParam("expires_in", responseEntity.get("expires_in"))
                        .request("text/plain").get();

//         save the response in the responseEntity
        final Map<String, Object> userInfo = getResponseEntity(response);

        logger.warn(userInfo.get("id").toString() + " " +
                userInfo.get("name").toString());

        User user = new Student();
        user.setName(userInfo.get("name").toString());
        if (userInfo.get("email") == null) {
            user.setEmail("");
        } else {
            // if user with email already exists
//            if(user)
            user.setEmail(userInfo.get("email").toString());
        }

        // Step 3. return the partial response for future use
        final OperationResult result = OperationResult.success(userJsonConverter.convertToJsonElementSocial(user));
        return Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();

    }

//    @POST
//    @Path("/authenticate/google")
//    @PermitAll
//    public Response loginGoogle(@Valid final Payload payload,
//                                @Context final HttpServletRequest request) throws IOException {
//
//        Client client = ClientBuilder.newClient();
//        final String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";
//        final String peopleApiUrl = "https://www.googleapis.com/plus/v1/people/me/openIdConnect";
//
//        Response response;
//
//        // Step 1. Exchange authorization code for access token.
//        final MultivaluedMap<String, String> accessData = new MultivaluedHashMap<String, String>();
//        accessData.add(CLIENT_ID_KEY, payload.getClientId());
//        accessData.add(REDIRECT_URI_KEY, payload.getRedirectUri());
//        accessData.add(CLIENT_SECRET, "KCnEF73zy9Q3Ye4ACxsvUWM0");
//        accessData.add(CODE_KEY, payload.getCode());
//        accessData.add(GRANT_TYPE_KEY, AUTH_CODE);
//        response = client.target(accessTokenUrl).request().post(Entity.form(accessData));
//        accessData.clear();
//
//        // Step 2. Retrieve profile information about the current user.
//        final String accessToken = (String) getResponseEntity(response).get("access_token");
//        response =
//                client.target(peopleApiUrl).request("text/plain")
//                        .header("Authorization", String.format("Bearer %s", accessToken)).get();
//        final Map<String, Object> userInfo = getResponseEntity(response);
//
//        // Step 3. Process the authenticated the user.
//        return processUser(request, "google", userInfo.get("sub").toString(),
//                userInfo.get("name").toString(), userInfo.get("email").toString());
//    }

    @GET
    @RolesAllowed({"ADMINISTRATOR", "TEACHER"}) // list all the users in the system
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

    private Map<String, Object> getResponseEntity(final Response response) throws IOException {
        return MAPPER.readValue(response.readEntity(String.class),
                new TypeReference<Map<String, Object>>() {
                });
    }

//    private Response processUser(final HttpServletRequest request, final String provider,
//                                 final String id, final String displayName, final String email) {
//
//        ResponseBuilder responseBuilder;
////        // Step 3a. If user is already signed in then link accounts.
//        try {
//            User user = userServices.findByEmail(email);
//            final OperationResult result = OperationResult.success(JsonUtils.getJsonElementWithJWT(userJsonConverter, user));
//
//            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
//            logger.debug("User found by email/password: {}", user);
//
//            // Step 3b. Create a new user account or return an existing one.
//        } catch (UserNotFoundException unfe) {
//            User userToStore = new Student();
//            userToStore.setName(displayName);
//            userToStore.setEmail(email);
//
//            SchoolClass sc = new SchoolClass();
//            sc.setId(1L);
//
//            // create password from id and some salting TODO
//            userToStore.setPassword("PaSsWoRd");
//            HttpCode httpCode = HttpCode.CREATED;
//            OperationResult result;
//
//            userToStore = userServices.add(userToStore);
//
//            result = OperationResult.success(JsonUtils.getJsonElementWithJWT(userJsonConverter, userToStore));
//
//            logger.debug("Returning the operation result after adding user: {}", result);
//            responseBuilder =  Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result));
//
//        }
//
//        return responseBuilder.build();
//    }

    public static class Payload {
        @NotNull
        String clientId;

        @NotNull
        String redirectUri;

        @NotNull
        String code;

        public String getClientId() {
            return clientId;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public String getCode() {
            return code;
        }
    }

}