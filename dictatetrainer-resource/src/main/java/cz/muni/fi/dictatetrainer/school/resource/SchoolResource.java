package cz.muni.fi.dictatetrainer.school.resource;

import com.google.gson.JsonElement;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.json.JsonUtils;
import cz.muni.fi.dictatetrainer.common.json.JsonWriter;
import cz.muni.fi.dictatetrainer.common.json.OperationResultJsonWriter;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.common.model.OperationResult;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.model.ResourceMessage;
import cz.muni.fi.dictatetrainer.school.exception.SchoolExistentException;
import cz.muni.fi.dictatetrainer.school.exception.SchoolNotFoundException;
import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.school.services.SchoolServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static cz.muni.fi.dictatetrainer.common.model.StandardsOperationResults.*;

/**
 * Resource that allows system to perform CRUD operations with School entities in Database
 */
@Path("/schools")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"TEACHER"})
public class SchoolResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("school");

    @Inject
    SchoolServices schoolServices;

    @Inject
    SchoolJsonConverter schoolJsonConverter;

    @POST
    public Response add(final String body) {
        logger.debug("Adding a new school with body {}", body);
        School school = schoolJsonConverter.convertFrom(body);

        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result;
        try {
            school = schoolServices.add(school);
            result = OperationResult.success(JsonUtils.getJsonElementWithId(school.getId()));
        } catch (final FieldNotValidException e) {
            logger.error("One of the fields of the school is not valid", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final SchoolExistentException e) {
            logger.error("There's already a school for the given name", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultExistent(RESOURCE_MESSAGE, "name");
        }

        logger.debug("Returning the operation result after adding school: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the school {} with body {}", id, body);
        final School school = schoolJsonConverter.convertFrom(body);
        school.setId(id);

        HttpCode httpCode = HttpCode.OK;
        OperationResult result;
        try {
            schoolServices.update(school);
            result = OperationResult.success();
        } catch (final FieldNotValidException e) {
            logger.error("One of the field of the school is not valid", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final SchoolExistentException e) {
            logger.error("There is already a school for the given name", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultExistent(RESOURCE_MESSAGE, "name");
        } catch (final SchoolNotFoundException e) {
            logger.error("No school found for the given id", e);
            httpCode = HttpCode.NOT_FOUND;
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        logger.debug("Returning the operation result after updating school: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find school: {}", id);
        Response.ResponseBuilder responseBuilder;
        try {
            final School school = schoolServices.findById(id);
            final OperationResult result = OperationResult
                    .success(schoolJsonConverter.convertToJsonElement(school));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("School found: {}", school);
        } catch (final SchoolNotFoundException e) {
            logger.error("No school found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    @GET
    @PermitAll
    public Response findAll() {
        logger.debug("Find all schools");

        final List<School> schools = schoolServices.findAll();

        logger.debug("Found {} schools", schools.size());

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(
                new PaginatedData<School>(schools.size(), schools), schoolJsonConverter);

        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

}