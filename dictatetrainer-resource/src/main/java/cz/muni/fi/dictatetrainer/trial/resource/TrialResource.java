package cz.muni.fi.dictatetrainer.trial.resource;

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
import cz.muni.fi.dictatetrainer.trial.exception.TrialNotFoundException;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.trial.model.filter.TrialFilter;
import cz.muni.fi.dictatetrainer.trial.services.TrialServices;
import cz.muni.fi.dictatetrainer.user.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static cz.muni.fi.dictatetrainer.common.model.StandardsOperationResults.getOperationResultDependencyNotFound;
import static cz.muni.fi.dictatetrainer.common.model.StandardsOperationResults.getOperationResultInvalidField;

/**
 * Resource that allows system to perform CRUD operations with Trial entities in Database
 */
@Path("/trials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrialResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("Pokus");

    @Inject
    TrialServices trialServices;

    @Inject
    TrialJsonConverter trialJsonConverter;

    @Context
    SecurityContext securityContext;

    @Context
    UriInfo uriInfo;

    @POST
    @RolesAllowed({"STUDENT"})
    public Response add(final String body) {
        logger.debug("Adding a new trial with body {}", body);
        Trial trial = trialJsonConverter.convertFrom(body);

        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result;
        try {
            trial = trialServices.add(trial);
            result = OperationResult.success(JsonUtils.getJsonElementWithId(trial.getId()));
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the trial is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Student not found for trial", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "student");
        } catch (final DictateNotFoundException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("Dictate not found for trial", e);
            result = getOperationResultDependencyNotFound(RESOURCE_MESSAGE, "dictate");
        }

        logger.debug("Returning the operation result after adding trial: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find trial by id: {}", id);
        Response.ResponseBuilder responseBuilder;
        try {
            final Trial trial = trialServices.findById(id);
            final OperationResult result = OperationResult.success(trialJsonConverter.convertToJsonElement(trial));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("Trial found by id: {}", trial);
        } catch (final TrialNotFoundException e) {
            logger.error("No trial found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }
        return responseBuilder.build();
    }

    @GET
    @PermitAll
    public Response findByFilter() {
        final TrialFilter trialFilter = new TrialFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding trials using filter: {}", trialFilter);

        final PaginatedData<Trial> trials = trialServices.findByFilter(trialFilter);

        logger.debug("Found {} trials", trials.getNumberOfRows());

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(trials,
                trialJsonConverter);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }
}
