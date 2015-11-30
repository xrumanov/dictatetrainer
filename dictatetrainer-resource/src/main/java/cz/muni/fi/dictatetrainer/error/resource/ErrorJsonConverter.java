package cz.muni.fi.dictatetrainer.error.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.common.json.EntityJsonConverter;
import cz.muni.fi.dictatetrainer.common.json.JsonReader;
import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.resource.DictateJsonConverter;
import cz.muni.fi.dictatetrainer.error.model.Error;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.trial.resource.TrialJsonConverter;
import cz.muni.fi.dictatetrainer.user.model.Student;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.resource.UserJsonConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Converter from JSON to Error object and vice versa
 */
@ApplicationScoped
public class ErrorJsonConverter implements EntityJsonConverter<Error> {

//    @Inject
//    DictateJsonConverter dictateJsonConverter;
//
//    @Inject
//    UserJsonConverter userJsonConverter;

    @Inject
    TrialJsonConverter trialJsonConverter;

    @Override
    public Error convertFrom(final String json) {

        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final Error error = new Error();

        error.setMistakeCharPosInWord(JsonReader.getIntegerOrNull(jsonObject, "mistakeCharPosInWord"));
        error.setCorrectChars(JsonReader.getStringOrNull(jsonObject, "correctChars"));
        error.setWrittenChars(JsonReader.getStringOrNull(jsonObject, "writtenChars"));
        error.setCorrectWord(JsonReader.getStringOrNull(jsonObject, "correctWord"));
        error.setWrittenWord(JsonReader.getStringOrNull(jsonObject, "writtenWord"));
        error.setPreviousWord(JsonReader.getStringOrNull(jsonObject, "previousWord"));
        error.setNextWord(JsonReader.getStringOrNull(jsonObject, "nextWord"));
        error.setWordPosition(JsonReader.getIntegerOrNull(jsonObject, "wordPosition"));
        error.setLemma(JsonReader.getStringOrNull(jsonObject, "lemma"));
        error.setPosTag(JsonReader.getStringOrNull(jsonObject, "posTag"));
        error.setSentence(JsonReader.getStringOrNull(jsonObject, "sentence"));
        error.setErrorPriority(JsonReader.getIntegerOrNull(jsonObject, "errorPriority"));
        error.setErrorType(Error.ErrorType.valueOf(JsonReader.getStringOrNull(jsonObject, "errorType")));
        error.setErrorDescription(JsonReader.getStringOrNull(jsonObject, "errorDescription"));

        final Dictate dictate = new Dictate();
        dictate.setId(JsonReader.getLongOrNull(jsonObject, "dictateId"));
        error.setDictate(dictate);

        final Student student = new Student();
        student.setId(JsonReader.getLongOrNull(jsonObject, "studentId"));
        error.setStudent(student);

        final Trial trial = new Trial();
        trial.setId(JsonReader.getLongOrNull(jsonObject, "trialId"));
        error.setTrial(trial);


        return error;
    }

    @Override
    public JsonElement convertToJsonElement(final Error error) {
        return getErrorAsJsonElement(error);
    }

    @Override
    public JsonElement convertToJsonElement(final List<Error> errors) {
        final JsonArray jsonArray = new JsonArray();

        for (final Error error : errors) {
            jsonArray.add(getErrorAsJsonElement(error));
        }

        return jsonArray;
    }

    private JsonElement getErrorAsJsonElement(final Error error) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", error.getId());

        jsonObject.addProperty("mistakeCharPosInWord", error.getMistakeCharPosInWord());
        jsonObject.addProperty("correctChars", error.getCorrectChars());
        jsonObject.addProperty("writtenChars", error.getWrittenChars());
        jsonObject.addProperty("correctWord", error.getCorrectWord());
        jsonObject.addProperty("writtenWord", error.getWrittenWord());
        jsonObject.addProperty("previousWord", error.getPreviousWord());
        jsonObject.addProperty("nextWord", error.getNextWord());
        jsonObject.addProperty("wordPosition", error.getWordPosition());
        jsonObject.addProperty("lemma", error.getLemma());
        jsonObject.addProperty("posTag", error.getPosTag());
        jsonObject.addProperty("sentence", error.getSentence());

        jsonObject.addProperty("errorPriority", error.getErrorPriority());
        jsonObject.addProperty("errorType", error.getErrorType().toString());
        jsonObject.addProperty("errorDescription", error.getErrorDescription());

        jsonObject.add("student", getStudentAsJsonElement(error.getStudent()));
        jsonObject.add("dictate", getDictateAsJsonElement(error.getDictate()));
        jsonObject.add("trial", trialJsonConverter.convertToJsonElement(error.getTrial()));

        return jsonObject;
    }

    private JsonElement getStudentAsJsonElement(final User user) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", user.getId());

        return jsonObject;
    }

    private JsonElement getDictateAsJsonElement(final Dictate dictate) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", dictate.getId());

        return jsonObject;
    }
}
