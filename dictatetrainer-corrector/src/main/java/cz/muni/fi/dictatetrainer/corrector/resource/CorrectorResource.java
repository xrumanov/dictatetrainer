package cz.muni.fi.dictatetrainer.corrector.resource;

import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.corrector.exception.FieldNotValidException;
import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.resource.common.json.JsonReader;
import cz.muni.fi.dictatetrainer.corrector.resource.common.json.OperationResultJsonWriter;
import cz.muni.fi.dictatetrainer.corrector.resource.common.model.HttpCode;
import cz.muni.fi.dictatetrainer.corrector.resource.common.model.OperationResult;
import cz.muni.fi.dictatetrainer.corrector.resource.common.model.ResourceMessage;
import cz.muni.fi.dictatetrainer.corrector.rules.impl.CorrectorRulesNoContext;
import cz.muni.fi.dictatetrainer.corrector.service.CorrectorService;
import cz.muni.fi.dictatetrainer.corrector.service.impl.CorrectorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static cz.muni.fi.dictatetrainer.corrector.resource.common.model.StandardsOperationResults.getOperationResultInvalidField;

/**
 * Resource that allows user or other website to correct dictate
 * <p>
 * Request:
 * String with correct dictate (dictateTranscript)
 * String with text inputted by user (userText)
 * <p>
 * Response:
 * total number of mistakes in user text (numberOfMistakes)
 * array of Mistake objects
 * <p>
 * For this resource only, there is CORS enabled to allow anybody to request the resource
 */
@Path("/correctDictate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CorrectorResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("corrector");

    CorrectorService correctorSerivce = new CorrectorServiceImpl();

    MistakeJsonConverter mistakeJsonConverter = new MistakeJsonConverter();

    @Context
    UriInfo uriInfo;

    @POST
    public Response correct(final String body) {
        logger.debug("Adding a new corrector body {}", body);

        HttpCode httpCode = HttpCode.OK;
        final JsonObject jsonObject = JsonReader.readAsJsonObject(body);
        OperationResult result;

        try {
            String transcript = JsonReader.getStringOrNull(jsonObject, "dictateTranscript");
            String userText = JsonReader.getStringOrNull(jsonObject, "userText");

            String markedText = correctorSerivce.markInput(transcript, userText);
            String[] tokens = correctorSerivce.tokenizeAndReturnTokens(markedText, "\\s+");
            List<String> sentences = correctorSerivce.sentencizedAndReturnSentences(markedText, "cs");
            List<Mistake> mistakes = correctorSerivce.createMistakeObjectsAndApplyCorrectorRules(tokens, sentences,
                    new CorrectorRulesNoContext());

            result = OperationResult.success(mistakeJsonConverter.getMistakesAsJsonElement(mistakes, mistakeJsonConverter));

        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the dictate is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        }

        return Response
                .status(httpCode.getCode())
                .entity(OperationResultJsonWriter.toJson(result))
                .build();
    }
}