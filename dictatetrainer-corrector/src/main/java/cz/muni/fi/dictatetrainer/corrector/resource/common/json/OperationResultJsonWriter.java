/**
 * Write the operational result
 */
package cz.muni.fi.dictatetrainer.corrector.resource.common.json;

import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.corrector.resource.common.model.OperationResult;

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