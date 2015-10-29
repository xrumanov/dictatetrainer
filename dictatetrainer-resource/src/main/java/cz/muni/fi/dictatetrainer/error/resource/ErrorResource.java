package cz.muni.fi.dictatetrainer.error.resource;

import com.google.gson.JsonElement;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.json.JsonUtils;
import cz.muni.fi.dictatetrainer.common.json.JsonWriter;
import cz.muni.fi.dictatetrainer.common.json.OperationResultJsonWriter;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.common.model.OperationResult;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.model.ResourceMessage;
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.error.exception.ErrorNotFoundException;
import cz.muni.fi.dictatetrainer.error.model.Error;
import cz.muni.fi.dictatetrainer.error.model.filter.ErrorFilter;
import cz.muni.fi.dictatetrainer.error.services.ErrorServices;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import static cz.muni.fi.dictatetrainer.common.model.StandardsOperationResults.*;

@Path("/errors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"TEACHER"})
public class ErrorResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("error");

    @Inject
    ErrorServices errorServices;

    @Inject
    ErrorJsonConverter errorJsonConverter;

    @Context
    UriInfo uriInfo;

    @POST
    public Response add(final String body) {
        logger.debug("Adding a new error with body {}", body);
        Error error = errorJsonConverter.convertFrom(body);

        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result;
        try {
            error = errorServices.add(error);

            result = OperationResult.success(JsonUtils.getJsonElementWithId(error.getId()));
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the error is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final DictateNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Dictate not found for error", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "category");
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Student not found for error", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "uploader");
        }

        logger.debug("Returning the operation result after adding error: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the error {} with body {}", id, body);
        final Error error = errorJsonConverter.convertFrom(body);
        error.setId(id);

        HttpCode httpCode = HttpCode.OK;
        OperationResult result;
        try {
            errorServices.update(error);
            result = OperationResult.success();
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the error is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final DictateNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Dictate not found for error", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "category");
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Student not found for error", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "uploader");
        } catch (final ErrorNotFoundException e) {
            httpCode = HttpCode.NOT_FOUND;
            logger.error("No error found for the given id", e);
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        logger.debug("Returning the operation result after updating error: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find error: {}", id);
        ResponseBuilder responseBuilder;
        try {
            final Error error = errorServices.findById(id);
            final OperationResult result = OperationResult.success(errorJsonConverter.convertToJsonElement(error));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("Error found: {}", error);
        } catch (final ErrorNotFoundException e) {
            logger.error("No error found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    @GET
    @PermitAll
    public Response findByFilter() {

        final ErrorFilter errorFilter = new ErrorFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding errors using filter: {}", errorFilter);

        final PaginatedData<Error> errors = errorServices.findByFilter(errorFilter);

        logger.debug("Found {} errors", errors.getNumberOfRows());

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(errors,
                errorJsonConverter);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }
}