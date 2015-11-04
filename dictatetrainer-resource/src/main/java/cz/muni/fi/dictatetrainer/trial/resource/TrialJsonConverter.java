package cz.muni.fi.dictatetrainer.trial.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.common.json.EntityJsonConverter;
import cz.muni.fi.dictatetrainer.common.json.JsonReader;
import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.trial.model.Trial;
import cz.muni.fi.dictatetrainer.user.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Converter from JSON to Trial object and vice versa
 */
@ApplicationScoped
public class TrialJsonConverter implements EntityJsonConverter<Trial> {

    @Override
    public Trial convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final Trial trial = new Trial();
        trial.setTrialText(JsonReader.getStringOrNull(jsonObject, "trialText"));

        return trial;
    }

    @Override
    public JsonElement convertToJsonElement(final Trial trial) {
        return getTrialAsJsonElement(trial);
    }

    @Override
    public JsonElement convertToJsonElement(final List<Trial> trials) {
        final JsonArray jsonArray = new JsonArray();

        for (final Trial trial : trials) {
            jsonArray.add(getTrialAsJsonElement(trial));
        }

        return jsonArray;
    }

    private JsonElement getTrialAsJsonElement(final Trial trial) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", trial.getId());
        jsonObject.add("student", getStudentAsJsonElement(trial.getStudent()));
        jsonObject.add("dictate", getDictateAsJsonElement(trial.getDictate()));
        jsonObject.addProperty("text", trial.getTrialText());
        jsonObject.addProperty("performed", DateUtils.formatDateTime(trial.getPerformed()));
        return jsonObject;
    }

    private JsonElement getStudentAsJsonElement(final User user) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("name", user.getName());

        return jsonObject;
    }

    private JsonElement getDictateAsJsonElement(final Dictate dictate) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", dictate.getId());
        jsonObject.addProperty("name", dictate.getName());
        jsonObject.addProperty("category", dictate.getCategory().toString());
        jsonObject.addProperty("transcript", dictate.getTranscript());
        jsonObject.addProperty("filename", dictate.getFilename());
        jsonObject.addProperty("description", dictate.getDescription());

        return jsonObject;
    }
}
