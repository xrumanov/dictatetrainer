/**
 * Converting JSON to User entity and vice versa
 */
package cz.muni.fi.dictatetrainer.user.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import cz.muni.fi.dictatetrainer.common.json.EntityJsonConverter;
import cz.muni.fi.dictatetrainer.common.json.JsonReader;
import cz.muni.fi.dictatetrainer.common.utils.DateUtils;
import cz.muni.fi.dictatetrainer.schoolclass.model.SchoolClass;
import cz.muni.fi.dictatetrainer.schoolclass.resource.SchoolClassJsonConverter;
import cz.muni.fi.dictatetrainer.user.model.Student;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import cz.muni.fi.dictatetrainer.user.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Converter from JSON to User object and vice versa
 */
@ApplicationScoped
public class UserJsonConverter implements EntityJsonConverter<User> {

    @Inject
    SchoolClassJsonConverter schoolClassJsonConverter;

    @Override
    public User convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final User user = getUserInstance(jsonObject);
        user.setName(JsonReader.getStringOrNull(jsonObject, "name"));
        user.setEmail(JsonReader.getStringOrNull(jsonObject, "email"));
        user.setPassword(JsonReader.getStringOrNull(jsonObject, "password"));
        if(user.getUserType().toString().equals("STUDENT")) {
            final SchoolClass schoolClass = new SchoolClass();
            schoolClass.setId(JsonReader.getLongOrNull(jsonObject, "schoolClassId"));
            user.setSchoolClass(schoolClass);
        }
        return user;
    }

    @Override
    public JsonElement convertToJsonElement(final User user) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("email", user.getEmail());
        if(user.getRoles().get(0) == User.Roles.STUDENT) {
            jsonObject.add("schoolClass", schoolClassJsonConverter.convertToJsonElement(user.getSchoolClass()));
        }
        jsonObject.addProperty("type", user.getUserType().toString());

        final JsonArray roles = new JsonArray();
        for (final User.Roles role : user.getRoles()) {
            roles.add(new JsonPrimitive(role.toString()));
        }
        jsonObject.add("roles", roles);
        jsonObject.addProperty("createdAt", DateUtils.formatDateTime(user.getCreatedAt()));

        return jsonObject;
    }

    public JsonElement convertToJsonElementSocial(final User user) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("email", user.getEmail());
        jsonObject.addProperty("type", user.getUserType().toString());

        final JsonArray roles = new JsonArray();
        for (final User.Roles role : user.getRoles()) {
            roles.add(new JsonPrimitive(role.toString()));
        }
        jsonObject.add("roles", roles);
        jsonObject.addProperty("createdAt", DateUtils.formatDateTime(user.getCreatedAt()));

        return jsonObject;
    }

    private User getUserInstance(final JsonObject userJson) {
        final User.UserType userType = User.UserType.valueOf(JsonReader.getStringOrNull(userJson, "type"));
        if (User.UserType.TEACHER.equals(userType)) {
            return new Teacher();
        }
        return new Student();
    }

}