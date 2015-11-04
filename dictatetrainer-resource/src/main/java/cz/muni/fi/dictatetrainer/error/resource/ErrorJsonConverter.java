package cz.muni.fi.dictatetrainer.error.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.common.json.EntityJsonConverter;
import cz.muni.fi.dictatetrainer.common.json.JsonReader;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.dictate.resource.DictateJsonConverter;
import cz.muni.fi.dictatetrainer.error.model.Error;
import cz.muni.fi.dictatetrainer.user.model.Student;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.resource.UserJsonConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Converter from JSON to Error object and vice versa
 */
@ApplicationScoped
public class ErrorJsonConverter implements EntityJsonConverter<Error> {

    @Inject
    DictateJsonConverter dictateJsonConverter;

    @Inject
    UserJsonConverter userJsonConverter;

    @Override
    public Error convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final Error error = new Error();
        error.setCorrectWord(JsonReader.getStringOrNull(jsonObject, "correctWord"));
        error.setWrittenWord(JsonReader.getStringOrNull(jsonObject, "writtenWord"));

        final Dictate dictate = new Dictate();
        dictate.setId(JsonReader.getLongOrNull(jsonObject, "dictateId"));
        error.setDictate(dictate);

        final User student = new Student();
        student.setId(JsonReader.getLongOrNull(jsonObject, "studentId"));
        error.setStudent(student);

        error.setErrorPriority(JsonReader.getIntegerOrNull(jsonObject, "errorPriority"));
        error.setErrorDescription(JsonReader.getStringOrNull(jsonObject, "errorDescription"));

        return error;
    }

    @Override
    public JsonElement convertToJsonElement(final Error error) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", error.getId());
        jsonObject.addProperty("correctWord", error.getCorrectWord());
        jsonObject.addProperty("writtenWord", error.getWrittenWord());
        jsonObject.addProperty("errorPriority", error.getErrorPriority());
        jsonObject.addProperty("description", error.getErrorDescription());

        jsonObject.add("dictate", dictateJsonConverter.convertToJsonElement(error.getDictate()));
        jsonObject.add("student", userJsonConverter.convertToJsonElement(error.getStudent()));

        return jsonObject;
    }

}