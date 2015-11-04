package cz.muni.fi.dictatetrainer.corrector.resource.common.json;

import com.google.gson.Gson;

/**
 * Auxiliary class for stringify JSON object
 */
public final class JsonWriter {

    private JsonWriter() {
    }

    public static String writeToString(final Object object) {
        if (object == null) {
            return "";
        }

        return new Gson().toJson(object);
    }

}