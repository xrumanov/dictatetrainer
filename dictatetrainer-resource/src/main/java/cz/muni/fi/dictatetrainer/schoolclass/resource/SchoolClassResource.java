package cz.muni.fi.dictatetrainer.schoolclass.resource;

import com.google.gson.JsonElement;
import cz.muni.fi.dictatetrainer.category.exception.CategoryNotFoundException;
import cz.muni.fi.dictatetrainer.common.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.common.json.JsonUtils;
import cz.muni.fi.dictatetrainer.common.json.JsonWriter;
import cz.muni.fi.dictatetrainer.common.json.OperationResultJsonWriter;
import cz.muni.fi.dictatetrainer.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.common.model.OperationResult;
import cz.muni.fi.dictatetrainer.common.model.PaginatedData;
import cz.muni.fi.dictatetrainer.common.model.ResourceMessage;
import cz.muni.fi.dictatetrainer.school.exception.SchoolNotFoundException;
import cz.muni.fi.dictatetrainer.schoolclass.exception.SchoolClassNotFoundException;
import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.schoolclass.model.filter.SchoolClassFilter;
import cz.muni.fi.dictatetrainer.schoolclass.services.SchoolClassServices;
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

/**
 * Resource that allows system to perform CRUD operations with SchoolClass entities in Database
 */
@Path("/schoolclasses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"TEACHER"})
public class SchoolClassResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("Školská třída");

    @Inject
    SchoolClassServices schoolClassServices;

    @Inject
    SchoolClassJsonConverter schoolClassJsonConverter;

    @Context
    UriInfo uriInfo;

    @POST
    public Response add(final String body) {
        logger.debug("Adding a new schoolClass with body {}", body);
        SchoolClass schoolClass = schoolClassJsonConverter.convertFrom(body);

        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result;
        try {
            schoolClass = schoolClassServices.add(schoolClass);

            result = OperationResult.success(JsonUtils.getJsonElementWithId(schoolClass.getId()));
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the schoolclass is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final SchoolNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("School not found for schoolclass", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "school");
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Teacher not found for schoolclass", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "teacher");
        }

        logger.debug("Returning the operation result after adding schoolclass: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the schoolclass {} with body {}", id, body);
        final SchoolClass schoolClass = schoolClassJsonConverter.convertFrom(body);
        schoolClass.setId(id);

        HttpCode httpCode = HttpCode.OK;
        OperationResult result;
        try {
            schoolClassServices.update(schoolClass);
            result = OperationResult.success();
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the schoolclass is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final SchoolNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("School not found for schoolclass", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "school");
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Teacher not found for schoolclass", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "teacher");
        } catch (final SchoolClassNotFoundException e) {
            httpCode = HttpCode.NOT_FOUND;
            logger.error("No schoolclass found for the given id", e);
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        logger.debug("Returning the operation result after updating schoolclass: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find schoolclass: {}", id);
        ResponseBuilder responseBuilder;
        try {
            final SchoolClass schoolClass = schoolClassServices.findById(id);
            final OperationResult result = OperationResult.success(schoolClassJsonConverter.convertToJsonElement(schoolClass));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("SchoolClass found: {}", schoolClass);
        } catch (final SchoolClassNotFoundException e) {
            logger.error("No schoolclass found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    @GET
    @PermitAll
    public Response findByFilter() {

        final SchoolClassFilter schoolClassFilter = new SchoolClassFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding schoolclasses using filter: {}", schoolClassFilter);

        final PaginatedData<SchoolClass> schoolClasses = schoolClassServices.findByFilter(schoolClassFilter);

        logger.debug("Found {} schoolclasses", schoolClasses.getNumberOfRows());

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(schoolClasses,
                schoolClassJsonConverter);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

}
