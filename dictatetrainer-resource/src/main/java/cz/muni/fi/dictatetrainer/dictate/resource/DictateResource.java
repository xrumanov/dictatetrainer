package cz.muni.fi.dictatetrainer.dictate.resource;

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
import cz.muni.fi.dictatetrainer.dictate.exception.DictateNotFoundException;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.model.filter.DictateFilter;
import cz.muni.fi.dictatetrainer.dictate.services.DictateServices;
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

@Path("/dictates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"TEACHER"})
public class DictateResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("dictate");

    @Inject
    DictateServices dictateServices;

    @Inject
    DictateJsonConverter dictateJsonConverter;

    @Context
    UriInfo uriInfo;

    @POST
    public Response add(final String body) {
        logger.debug("Adding a new dictate with body {}", body);
        Dictate dictate = dictateJsonConverter.convertFrom(body);

        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result;
        try {
            dictate = dictateServices.add(dictate);

            result = OperationResult.success(JsonUtils.getJsonElementWithId(dictate.getId()));
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the dictate is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final CategoryNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Category not found for dictate", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "category");
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Uploader not found for dictate", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "uploader");
        }

        logger.debug("Returning the operation result after adding dictate: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the dictate {} with body {}", id, body);
        final Dictate dictate = dictateJsonConverter.convertFrom(body);
        dictate.setId(id);

        HttpCode httpCode = HttpCode.OK;
        OperationResult result;
        try {
            dictateServices.update(dictate);
            result = OperationResult.success();
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the dictate is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final CategoryNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Category not found for dictate", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "category");
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Uploader not found for dictate", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "uploader");
        } catch (final DictateNotFoundException e) {
            httpCode = HttpCode.NOT_FOUND;
            logger.error("No dictate found for the given id", e);
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        logger.debug("Returning the operation result after updating dictate: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find dictate: {}", id);
        ResponseBuilder responseBuilder;
        try {
            final Dictate dictate = dictateServices.findById(id);
            final OperationResult result = OperationResult.success(dictateJsonConverter.convertToJsonElement(dictate));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("Dictate found: {}", dictate);
        } catch (final DictateNotFoundException e) {
            logger.error("No dictate found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    @GET
    @PermitAll
    public Response findByFilter() {

        final DictateFilter dictateFilter = new DictateFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding dictates using filter: {}", dictateFilter);

        final PaginatedData<Dictate> dictates = dictateServices.findByFilter(dictateFilter);

        logger.debug("Found {} dictates", dictates.getNumberOfRows());

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(dictates,
                dictateJsonConverter);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

}
