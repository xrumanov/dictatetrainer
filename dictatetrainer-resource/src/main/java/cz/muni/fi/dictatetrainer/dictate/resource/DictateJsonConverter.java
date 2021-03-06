package cz.muni.fi.dictatetrainer.dictate.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.muni.fi.dictatetrainer.category.model.Category;
import cz.muni.fi.dictatetrainer.category.resource.CategoryJsonConverter;
import cz.muni.fi.dictatetrainer.common.json.EntityJsonConverter;
import cz.muni.fi.dictatetrainer.common.json.JsonReader;
import cz.muni.fi.dictatetrainer.dictate.model.Dictate;
import cz.muni.fi.dictatetrainer.user.model.Teacher;
import cz.muni.fi.dictatetrainer.user.model.User;
import cz.muni.fi.dictatetrainer.user.resource.UserJsonConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Converter from JSON to Dictate object and vice versa
 */
@ApplicationScoped
public class DictateJsonConverter implements EntityJsonConverter<Dictate> {


    @Inject
    CategoryJsonConverter categoryJsonConverter;

    @Inject
    UserJsonConverter userJsonConverter;

    @Override
    public Dictate convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final Dictate dictate = new Dictate();
        dictate.setName(JsonReader.getStringOrNull(jsonObject, "name"));
        dictate.setDescription(JsonReader.getStringOrNull(jsonObject, "description"));

        final Category category = new Category();
        category.setId(JsonReader.getLongOrNull(jsonObject, "categoryId"));
        dictate.setCategory(category);

        final User uploader = new Teacher();
        uploader.setId(JsonReader.getLongOrNull(jsonObject, "uploaderId"));
        dictate.setUploader(uploader);

        dictate.setFilename(JsonReader.getStringOrNull(jsonObject, "filename"));
        dictate.setTranscript(JsonReader.getStringOrNull(jsonObject, "transcript"));
        dictate.setDefaultRepetitionForDictate(JsonReader.getIntegerOrNull(jsonObject, "defaultRepetitionForDictate"));
        dictate.setDefaultRepetitionForSentences(JsonReader.getIntegerOrNull(jsonObject, "defaultRepetitionForSentences"));
        dictate.setDefaultPauseBetweenSentences(JsonReader.getIntegerOrNull(jsonObject, "defaultPauseBetweenSentences"));
        dictate.setSentenceEndings(JsonReader.getStringOrNull(jsonObject, "sentenceEndings"));


        return dictate;
    }

    @Override
    public JsonElement convertToJsonElement(final Dictate dictate) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", dictate.getId());
        jsonObject.addProperty("name", dictate.getName());
        jsonObject.addProperty("description", dictate.getDescription());
        jsonObject.addProperty("filename", dictate.getFilename());
        jsonObject.addProperty("transcript", dictate.getTranscript());
        jsonObject.addProperty("defaultRepetitionForDictate", dictate.getDefaultRepetitionForDictate());
        jsonObject.addProperty("defaultRepetitionForSentences", dictate.getDefaultRepetitionForSentences());
        jsonObject.addProperty("defaultPauseBetweenSentences", dictate.getDefaultPauseBetweenSentences());
        jsonObject.addProperty("sentenceEndings", dictate.getSentenceEndings());

        jsonObject.add("category", categoryJsonConverter.convertToJsonElement(dictate.getCategory()));
        jsonObject.add("uploader", userJsonConverter.convertToJsonElement(dictate.getUploader()));

        return jsonObject;
    }
}