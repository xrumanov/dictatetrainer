package cz.muni.fi.dictatetrainer.common.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;

public interface EntityJsonConverter<T> {

    T convertFrom(String json);

    JsonElement convertToJsonElement(T entity);

	// default method - Java8 feature
    default JsonElement convertToJsonElement(final List<T> entities) {
        final JsonArray jsonArray = new JsonArray();

        for (final T entity : entities) {
            jsonArray.add(convertToJsonElement(entity));
        }

        return jsonArray;
    }

}