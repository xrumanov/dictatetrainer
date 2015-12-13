package cz.muni.fi.dictatetrainer.schoolclass.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.common.json.EntityJsonConverter;
import cz.muni.fi.dictatetrainer.common.json.JsonReader;
import cz.muni.fi.dictatetrainer.school.model.School;
import cz.muni.fi.dictatetrainer.school.resource.SchoolJsonConverter;
import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.resource.UserJsonConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Converter from JSON to SchoolClass object and vice versa
 */
@ApplicationScoped
public class SchoolClassJsonConverter implements EntityJsonConverter<SchoolClass> {

    @Inject
    SchoolJsonConverter schoolJsonConverter;

    @Inject
    UserJsonConverter userJsonConverter;

    @Override
    public SchoolClass convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(JsonReader.getStringOrNull(jsonObject, "name"));

        final School school = new School();
        school.setId(JsonReader.getLongOrNull(jsonObject, "schoolId"));
        schoolClass.setSchool(school);

        final User teacher = new Teacher();
        teacher.setId(JsonReader.getLongOrNull(jsonObject, "teacherId"));
        schoolClass.setTeacher(teacher);

        return schoolClass;
    }

    @Override
    public JsonElement convertToJsonElement(final SchoolClass schoolClass) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", schoolClass.getId());
        jsonObject.addProperty("name", schoolClass.getName());
        jsonObject.add("school", schoolJsonConverter.convertToJsonElement(schoolClass.getSchool()));
        jsonObject.add("teacher", userJsonConverter.convertToJsonElement(schoolClass.getTeacher()));

        return jsonObject;
    }
}