package cz.muni.fi.dictatetrainer.school.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.common.json.EntityJsonConverter;
import cz.muni.fi.dictatetrainer.common.json.JsonReader;

import javax.enterprise.context.ApplicationScoped;

/**
 * Converter from JSON to School object and vice versa
 */
@ApplicationScoped
public class SchoolJsonConverter implements EntityJsonConverter<School> {

    @Override
    public School convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final School school = new School();
        school.setName(JsonReader.getStringOrNull(jsonObject, "name"));

        return school;
    }

    @Override
    public JsonElement convertToJsonElement(final School school) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", school.getId());
        jsonObject.addProperty("name", school.getName());
        return jsonObject;
    }

}