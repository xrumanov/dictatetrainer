package cz.muni.fi.dictatetrainer.corrector.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.model.MistakeType;
import cz.muni.fi.dictatetrainer.corrector.resource.common.json.EntityJsonConverter;
import cz.muni.fi.dictatetrainer.corrector.resource.common.json.JsonReader;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Converter from JSON to Mistake object and vice versa
 */
@ApplicationScoped
public class MistakeJsonConverter implements EntityJsonConverter<Mistake> {

    @Override
    public Mistake convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        //TODO change the structure according to getMistakeAsJsonElement maybe I don't need it at all
        final Mistake mistake = new Mistake();

        mistake.setWordPosition(JsonReader.getIntegerOrNull(jsonObject, "wordPosition"));
        mistake.setCorrectWord(JsonReader.getStringOrNull(jsonObject, "correctWord"));
        mistake.setWrittenWord(JsonReader.getStringOrNull(jsonObject, "writtenWord"));
        mistake.setPriority(JsonReader.getIntegerOrNull(jsonObject, "priority"));
        mistake.setMistakeType(MistakeType.valueOf(JsonReader.getStringOrNull(jsonObject, "mistakeType")));
        mistake.setMistakeDescription(JsonReader.getStringOrNull(jsonObject, "mistakeText"));


        return mistake;
    }

    @Override
    public JsonElement convertToJsonElement(final Mistake mistake) {
        return getMistakeAsJsonElement(mistake);
    }

    @Override
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

    public JsonElement getMistakesAsJsonElement(List<Mistake> mistakes, EntityJsonConverter entityJsonConverter) {

        final JsonObject jsonWithMistakesAndCount = new JsonObject();

        jsonWithMistakesAndCount.addProperty("totalMistakes", mistakes.size());
        jsonWithMistakesAndCount.add("mistakes", entityJsonConverter.convertToJsonElement(mistakes));

        return jsonWithMistakesAndCount;
    }
}
