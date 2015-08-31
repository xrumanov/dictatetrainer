/**
 * Write object to string
 */
package cz.muni.fi.dictatetrainer.common.json;

import com.google.gson.Gson;

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