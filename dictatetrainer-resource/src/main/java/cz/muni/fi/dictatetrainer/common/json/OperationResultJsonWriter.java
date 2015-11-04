package cz.muni.fi.dictatetrainer.common.json;

import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.common.model.OperationResult;

/**
 * Auxiliary method for creating JSON when operation is successful or
 * creating JSON with error identification and description
 */
public final class OperationResultJsonWriter {

    private OperationResultJsonWriter() {
    }

    public static String toJson(final OperationResult operationResult) {
        return JsonWriter.writeToString(getJsonObject(operationResult));
    }

    private static Object getJsonObject(final OperationResult operationResult) {
        if (operationResult.isSuccess()) {
            return getJsonSuccess(operationResult);
        }
        return getJsonError(operationResult);
    }

    private static Object getJsonSuccess(final OperationResult operationResult) {
        return operationResult.getEntity();
    }

    private static JsonObject getJsonError(final OperationResult operationResult) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("errorIdentification", operationResult.getErrorIdentification());
        jsonObject.addProperty("errorDescription", operationResult.getErrorDescription());

        return jsonObject;
    }

}