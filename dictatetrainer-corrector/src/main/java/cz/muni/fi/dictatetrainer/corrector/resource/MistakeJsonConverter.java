package cz.muni.fi.dictatetrainer.corrector.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.corrector.model.Mistake;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Converter from JSON to Mistake object and vice versa
 */
@ApplicationScoped
public class MistakeJsonConverter {

    public JsonElement convertToJsonElement(final Mistake mistake) {
        return getMistakeAsJsonElement(mistake);
    }

    public JsonElement convertToJsonElement(final List<Mistake> mistakes) {
        final JsonArray jsonArray = new JsonArray();

        for (final Mistake mistake : mistakes) {
            jsonArray.add(getMistakeAsJsonElement(mistake));
        }

        return jsonArray;
    }

    private JsonElement getMistakeAsJsonElement(final Mistake mistake) {
        final JsonObject jsonObject = new JsonObject();
//        final JsonObject metaData = new JsonObject(); TODO


        jsonObject.addProperty("id", mistake.getId());

        jsonObject.addProperty("mistakeCharPosInWord", mistake.getMistakeCharPosInWord());
        jsonObject.addProperty("correctChars", mistake.getCorrectChars());
        jsonObject.addProperty("writtenChars", mistake.getWrittenChars());
        jsonObject.addProperty("correctWord", mistake.getCorrectWord());
        jsonObject.addProperty("writtenWord", mistake.getWrittenWord());
        jsonObject.addProperty("previousWord", mistake.getPreviousWord());
        jsonObject.addProperty("nextWord", mistake.getNextWord());
        jsonObject.addProperty("wordPosition", mistake.getWordPosition());
        jsonObject.addProperty("lemma", mistake.getLemma());
        jsonObject.addProperty("posTag", mistake.getPosTag());
        jsonObject.addProperty("sentence", mistake.getSentence());

//        jsonObject.add("metadata", metaData);
        jsonObject.addProperty("priority", mistake.getPriority());
        jsonObject.addProperty("mistakeType", mistake.getMistakeType().toString());
        jsonObject.addProperty("mistakeDescription", mistake.getMistakeDescription());

        return jsonObject;
    }

    public JsonElement getMistakesAsJsonElement(List<Mistake> mistakes) {

        final JsonObject jsonWithMistakesAndCount = new JsonObject();

        jsonWithMistakesAndCount.addProperty("totalMistakes", mistakes.size());
        jsonWithMistakesAndCount.add("mistakes", convertToJsonElement(mistakes));

        return jsonWithMistakesAndCount;
    }

    private JsonElement getMistakeAsJsonElementWithoutMetadata(final Mistake mistake) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", mistake.getId());
        jsonObject.addProperty("priority", mistake.getPriority());
        jsonObject.addProperty("mistakeType", mistake.getMistakeType().toString());
        jsonObject.addProperty("mistakeDescription", mistake.getMistakeDescription());

        return jsonObject;
    }

    public JsonElement convertToJsonElementWithoutMetadata(final List<Mistake> mistakes) {
        final JsonArray jsonArray = new JsonArray();

        for (final Mistake mistake : mistakes) {
            jsonArray.add(getMistakeAsJsonElementWithoutMetadata(mistake));
        }

        return jsonArray;
    }

    public JsonElement getMistakesAsJsonElementWithoutMetadata(List<Mistake> mistakes) {

        final JsonObject jsonWithMistakesAndCount = new JsonObject();

        jsonWithMistakesAndCount.addProperty("totalMistakes", mistakes.size());
        jsonWithMistakesAndCount.add("mistakes", convertToJsonElementWithoutMetadata(mistakes));

        return jsonWithMistakesAndCount;
    }
}
